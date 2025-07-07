package com.jonathan.droidchat.ui.validator

interface FormValidator<FormState> {
    fun validate(formState: FormState): FormState
}