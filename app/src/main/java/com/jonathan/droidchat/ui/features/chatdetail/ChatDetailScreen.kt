package com.jonathan.droidchat.ui.features.chatdetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.jonathan.droidchat.R
import com.jonathan.droidchat.model.ChatMessage
import com.jonathan.droidchat.model.fake.chatMessage1
import com.jonathan.droidchat.model.fake.chatMessage2
import com.jonathan.droidchat.model.fake.chatMessage3
import com.jonathan.droidchat.model.fake.chatMessage4
import com.jonathan.droidchat.model.fake.chatMessage5
import com.jonathan.droidchat.model.fake.user2
import com.jonathan.droidchat.ui.components.AnimatedContent
import com.jonathan.droidchat.ui.components.AppDialog
import com.jonathan.droidchat.ui.components.ChatMessageBubble
import com.jonathan.droidchat.ui.components.ChatMessageTextField
import com.jonathan.droidchat.ui.components.ChatScaffold
import com.jonathan.droidchat.ui.components.ChatTopAppBar
import com.jonathan.droidchat.ui.components.GeneralEmptyList
import com.jonathan.droidchat.ui.components.GeneralError
import com.jonathan.droidchat.ui.components.PrimaryButton
import com.jonathan.droidchat.ui.components.RoundedAvatar
import com.jonathan.droidchat.ui.theme.DroidChatTheme
import kotlinx.coroutines.flow.flowOf

