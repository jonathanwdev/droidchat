package com.jonathan.droidchat.ui.di

import com.jonathan.droidchat.ui.features.signin.SignInFormState
import com.jonathan.droidchat.ui.features.signin.SignInFormValidator
import com.jonathan.droidchat.ui.features.signup.SignUpFormState
import com.jonathan.droidchat.ui.features.signup.SignUpFormValidator
import com.jonathan.droidchat.ui.validator.FormValidator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
interface FormValidatorModule {

    @Binds
    fun bindSignUpFormValidator(
        signUpFormValidator: SignUpFormValidator
    ): FormValidator<SignUpFormState>

    @Binds
    fun bindSignInFormValidator(
        signInFormValidator: SignInFormValidator
    ): FormValidator<SignInFormState>
}