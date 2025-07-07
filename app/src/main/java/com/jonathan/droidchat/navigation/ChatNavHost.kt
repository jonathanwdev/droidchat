package com.jonathan.droidchat.navigation

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.navOptions
import com.jonathan.droidchat.navigation.extension.slideInTo
import com.jonathan.droidchat.navigation.extension.slideOutTo
import com.jonathan.droidchat.ui.features.chatdetail.ChatDetailScreen
import com.jonathan.droidchat.ui.features.chats.ChatsScreen
import com.jonathan.droidchat.ui.features.chats.navigateToChats
import com.jonathan.droidchat.ui.features.signin.SignInScreen
import com.jonathan.droidchat.ui.features.signup.SignUpScreen
import com.jonathan.droidchat.ui.features.splash.SplashScreen
import com.jonathan.droidchat.ui.features.users.UsersScreen


const val CHAT_BASE_DETAIL_URI = "droidchat://chat_detail"

@SuppressLint("ContextCastToActivity")
@Composable
fun ChatNavHost(
    navigationState: DroidChatNavigationState
) {
    val navController = navigationState.navController
    val activity = LocalActivity.current

    NavHost(
        navController = navController,
        startDestination = navigationState.startDestination
    ) {
        composable<Routes.SplashRoute>(
            enterTransition = {
                this.slideInTo(AnimatedContentTransitionScope.SlideDirection.Right)
            },
            exitTransition = {
                this.slideOutTo(AnimatedContentTransitionScope.SlideDirection.Left)
            }
        ) {
            SplashScreen(
                onNavigateToSignIn = {
                    navController.navigate(
                        route = Routes.SignInRoute,
                        navOptions = navOptions {
                            popUpTo(Routes.SplashRoute) {
                                inclusive = true
                            }
                        }
                    )
                },
                onNavigateToMain = {
                    navController.navigateToChats(
                        navOptions = navOptions {
                            popUpTo(Routes.SplashRoute) {
                                inclusive = true
                            }
                        }
                    )
                },
                onCloseApp = {
                    activity?.finish()
                }
            )
        }
        composable<Routes.SignInRoute>(
            enterTransition = {
                this.slideInTo(AnimatedContentTransitionScope.SlideDirection.Right)
            },
            exitTransition = {
                this.slideOutTo(AnimatedContentTransitionScope.SlideDirection.Left)
            }
        ) {
            SignInScreen(
                navigateToSignUp = {
                    navController.navigate(Routes.SignUpRoute)
                },
                navigateToMain = {
                    navController.navigateToChats(
                        navOptions = navOptions {
                            popUpTo(Routes.SignInRoute) {
                                inclusive = true
                            }
                        }
                    )
                }
            )
        }
        composable<Routes.SignUpRoute>(
            enterTransition = {
                this.slideInTo(AnimatedContentTransitionScope.SlideDirection.Left)
            },
            exitTransition = {
                this.slideOutTo(AnimatedContentTransitionScope.SlideDirection.Right)
            }
        ) {
            SignUpScreen(
                onSignUpSuccess = {
                    navController.popBackStack()
                }
            )
        }
        composable<Routes.ChatsRoute>() {
            ChatsScreen(
                navigateToChatDetail = { chat ->
                    navController.navigate(Routes.ChatDetailRoute(chat.otherMember.id))
                }
            )
        }
        composable<Routes.UsersRoute>() {
            UsersScreen(
                navigateToChatClicked = { userId ->
                    navController.navigate(Routes.ChatDetailRoute(userId))
                }
            )
        }

        composable<Routes.ChatDetailRoute>(
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "$CHAT_BASE_DETAIL_URI/{userId}"
                }

            )
        ) {
            ChatDetailScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}