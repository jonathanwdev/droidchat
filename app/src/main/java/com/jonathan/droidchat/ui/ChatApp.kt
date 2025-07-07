package com.jonathan.droidchat.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.jonathan.droidchat.navigation.ChatNavHost
import com.jonathan.droidchat.navigation.DroidChatNavigationState
import com.jonathan.droidchat.navigation.rememberDroidChatNavigationState
import com.jonathan.droidchat.ui.components.BottomNavigationMenu
import com.jonathan.droidchat.ui.theme.Grey1

@Composable
fun ChatApp(
    droidChatNavigationState: DroidChatNavigationState
) {

    val topLevelDes = remember(droidChatNavigationState.topLevelDestinations) {
        droidChatNavigationState.topLevelDestinations.toSet()
    }

    Scaffold(
        bottomBar = {
            if(droidChatNavigationState.currentTopLevelDestination in topLevelDes) {
                BottomNavigationMenu(
                    navigationState = droidChatNavigationState
                )
            }
        },
        containerColor = Grey1,
        contentWindowInsets = WindowInsets(0,0,0,0)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
        ) {
            ChatNavHost(
                navigationState = droidChatNavigationState
            )
        }
    }
}