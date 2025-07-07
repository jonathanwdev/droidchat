package com.jonathan.droidchat.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.jonathan.droidchat.R
import kotlin.reflect.KClass

enum class TopLevelDestination(
    @StringRes val titleRes: Int?,
    @DrawableRes val iconRes: Int?,
    val route: KClass<*>
) {
    CHATS(
        titleRes = R.string.bottom_navigation_item_chats,
        iconRes = R.drawable.ic_bottom_nav_chats,
        route = Routes.ChatsRoute::class
    ),
    PLUS_BUTTON(
        titleRes = null,
        iconRes = null,
        route = Routes.UsersRoute::class
    ),
    PROFILE(
        titleRes = R.string.bottom_navigation_item_profile,
        iconRes = R.drawable.ic_bottom_nav_profile,
        route = Routes.ProfileRoute::class
    ),
}