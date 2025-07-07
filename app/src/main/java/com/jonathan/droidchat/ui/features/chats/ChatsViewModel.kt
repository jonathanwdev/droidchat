package com.jonathan.droidchat.ui.features.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonathan.droidchat.data.repository.AuthRepository
import com.jonathan.droidchat.data.repository.ChatRepository
import com.jonathan.droidchat.model.Chat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val authRepository: AuthRepository,
): ViewModel() {
    private val _uiState = MutableStateFlow<ChatsListUiState>(ChatsListUiState.Loading)
    val uiState = _uiState
        .onStart {
            getChats()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ChatsListUiState.Loading
        )


    val currentUserFlow = authRepository.currentUserFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )

    init {
        viewModelScope.launch {
            chatRepository.newMessageReceivedFlow.collect() {
                getChats()
            }
        }
    }


    fun getChats(isRefresh: Boolean = false) {
        viewModelScope.launch {
            if(isRefresh) {
                _uiState.update {
                    ChatsListUiState.Loading
                }
            }

            chatRepository.getChats(0,10).fold(
                onSuccess = { chats ->
                    _uiState.update {
                        ChatsListUiState.Success(chats)
                    }
                },
                onFailure = {
                    _uiState.update {
                        ChatsListUiState.Error
                    }
                }
            )
        }
    }

    sealed interface ChatsListUiState {
        data object Loading : ChatsListUiState
        data class Success(val chats: List<Chat>) : ChatsListUiState
        data object Error : ChatsListUiState
    }
}