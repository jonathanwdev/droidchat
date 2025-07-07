package com.jonathan.droidchat.ui.features.signup

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonathan.droidchat.R
import com.jonathan.droidchat.data.repository.AuthRepository
import com.jonathan.droidchat.model.CreateAccount
import com.jonathan.droidchat.model.NetworkException
import com.jonathan.droidchat.ui.validator.FormValidator
import com.jonathan.droidchat.util.image.ImageCompressor
import com.jonathan.droidchat.util.image.ImageCompressorImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val formValidator: FormValidator<SignUpFormState>,
    private val authRepository: AuthRepository,
    private val imageCompressor: ImageCompressor
) : ViewModel() {
    var formState by mutableStateOf(SignUpFormState())
        private set

    fun onFormEvent(event: SignUpFormEvent) {
        when (event) {
            is SignUpFormEvent.EmailChange -> {
                formState = formState.copy(email = event.email)
            }

            is SignUpFormEvent.FirstNameChange -> {
                formState = formState.copy(firstName = event.firstName)
            }

            is SignUpFormEvent.LastNameChange -> {
                formState = formState.copy(lastName = event.lastName)
            }

            is SignUpFormEvent.PasswordChange -> {
                formState = formState.copy(password = event.password)
                updateIsEqualPass()
            }

            is SignUpFormEvent.PasswordConfirmationChange -> {
                formState = formState.copy(passwordConfirmation = event.passwordConfirmation)
                updateIsEqualPass()
            }

            is SignUpFormEvent.ProfilePictureChange -> {
                formState = formState.copy(profilePictureUri = event.uri)
                event.uri?.let { img ->
                    compressImage(img)
                }
            }

            is SignUpFormEvent.ChangeModalBottomSheetVisibility -> {
                formState =
                    formState.copy(isModalBottomSheetVisible = event.modalBottomSheetVisibility)
            }

            is SignUpFormEvent.Submit -> {
                doSignUp()
            }
        }
    }

    private fun compressImage(uri: Uri) {
        viewModelScope.launch {
            try {
                formState = formState.copy(isCompressingImg = true)
                val compressedImg = imageCompressor.compressAndResizeImage(
                    imageUri = uri
                )
                formState = formState.copy(profilePictureUri = compressedImg.toUri())

            } catch (err: Exception) {
                //error
            } finally {
                formState = formState.copy(isCompressingImg = false)
            }
        }
    }


    private fun updateIsEqualPass() {
        formState = formState.copy(
            isEqualPassword = if (formState.password.isNotEmpty() && formState.password == formState.passwordConfirmation) {
                R.string.feature_sign_up_passwords_match
            } else null
        )
    }

    fun successMessageShown() {
        formState = formState.copy(isSignedUp = false)
    }

    fun onClearError() {
        formState = formState.copy(apiErrorMessageHasId = null)
    }

    private fun isFormValid(): Boolean {
        return !formValidator.validate(formState).also {
            formState = it
        }.hasError
    }

    private fun doSignUp() {
        if (isFormValid()) {
            formState = formState.copy(isLoading = true)
            viewModelScope.launch {
                var profilePictureId: Int? = null
                var errorWhileUpload = false

                formState.profilePictureUri?.path?.let { path ->
                    authRepository.uploadProfilePicture(path).fold(
                        onSuccess = { image ->
                            profilePictureId = image.id
                        },
                        onFailure = {
                            formState = formState.copy(
                                isLoading = false,
                                profilePictureUri = null,
                                apiErrorMessageHasId = R.string.error_message_profile_picture_uploading_failed
                            )
                            errorWhileUpload = true
                        }
                    )
                }
                if(errorWhileUpload) return@launch
                authRepository.signUp(
                    request = CreateAccount(
                        username = formState.email,
                        password = formState.password,
                        lastName = formState.lastName,
                        firstName = formState.firstName,
                        profilePictureId = profilePictureId
                    )
                ).fold(
                    onSuccess = {
                        formState = formState.copy(
                            isLoading = false,
                            isSignedUp = true
                        )
                    },
                    onFailure = {
                        formState = formState.copy(
                            isLoading = false,
                            apiErrorMessageHasId = if (it is NetworkException.ApiException) {
                                when (it.statusCode) {
                                    400 -> R.string.error_message_api_form_validation_failed
                                    409 -> R.string.error_message_user_with_username_already_exists
                                    else -> R.string.common_generic_error_message
                                }
                            } else R.string.common_generic_error_message
                        )
                    }
                )
            }
        }
    }
}