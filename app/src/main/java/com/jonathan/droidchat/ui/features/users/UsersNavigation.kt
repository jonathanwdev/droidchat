package com.jonathan.droidchat.ui.features.users

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.jonathan.droidchat.navigation.Routes

fun NavController.navigateToUsers(
    navOptions: NavOptions? = null
) {
    this.navigate(Routes.UsersRoute, navOptions)
}