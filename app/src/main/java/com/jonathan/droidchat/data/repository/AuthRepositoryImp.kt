package com.jonathan.droidchat.data.repository

import com.jonathan.droidchat.data.di.IoDispatcher
import com.jonathan.droidchat.data.manager.notification.NotificationManager
import com.jonathan.droidchat.data.manager.selfuser.SelfUserManager
import com.jonathan.droidchat.data.manager.token.TokenManager
import com.jonathan.droidchat.data.mapper.toDomainModel
import com.jonathan.droidchat.data.network.NetworkDataSource
import com.jonathan.droidchat.data.network.model.AuthRequest
import com.jonathan.droidchat.data.network.model.CreateAccountRequest
import com.jonathan.droidchat.model.CreateAccount
import com.jonathan.droidchat.model.Image
import com.jonathan.droidchat.model.RegisterTokenRequest
import com.jonathan.droidchat.model.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImp @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val tokenManager: TokenManager,
    private val selfUserManager: SelfUserManager,
    private val notificationManager: NotificationManager,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : AuthRepository {
    override val currentUserFlow: Flow<User>
        get() = selfUserManager.selfUserFlow.flowOn(ioDispatcher).map { it.toDomainModel() }

    override suspend fun getAccessToken(): String? {
        return tokenManager.accessToken.firstOrNull()
    }

    override suspend fun clearAccessToken() {
        withContext(ioDispatcher) {
            tokenManager.clearAccessToken()
        }
    }

    override suspend fun signUp(request: CreateAccount): Result<Unit> {
        return withContext(ioDispatcher) {
            runCatching {
                networkDataSource.signUp(
                    request = CreateAccountRequest(
                        firstName = request.firstName,
                        lastName = request.lastName,
                        username = request.username,
                        password = request.password,
                        profilePictureId = request.profilePictureId
                    )
                )
            }
        }
    }

    override suspend fun signIn(username: String, password: String): Result<Unit> {
        return withContext(ioDispatcher) {
            runCatching {
                val response = networkDataSource.signIn(
                    request = AuthRequest(
                        username = username,
                        password = password
                    )
                )
                tokenManager.saveAccessToken(response.token)
                authenticate().getOrThrow()
            }
        }
    }

    override suspend fun uploadProfilePicture(filePath: String): Result<Image> {
        return withContext(ioDispatcher) {
            runCatching {
                val response = networkDataSource.uploadProfilePicture(filePath)
                Image(
                    id = response.id,
                    name = response.name,
                    url = response.url,
                    type = response.type
                )
            }
        }
    }

    override suspend fun authenticate(): Result<Unit> {
        return withContext(ioDispatcher) {
            runCatching {
                val response = networkDataSource.authenticate()
                selfUserManager.saveSelfUserData(
                    firstName = response.firstName,
                    lastName = response.lastName,
                    profilePictureUrl = response.profilePictureUrl ?: "",
                    username = response.username,
                    id = response.id
                )
                val token = notificationManager.getToken()
                networkDataSource.registerNotificationToken(
                    registerTokenRequest = RegisterTokenRequest(
                        token = token
                    )
                )
            }

        }
    }
}