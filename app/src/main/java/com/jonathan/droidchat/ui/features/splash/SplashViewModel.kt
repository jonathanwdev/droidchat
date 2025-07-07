package com.jonathan.droidchat.ui.features.splash

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonathan.droidchat.data.repository.AuthRepository
import com.jonathan.droidchat.model.NetworkException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {
    private val _authenticationState = MutableSharedFlow<AuthenticationState>(replay = 1)
    val authenticationState = _authenticationState.asSharedFlow()

    var showErrorDialogState by mutableStateOf(false)
        private set

    fun checkSession() {
        disMissDialog()
        viewModelScope.launch {
            val accessToken = authRepository.getAccessToken()
            if(accessToken.isNullOrBlank()) {
                _authenticationState.emit(AuthenticationState.UserUnAuthenticated)
                return@launch
            }
            authRepository.authenticate().fold(
                onSuccess = {
                    _authenticationState.emit(AuthenticationState.UserAuthenticated)
                },
                onFailure = {
                    if(it is NetworkException.ApiException && it.statusCode == 401) {
                        authRepository.clearAccessToken()
                        _authenticationState.emit(AuthenticationState.UserUnAuthenticated)
                    }else {
                        showErrorDialogState = true
                    }
                }
            )
        }
    }

    fun disMissDialog() {
        showErrorDialogState = false

    }

    sealed interface AuthenticationState {
        data object UserAuthenticated : AuthenticationState
        data object UserUnAuthenticated : AuthenticationState

    }
}