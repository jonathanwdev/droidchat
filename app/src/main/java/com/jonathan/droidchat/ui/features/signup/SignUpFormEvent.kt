package com.jonathan.droidchat.ui.features.signup

import android.net.Uri

sealed interface SignUpFormEvent {
    data class ProfilePictureChange(val uri: Uri?): SignUpFormEvent
    data class FirstNameChange(val firstName: String) : SignUpFormEvent
    data class LastNameChange(val lastName: String): SignUpFormEvent
    data class EmailChange(val email: String) : SignUpFormEvent
    data class PasswordChange(val password: String) : SignUpFormEvent
    data class PasswordConfirmationChange(val passwordConfirmation: String) : SignUpFormEvent
    data class ChangeModalBottomSheetVisibility(val modalBottomSheetVisibility: Boolean): SignUpFormEvent
    data object Submit: SignUpFormEvent
}