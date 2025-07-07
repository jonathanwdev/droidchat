package com.jonathan.droidchat.ui.features.signin

import androidx.annotation.StringRes

data class SignInFormState(
    val email: String = "",
    @StringRes
    val emailError: Int? = null,
    val password: String = "",
    @StringRes
    val passwordError: Int? = null,
    val isLoading: Boolean = false,
    val hasError: Boolean = false
)