package com.jonathan.droidchat.ui.validator

object EmailValidator {
    private const val EMAIL_REGEX = """^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,6}$"""

    fun isValid(value: String): Boolean {
        return Regex(EMAIL_REGEX).matches(value)
    }
}

