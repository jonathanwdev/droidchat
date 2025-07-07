package com.jonathan.droidchat.ui.features.chats

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.jonathan.droidchat.navigation.Routes

fun NavController.navigateToChats(
    navOptions: NavOptions? = null
) {
    this.navigate(Routes.ChatsRoute, navOptions)
}