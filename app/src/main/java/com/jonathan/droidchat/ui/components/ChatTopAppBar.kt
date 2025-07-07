package com.jonathan.droidchat.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jonathan.droidchat.ui.theme.DroidChatTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable() (RowScope.() -> Unit) = {},
    expandedHeight: Dp = 100.dp,
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.primary,
        titleContentColor = MaterialTheme.colorScheme.onPrimary
    ),
    scrollBehavior: TopAppBarScrollBehavior? = null

) {
    TopAppBar(
        title = title,
        colors = colors,
        modifier = modifier,
        navigationIcon = navigationIcon,
        actions = actions,
        scrollBehavior = scrollBehavior,
        expandedHeight = expandedHeight,
        windowInsets = windowInsets
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun ChatTopAppBarPreview() {
    DroidChatTheme {
        ChatTopAppBar(
            title = {
                Text("hello world")
            }
        )
    }
}