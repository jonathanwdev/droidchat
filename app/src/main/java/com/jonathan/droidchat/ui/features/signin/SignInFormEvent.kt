package com.jonathan.droidchat.ui.features.signin

sealed interface SignInFormEvent {
    data class EmailChange(val email: String) : SignInFormEvent
    data class PasswordChange(val password: String) : SignInFormEvent
    data object Submit : SignInFormEvent
}