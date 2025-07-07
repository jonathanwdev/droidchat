package com.jonathan.droidchat.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.jonathan.droidchat.navigation.DroidChatNavigationState
import com.jonathan.droidchat.navigation.TopLevelDestination
import com.jonathan.droidchat.navigation.rememberDroidChatNavigationState
import com.jonathan.droidchat.ui.theme.DroidChatTheme
import kotlin.reflect.KClass

@Composable
fun BottomNavigationMenu(
    navigationState: DroidChatNavigationState,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .padding(top = 1.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp
    ) {
        navigationState.topLevelDestinations.forEach { topLevelDest ->
            if (topLevelDest == TopLevelDestination.PLUS_BUTTON) {
                FloatingActionButton(
                    onClick = {
                        navigationState.navigateToTopLevelDestination(topLevelDest)
                    },
                    modifier = Modifier
                        .padding(top = 6.dp),
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp,
                        hoveredElevation = 0.dp,
                        focusedElevation = 0.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                }
            } else {
                val selected =
                    navigationState.currentDestination.isRouteInHierarchy(topLevelDest.route)
                val label = topLevelDest.titleRes?.let {
                    stringResource(it)
                }
                NavigationBarItem(
                    selected = selected,
                    onClick = {
                        navigationState.navigateToTopLevelDestination(topLevelDest)
                    },
                    icon = {
                        topLevelDest.iconRes?.let { icon ->
                            Icon(
                                painter = painterResource(id = icon),
                                contentDescription = label
                            )
                        }
                    },
                    label = {
                        label?.let {
                            Text(text = label)
                        }
                    },
                    alwaysShowLabel = true,
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.primary,
                        selectedIconColor = MaterialTheme.colorScheme.onSurface,
                        selectedTextColor = MaterialTheme.colorScheme.onSurface,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,

                        )
                )
            }
        }
    }
}

private fun NavDestination?.isRouteInHierarchy(route: KClass<*>) = this?.hierarchy?.any {
    it.hasRoute(route)
} ?: false


@Preview
@Composable
private fun BottomNavigationMenuPrev() {
    DroidChatTheme {
        BottomNavigationMenu(
            navigationState = rememberDroidChatNavigationState()
        )
    }
}