package com.jonathan.droidchat.ui.features.signup

import android.net.Uri
import androidx.annotation.StringRes

data class SignUpFormState(
    val profilePictureUri: Uri? = null,
    val firstName: String = "",
    @StringRes
    val firstNameError: Int? = null,
    val lastName: String = "",
    @StringRes
    val lastNameError: Int? = null,
    val password: String = "",
    @StringRes
    val passwordError: Int? = null,
    val email: String = "",
    @StringRes
    val emailError: Int? = null,
    val passwordConfirmation: String = "",
    @StringRes
    val passwordConfirmationError: Int? = null,
    @StringRes
    val isEqualPassword: Int? = null,
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
    val isModalBottomSheetVisible: Boolean = false,
    val isSignedUp: Boolean = false,
    val apiErrorMessageHasId: Int? = null,
    val isCompressingImg: Boolean = false
)

