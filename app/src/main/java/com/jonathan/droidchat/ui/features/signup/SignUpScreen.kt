package com.jonathan.droidchat.ui.features.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jonathan.droidchat.R
import com.jonathan.droidchat.ui.components.AppDialog
import com.jonathan.droidchat.ui.components.PrimaryButton
import com.jonathan.droidchat.ui.components.ProfilePictureModalBottomSheet
import com.jonathan.droidchat.ui.components.ProfilePictureSelector
import com.jonathan.droidchat.ui.components.SecondaryTextField
import com.jonathan.droidchat.ui.theme.BackgroundGradient
import com.jonathan.droidchat.ui.theme.DroidChatTheme
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    onSignUpSuccess: () -> Unit,
) {
    val formState = viewModel.formState

    SignUpView(
        formState = formState,
        onFormEvent = viewModel::onFormEvent
    )

    if(formState.isSignedUp) {
        AppDialog(
            onDismissRequest = {
                viewModel.successMessageShown()
                onSignUpSuccess()
            },
            onConfirmButtonClick = {
                viewModel.successMessageShown()
                onSignUpSuccess()
            },
            text = stringResource(id = R.string.feature_sign_up_success),
        )
    }


    formState.apiErrorMessageHasId?.let { error ->
        AppDialog(
            onDismissRequest = viewModel::onClearError,
            onConfirmButtonClick = viewModel::onClearError,
            text = stringResource(id = error),
            title = stringResource(id = R.string.common_generic_error_title)
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpView(
    formState: SignUpFormState,
    onFormEvent: (SignUpFormEvent) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()


    Box(
        modifier = Modifier
            .background(brush = BackgroundGradient)
            .verticalScroll(state = scrollState)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(56.dp))
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                modifier = Modifier
                    .fillMaxSize(),
                shape = MaterialTheme.shapes.extraLarge.copy(
                    bottomStart = CornerSize(0.dp),
                    bottomEnd = CornerSize(0.dp)
                ),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ProfilePictureSelector(
                        imageUri = formState.profilePictureUri,
                        isCompressingImg = formState.isCompressingImg,
                        modifier = Modifier.clickable {
                            onFormEvent(
                                SignUpFormEvent.ChangeModalBottomSheetVisibility(
                                    modalBottomSheetVisibility = true
                                )
                            )
                        },
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    SecondaryTextField(
                        label = stringResource(id = R.string.feature_sign_up_first_name),
                        value = formState.firstName,
                        errorText = formState.firstNameError?.let {
                            stringResource(
                                id = it,
                                stringResource(id = R.string.feature_sign_up_first_name)
                            )
                        },
                        onValueChange = { value ->
                            onFormEvent(SignUpFormEvent.FirstNameChange(firstName = value))
                        }
                    )
                    Spacer(modifier = Modifier.height(22.dp))
                    SecondaryTextField(
                        label = stringResource(id = R.string.feature_sign_up_last_name),
                        value = formState.lastName,
                        errorText = formState.lastNameError?.let {
                            stringResource(
                                id = it,
                                stringResource(id = R.string.feature_sign_up_last_name)
                            )
                        },
                        onValueChange = { value ->
                            onFormEvent(SignUpFormEvent.LastNameChange(value))
                        }
                    )
                    Spacer(modifier = Modifier.height(22.dp))
                    SecondaryTextField(
                        label = stringResource(id = R.string.feature_sign_up_email),
                        value = formState.email,
                        errorText = formState.emailError?.let { stringResource(it) },
                        keyboardType = KeyboardType.Email,
                        onValueChange = { value ->
                            onFormEvent(SignUpFormEvent.EmailChange(value))
                        }
                    )
                    Spacer(modifier = Modifier.height(22.dp))
                    SecondaryTextField(
                        label = stringResource(id = R.string.feature_sign_up_password),
                        value = formState.password,
                        keyboardType = KeyboardType.Password,
                        errorText = formState.passwordError?.let { stringResource(it) },
                        extraText = formState.isEqualPassword?.let { stringResource(it) },
                        onValueChange = { value ->
                            onFormEvent(SignUpFormEvent.PasswordChange(value))
                        }
                    )
                    Spacer(modifier = Modifier.height(22.dp))
                    SecondaryTextField(
                        label = stringResource(id = R.string.feature_sign_up_password_confirmation),
                        value = formState.passwordConfirmation,
                        keyboardType = KeyboardType.Password,
                        errorText = formState.passwordConfirmationError?.let { stringResource(it) },
                        extraText = formState.isEqualPassword?.let { stringResource(it) },
                        onValueChange = { value ->
                            onFormEvent(SignUpFormEvent.PasswordConfirmationChange(value))
                        },
                        imeAction = ImeAction.Done
                    )
                    Spacer(modifier = Modifier.height(22.dp))
                    PrimaryButton(
                        text = stringResource(id = R.string.feature_sign_up_button),
                        onClick = {
                            onFormEvent(SignUpFormEvent.Submit)
                        },
                        isLoading = formState.isLoading
                    )
                }
            }

            if (formState.isModalBottomSheetVisible) {
                ProfilePictureModalBottomSheet(
                    onPictureSelected = { picture ->
                        onFormEvent(SignUpFormEvent.ProfilePictureChange(picture))
                        scope.launch {
                            sheetState.hide()
                        }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                onFormEvent(SignUpFormEvent.ChangeModalBottomSheetVisibility(false))
                            }
                        }
                    },
                    onDismissRequest = {
                        onFormEvent(SignUpFormEvent.ChangeModalBottomSheetVisibility(false))
                    },
                    sheetState = sheetState
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun SignUpViewPreview() {
    DroidChatTheme {
        SignUpView(
            formState = SignUpFormState(),
            onFormEvent = {}
        )
    }
}