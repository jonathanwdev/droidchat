package com.jonathan.droidchat.ui.features.signin

import com.jonathan.droidchat.R
import com.jonathan.droidchat.ui.validator.EmailValidator
import com.jonathan.droidchat.ui.validator.FormValidator
import com.jonathan.droidchat.ui.validator.PasswordValidator
import javax.inject.Inject

class SignInFormValidator @Inject constructor(): FormValidator<SignInFormState> {
    override fun validate(formState: SignInFormState): SignInFormState {
        val isEmailValid = EmailValidator.isValid(formState.email)
        val isPasswordValid = PasswordValidator.isValid(formState.password)

        val hasError = listOf(
            isPasswordValid,
            isEmailValid
        ).any { !it }

        return formState.copy(
            emailError = if (!isEmailValid) R.string.error_message_email_invalid else null,
            passwordError = if (!isPasswordValid) R.string.error_message_password_invalid else null,
            hasError = hasError,
        )
    }

}