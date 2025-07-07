package com.jonathan.droidchat.navigation

import kotlinx.serialization.Serializable

interface Routes {
    @Serializable
    data object SplashRoute: Routes

    @Serializable
    object SignInRoute

    @Serializable
    object SignUpRoute

    @Serializable
    data object ChatsRoute: Routes

    @Serializable
    object UsersRoute

    @Serializable
    object ProfileRoute

    @Serializable
    data class ChatDetailRoute(
        val userId: Int
    )
}