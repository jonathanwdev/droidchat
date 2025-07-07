package com.jonathan.droidchat.ui.features.signin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jonathan.droidchat.R
import com.jonathan.droidchat.ui.components.AppDialog
import com.jonathan.droidchat.ui.components.PrimaryButton
import com.jonathan.droidchat.ui.components.PrimaryTextField
import com.jonathan.droidchat.ui.theme.BackgroundGradient
import com.jonathan.droidchat.ui.theme.DroidChatTheme
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignInScreen(
    viewModel: SignInViewModel = hiltViewModel(),
    navigateToSignUp: () -> Unit,
    navigateToMain: () -> Unit
) {
    val formState = viewModel.formState

//    val genericErrorMessage = stringResource(R.string.common_generic_error_message)
    var showUnauthorizedError by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(true) {
        viewModel.signInAction.collect  { action ->
            when (action) {
                SignInViewModel.SignInAction.Success -> {
                    navigateToMain()
                }

                is SignInViewModel.SignInAction.Error -> {
                    when (action) {
                        SignInViewModel.SignInAction.Error.GenericError -> {
                            //
                        }
                        SignInViewModel.SignInAction.Error.UnauthorizedError -> {
                            showUnauthorizedError = true
                        }
                    }
                }
            }
        }
    }
    if (showUnauthorizedError) {
        AppDialog(
            onDismissRequest = {
                showUnauthorizedError = false
            },
            onConfirmButtonClick = {
                showUnauthorizedError = false
            },
            title = stringResource(R.string.common_generic_error_title),
            text = stringResource(R.string.error_message_invalid_username_or_password),
        )
    }

    SignInView(
        formState = formState,
        onFormEvent = viewModel::onFormEvent,
        onRegisterClick = navigateToSignUp
    )
}

@Composable
private fun SignInView(
    formState: SignInFormState,
    onFormEvent: (SignInFormEvent) -> Unit,
    onRegisterClick: () -> Unit
) {
    val noAccountText = stringResource(R.string.feature_login_no_account)
    val registerText = stringResource(R.string.feature_login_register)
    val noAccountRegisterText = "$noAccountText $registerText"

    val annotatedString = buildAnnotatedString {
        val registerTextStartIdx = noAccountRegisterText.indexOf(registerText)
        val registerTextEndIdx = registerTextStartIdx + registerText.length
        append(noAccountRegisterText)
        addStyle(
            style = SpanStyle(
                color = Color.White
            ),
            start = 0,
            end = registerTextStartIdx
        )
        addLink(
            clickable = LinkAnnotation.Clickable(
                tag = "register_text",
                styles = TextLinkStyles(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline
                    )
                ),
                linkInteractionListener = {
                    onRegisterClick()
                }
            ),
            start = registerTextStartIdx,
            end = registerTextEndIdx
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .background(brush = BackgroundGradient)
            .imePadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(height = 78.dp))
        PrimaryTextField(
            value = formState.email,
            onValueChange = { email ->
                onFormEvent(SignInFormEvent.EmailChange(email))
            },
            errorMessage = formState.emailError?.let { stringResource(id = it) },
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.spacing_medium)),
            leadingIcon = R.drawable.ic_envelope,
            placeholder = stringResource(R.string.feature_login_email),
            keyboardType = KeyboardType.Email
        )
        Spacer(modifier = Modifier.height(14.dp))
        PrimaryTextField(
            value = formState.password,
            onValueChange = { password ->
                onFormEvent(SignInFormEvent.PasswordChange(password))
            },
            errorMessage = formState.passwordError?.let { stringResource(it) },
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.spacing_medium)),
            leadingIcon = R.drawable.ic_lock,
            placeholder = stringResource(R.string.feature_login_password),
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        )
        Spacer(modifier = Modifier.height(98.dp))
        PrimaryButton(
            text = stringResource(R.string.feature_login_button),
            isLoading = formState.isLoading,
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.spacing_medium)),
            onClick = {
                onFormEvent(SignInFormEvent.Submit)
            }
        )
        Spacer(modifier = Modifier.height(56.dp))
        Text(
            text = annotatedString
        )

    }
}


@Preview(showSystemUi = true)
@Composable
private fun SignInScreenPreview() {
    DroidChatTheme {
        SignInView(
            formState = SignInFormState(),
            onFormEvent = {},
            onRegisterClick = {}
        )
    }
}

