package com.jonathan.droidchat

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavHostController
import com.jonathan.droidchat.navigation.Routes
import com.jonathan.droidchat.navigation.rememberDroidChatNavigationState
import com.jonathan.droidchat.ui.ChatApp
import com.jonathan.droidchat.ui.theme.DroidChatTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {

            DroidChatTheme {
                val navigationState = rememberDroidChatNavigationState()
                navController = navigationState.navController
                val startDestination = if(intent.data == null) Routes.SplashRoute else Routes.ChatsRoute
                navigationState.startDestination = startDestination
                ChatApp(
                    droidChatNavigationState = navigationState
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if(intent.action == Intent.ACTION_SEND && intent.data!!.scheme == "droidchat") {
            val data = intent.data!!
            when(data.host) {
                "chat_detail" -> {
                    val userId = data.lastPathSegment?.toInt() ?: return
                    navController.navigate(Routes.ChatDetailRoute(userId))
                }
            }
        }
    }
}

