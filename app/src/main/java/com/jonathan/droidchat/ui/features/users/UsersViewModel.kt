package com.jonathan.droidchat.ui.features.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.jonathan.droidchat.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {
    val usersFlow = userRepository.getUsers().cachedIn(viewModelScope)

}