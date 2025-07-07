package com.jonathan.droidchat.ui.features.chats

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jonathan.droidchat.R
import com.jonathan.droidchat.model.Chat
import com.jonathan.droidchat.model.User
import com.jonathan.droidchat.model.fake.user1
import com.jonathan.droidchat.ui.components.AnimatedContent
import com.jonathan.droidchat.ui.components.ChatItem
import com.jonathan.droidchat.ui.components.ChatItemShimmer
import com.jonathan.droidchat.ui.components.ChatScaffold
import com.jonathan.droidchat.ui.components.ChatTopAppBar
import com.jonathan.droidchat.ui.components.GeneralEmptyList
import com.jonathan.droidchat.ui.components.GeneralError
import com.jonathan.droidchat.ui.components.NotificationPermanentlyDeniedInfo
import com.jonathan.droidchat.ui.components.PrimaryButton
import com.jonathan.droidchat.ui.notification.NotificationPermissionHandler
import com.jonathan.droidchat.ui.preview.ChatListPreviewProviderParameter
import com.jonathan.droidchat.ui.theme.DroidChatTheme
import com.jonathan.droidchat.ui.theme.Grey1

@Composable
fun ChatsScreen(
    viewModel: ChatsViewModel = hiltViewModel(),
    navigateToChatDetail: (Chat) -> Unit,
    context: Context = LocalContext.current
) {
    val user by viewModel.currentUserFlow.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showPermissionDeniedMessage by remember {
        mutableStateOf(false)
    }

    ChatsView(
        user = user,
        uiState = uiState,
        showPermissionPermanentlyDeniedInfo = showPermissionDeniedMessage,
        onDismissPermanentlyDeniedInfoClick = {
            showPermissionDeniedMessage = false
        },
        onGoToSettingsPermanentlyDeniedInfoClick = {
            context.startActivity(
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
            )
            showPermissionDeniedMessage = false
        },
        onTryAgainClick = {
            viewModel.getChats(isRefresh = true)
        },
        onChatClick = navigateToChatDetail,
    )
    NotificationPermissionHandler(
        onPermissionPermanentlyDenied = {
            showPermissionDeniedMessage = true
        },
        onPermissionGranted = {

        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatsView(
    user: User?,
    uiState: ChatsViewModel.ChatsListUiState,
    showPermissionPermanentlyDeniedInfo: Boolean,
    onDismissPermanentlyDeniedInfoClick: () -> Unit,
    onGoToSettingsPermanentlyDeniedInfoClick: () -> Unit,
    onTryAgainClick: () -> Unit,
    onChatClick: (Chat) -> Unit
) {
    ChatScaffold(topBar = {
        ChatTopAppBar(title = {
            Text(
                text = AnnotatedString.fromHtml(
                    stringResource(
                        R.string.feature_chats_greeting, user?.firstName ?: ""
                    )
                ),
                style = MaterialTheme.typography.titleLarge
            )
        })
    }) {
        when (uiState) {
            ChatsViewModel.ChatsListUiState.Loading -> {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    repeat(5) {
                        ChatItemShimmer()
                    }
                }
            }

            is ChatsViewModel.ChatsListUiState.Success -> {
                when (uiState.chats.isNotEmpty()) {
                    true -> {
                        ChatsListContent(
                            chats = uiState.chats,
                            showPermissionPermanentlyDeniedInfo = showPermissionPermanentlyDeniedInfo,
                            onDismissPermanentlyDeniedInfoClick = onDismissPermanentlyDeniedInfoClick,
                            onGoToSettingsPermanentlyDeniedInfoClick = onGoToSettingsPermanentlyDeniedInfoClick,
                            onChatClick = onChatClick
                        )
                    }

                    else -> {
                        GeneralEmptyList(
                            message = stringResource(R.string.feature_chats_empty_list),
                            resource = {
                                AnimatedContent(
                                    resId = R.raw.animation_empty_list
                                )
                            })
                    }
                }


            }

            is ChatsViewModel.ChatsListUiState.Error -> {
                GeneralError(
                    title = stringResource(R.string.common_generic_error_title),
                    message = stringResource(R.string.common_generic_error_message),
                    resource = {
                        AnimatedContent()
                    },
                    action = {
                        PrimaryButton(
                            text = stringResource(R.string.common_retry), onClick = onTryAgainClick
                        )
                    }

                )
            }
        }
    }
}


@Composable
fun ChatsListContent(
    modifier: Modifier = Modifier,
    chats: List<Chat>,
    showPermissionPermanentlyDeniedInfo: Boolean,
    onDismissPermanentlyDeniedInfoClick: () -> Unit,
    onGoToSettingsPermanentlyDeniedInfoClick: () -> Unit,
    onChatClick: (Chat) -> Unit
) {
    LazyColumn(
        modifier = modifier, contentPadding = PaddingValues(
            horizontal = 16.dp
        )
    ) {
        if (showPermissionPermanentlyDeniedInfo) {
            item(key = "notification_info") {
                NotificationPermanentlyDeniedInfo(
                    modifier = Modifier.padding(top = 16.dp),
                    onDismissClick = onDismissPermanentlyDeniedInfoClick,
                    onGoToSettingsClick = onGoToSettingsPermanentlyDeniedInfoClick
                )
            }
        }

        itemsIndexed(chats, key = { _, item -> item.id }) { index, chat ->
            ChatItem(
                chat = chat,

                onClick = {
                    onChatClick(chat)
                }
            )
            if (index < chats.lastIndex) {
                HorizontalDivider(
                    thickness = 1.dp, color = Grey1
                )
            }
        }


    }
}

@Preview(showSystemUi = true)
@Composable
private fun ChatsViewLoadingPreview() {
    DroidChatTheme {
        ChatsView(
            user = user1,
            uiState = ChatsViewModel.ChatsListUiState.Loading,
            onTryAgainClick = {},
            onChatClick = {},
            showPermissionPermanentlyDeniedInfo = false,
            onDismissPermanentlyDeniedInfoClick = {},
            onGoToSettingsPermanentlyDeniedInfoClick = {},
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ChatsViewSuccessPreview(
    @PreviewParameter(ChatListPreviewProviderParameter::class)
    chats: List<Chat>
) {
    DroidChatTheme {
        ChatsView(
            user = user1,
            uiState = ChatsViewModel.ChatsListUiState.Success(
                chats = chats
            ),
            onTryAgainClick = {},
            onChatClick = {},
            showPermissionPermanentlyDeniedInfo = true,
            onDismissPermanentlyDeniedInfoClick = {},
            onGoToSettingsPermanentlyDeniedInfoClick = {},
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ChatsViewEmptyListPreview() {
    DroidChatTheme {
        ChatsView(
            user = user1,
            uiState = ChatsViewModel.ChatsListUiState.Success(
                chats = emptyList()
            ),
            onTryAgainClick = {},
            onChatClick = {},
            showPermissionPermanentlyDeniedInfo = false,
            onDismissPermanentlyDeniedInfoClick = {},
            onGoToSettingsPermanentlyDeniedInfoClick = {},
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ChatsViewErrorPreview() {
    DroidChatTheme {
        ChatsView(
            user = user1,
            uiState = ChatsViewModel.ChatsListUiState.Error,
            onTryAgainClick = {},
            onChatClick = {},
            showPermissionPermanentlyDeniedInfo = false,
            onDismissPermanentlyDeniedInfoClick = {},
            onGoToSettingsPermanentlyDeniedInfoClick = {},
        )
    }
}