package com.jonathan.droidchat.ui.features.chatdetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.LoadState
import androidx.paging.cachedIn
import com.jonathan.droidchat.data.network.ws.SocketMessageResult
import com.jonathan.droidchat.data.repository.ChatRepository
import com.jonathan.droidchat.data.repository.UserRepository
import com.jonathan.droidchat.model.User
import com.jonathan.droidchat.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class ChatDetailViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val chatDetailRoute = savedStateHandle.toRoute<Routes.ChatDetailRoute>()

    private var sendMessageFlow = MutableSharedFlow<Unit>()
    private val _isUserOnline = MutableStateFlow(false)
    val isUserOnline = _isUserOnline.asStateFlow()

    private val _getUserUiState = MutableStateFlow<GetUserUiState>(GetUserUiState.Loading)
    val getUserUiState = _getUserUiState
        .onStart {
            getUserDetail()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = GetUserUiState.Loading
        )

    var messageText by mutableStateOf("")
        private set

    val pagingChatMessages = chatRepository.getPagedMessages(
        receiverId = chatDetailRoute.userId
    ).cachedIn(viewModelScope)

    private val pagingChatMessagesState = MutableStateFlow<LoadState>(LoadState.Loading)

    private val _showError = Channel<Boolean>()
    val showError = _showError.receiveAsFlow()

    fun resetShowErrorState() {
        viewModelScope.launch {
            _showError.send(false)
        }
    }

    fun onChangeMessage(message: String) {
        messageText = message
    }

    private fun updateUserOnlineInfo(socketMessageResult: SocketMessageResult) {
        if (socketMessageResult is SocketMessageResult.ActiveUsersChanged) {
            _isUserOnline.update {
                socketMessageResult
                    .activeUsersIdsResponse
                    .activeUserIds
                    .contains(chatDetailRoute.userId)
            }
        }
    }

    fun onResume() {
        viewModelScope.launch {
            chatRepository.connectWebsocket().fold(
                onSuccess = {
                    chatRepository.observeSocketMessageResultFlow()
                        .onEach {
                            updateUserOnlineInfo(it)
                        }
                        .launchIn(viewModelScope)
                },
                onFailure = {

                }
            )
        }
    }

    fun onPause() {
        viewModelScope.launch {
            chatRepository.disconnectWebsocket()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            chatRepository.disconnectWebsocket()
        }
    }

    init {
        viewModelScope.launch {
            sendMessageFlow.mapLatest {
                sendMessage()
            }.collect()
        }
        viewModelScope.launch {
            combine(
                _getUserUiState,
                pagingChatMessagesState
            ) { getUserUiState, pagingChatMessagesState ->
                Pair(getUserUiState, pagingChatMessagesState)
            }.collect {
                val (getUserUiState, pagingChatMessagesState) = it
                if (getUserUiState is GetUserUiState.Error || pagingChatMessagesState is LoadState.Error) {
                    _showError.send(true)
                }
            }
        }
    }

    fun onSendMessage() {
        viewModelScope.launch {
            sendMessageFlow.emit(Unit)
        }
    }

    private fun getUserDetail() {
        viewModelScope.launch {
            _getUserUiState.update {
                GetUserUiState.Loading
            }
            userRepository.getUser(chatDetailRoute.userId).fold(
                onSuccess = { user ->
                    _getUserUiState.update {
                        GetUserUiState.Success(user)
                    }
                },
                onFailure = { err ->
                    _getUserUiState.update {
                        GetUserUiState.Error(err.message ?: "Unknown error")
                    }
                }
            )

        }
    }

    private suspend fun sendMessage() {
        if (messageText.isBlank()) return
        chatRepository.sendMessage(
            receiverId = chatDetailRoute.userId,
            message = messageText
        ).fold(
            onSuccess = {
                messageText = ""
            },
            onFailure = {
                //
            }
        )

    }

    fun setPagingChatMessagesLoadState(loadState: LoadState) {
        pagingChatMessagesState.update {
            loadState
        }
    }

    sealed interface GetUserUiState {
        data object Loading : GetUserUiState
        data class Success(val user: User) : GetUserUiState
        data class Error(val message: String) : GetUserUiState
    }

}