@Composable
fun ChatDetailScreen(
    onNavigateBack: () -> Unit,
    viewModel: ChatDetailViewModel = hiltViewModel()
) {
    val isUserOnline by viewModel.isUserOnline.collectAsStateWithLifecycle()
    val pagingChatMessages = viewModel.pagingChatMessages.collectAsLazyPagingItems()
    val messageText = viewModel.messageText
    val getUserUiState by viewModel.getUserUiState.collectAsStateWithLifecycle()
    var showErrorDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(pagingChatMessages.loadState.refresh) {
        viewModel.setPagingChatMessagesLoadState(pagingChatMessages.loadState.refresh)
    }

    LaunchedEffect(Unit) {
        viewModel.showError.collect {
            showErrorDialog = it
        }
    }

    LifecycleResumeEffect(Unit) {
        viewModel.onResume()
        onPauseOrDispose {
            viewModel.onPause()
        }
    }






    if(showErrorDialog) {
        AppDialog(
            onDismissRequest = {
                viewModel.resetShowErrorState()
                onNavigateBack()
            },
            onConfirmButtonClick = {
                viewModel.resetShowErrorState()
                onNavigateBack()
            },
            text = stringResource(R.string.common_generic_error_message)
        )
    }

    ChatDetailView(
        pagingChatMessages = pagingChatMessages,
        messageText = messageText,
        getUserUiState = getUserUiState,
        isUserOnline = isUserOnline,
        onNavigationIconClick = onNavigateBack,
        onMessageChange = viewModel::onChangeMessage,
        onSendClick = viewModel::onSendMessage,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatDetailView(
    pagingChatMessages: LazyPagingItems<ChatMessage>,
    messageText: String,
    getUserUiState: ChatDetailViewModel.GetUserUiState,
    isUserOnline: Boolean,
    onNavigationIconClick: () -> Unit,
    onMessageChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    ChatScaffold(
        topBar = {
            ChatTopAppBar(
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            onNavigationIconClick()
                        },
                        tint = MaterialTheme.colorScheme.inverseSurface
                    )
                },
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        when(getUserUiState) {
                            is ChatDetailViewModel.GetUserUiState.Success -> {
                                RoundedAvatar(
                                    imageUri = getUserUiState.user.profilePictureUrl,
                                    contentDescription = null,
                                    size = 42.dp
                                )
                                Column(
                                    modifier = Modifier.padding(horizontal = 15.dp)
                                ) {
                                    Text(
                                        text = getUserUiState.user.firstName,
                                        style = MaterialTheme.typography.titleMedium,
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = 1,
                                        color = MaterialTheme.colorScheme.inverseOnSurface
                                    )
                                    if(isUserOnline) {
                                        Text(
                                            text = stringResource(R.string.user_online),
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.inverseOnSurface
                                        )
                                    }
                                }
                            }
                            ChatDetailViewModel.GetUserUiState.Loading -> {
                                CircularProgressIndicator(
                                    color = MaterialTheme.colorScheme.inverseOnSurface
                                )
                            }
                            is ChatDetailViewModel.GetUserUiState.Error -> {}
                        }
                    }
                }
            )
        }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                when (pagingChatMessages.loadState.refresh) {
                    LoadState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.Center)
                        )
                    }

                    is LoadState.NotLoading -> {
                        if (pagingChatMessages.itemCount == 0) {
                            GeneralEmptyList(
                                message = stringResource(R.string.feature_chat_detail_empty_list),
                                resource = {
                                    AnimatedContent(
                                        resId = R.raw.animation_empty_list
                                    )
                                }
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentPadding = PaddingValues(16.dp),
                                reverseLayout = true,
                                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Bottom)
                            ) {
                                items(pagingChatMessages.itemCount) { index ->
                                    val chatMessage = pagingChatMessages[index]
                                    val previousChatMessage = if (index > 0) {
                                        pagingChatMessages[index - 1]
                                    } else null

                                    if (chatMessage != null) {
                                        ChatMessageBubble(
                                            chatMessage = chatMessage,
                                            previousChatMessage = previousChatMessage
                                        )
                                    }
                                }

                                if (pagingChatMessages.loadState.append is LoadState.Loading) {
                                    item {
                                        Box(
                                            modifier = Modifier
                                                .padding(16.dp)
                                                .fillMaxWidth(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator()
                                        }
                                    }
                                }

                                if (pagingChatMessages.loadState.append is LoadState.Error) {
                                    item {
                                        Column(
                                            modifier = Modifier
                                                .padding(16.dp)
                                                .fillMaxWidth(),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                text = stringResource(R.string.feature_chat_detail_error_loading_more),
                                                modifier = Modifier
                                                    .padding(vertical = 8.dp),
                                                color = MaterialTheme.colorScheme.error
                                            )

                                            PrimaryButton(
                                                text = stringResource(R.string.common_retry),
                                                onClick = {
                                                    pagingChatMessages.retry()
                                                },
                                                modifier = Modifier
                                                    .padding(horizontal = 30.dp)
                                                    .height(46.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    is LoadState.Error -> {
                        GeneralError(
                            title = stringResource(R.string.common_generic_error_title),
                            message = stringResource(R.string.common_generic_error_message),
                            resource = {
                                AnimatedContent()
                            },
                            action = {
                                PrimaryButton(
                                    text = stringResource(R.string.common_retry),
                                    onClick = {
                                        pagingChatMessages.refresh()
                                    }
                                )
                            }
                        )
                    }
                }
            }

            ChatMessageTextField(
                value = messageText,
                onInputChange = onMessageChange,
                onSendClicked = onSendClick,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp, top = 8.dp),
                placeholder = stringResource(R.string.feature_chat_detail_text_field_placeholder)
            )
        }
    }

}

@Preview(showSystemUi = true)
@Composable
private fun ChatDetailViewPreview() {
    val pagingChatMessages = flowOf(
        PagingData.from(
            listOf(
                chatMessage5,
                chatMessage4,
                chatMessage3,
                chatMessage2,
                chatMessage1,
            ),
            sourceLoadStates = LoadStates(
                refresh = LoadState.NotLoading(true),
                prepend = LoadState.NotLoading(false),
                append = LoadState.NotLoading(false)
            ),

            )
    ).collectAsLazyPagingItems()

    DroidChatTheme {
        ChatDetailView(
            pagingChatMessages = pagingChatMessages,
            messageText = "",
            isUserOnline = true,
            onNavigationIconClick = {},
            onMessageChange = {},
            getUserUiState = ChatDetailViewModel.GetUserUiState.Success(user2),
            onSendClick = {}

        )
    }
}