package com.jonathan.droidchat.ui.features.signin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonathan.droidchat.data.repository.AuthRepository
import com.jonathan.droidchat.model.NetworkException
import com.jonathan.droidchat.ui.validator.FormValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val formValidator: FormValidator<SignInFormState>,
    private val authRepository: AuthRepository
) : ViewModel() {
    var formState by mutableStateOf(SignInFormState())
        private set

    private val _signInAction = MutableSharedFlow<SignInAction>()
    val signInAction = _signInAction.asSharedFlow()

    fun onFormEvent(event: SignInFormEvent) {
        when (event) {
            is SignInFormEvent.EmailChange -> {
                formState = formState.copy(email = event.email, emailError = null)
            }

            is SignInFormEvent.PasswordChange -> {
                formState = formState.copy(password = event.password, passwordError = null)
            }

            SignInFormEvent.Submit -> {
                doSignIn()
            }
        }
    }

    private fun isFormValid(): Boolean {
        return !formValidator.validate(formState).also {
            formState = it
        }.hasError
    }

    private fun doSignIn() {
        if(isFormValid()) {
            formState = formState.copy(isLoading = true)
            viewModelScope.launch {
                authRepository.signIn(
                    username = formState.email,
                    password = formState.password
                ).fold(
                    onSuccess = {
                        formState = formState.copy(isLoading = false)
                        println("****** is success viewmodel *****")
                        _signInAction.emit(SignInAction.Success)
                    },
                    onFailure = {
                        formState = formState.copy(isLoading = false)
                        val error = if(it is NetworkException.ApiException && it.statusCode == 401){
                            SignInAction.Error.UnauthorizedError
                        } else {
                            SignInAction.Error.GenericError

                        }
                        _signInAction.emit(error)

                    }
                )
            }
        }
    }

    sealed interface SignInAction {
        data object Success : SignInAction
        sealed interface  Error : SignInAction {
            data object GenericError : Error
            data object UnauthorizedError : Error
        }
    }


}