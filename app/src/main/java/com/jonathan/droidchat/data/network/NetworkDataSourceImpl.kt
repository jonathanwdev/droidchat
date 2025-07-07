package com.jonathan.droidchat.data.network


import com.jonathan.droidchat.data.network.model.AuthRequest
import com.jonathan.droidchat.data.network.model.CreateAccountRequest
import com.jonathan.droidchat.data.network.model.ImageResponse
import com.jonathan.droidchat.data.network.model.PaginatedChatResponse
import com.jonathan.droidchat.data.network.model.PaginatedMessageResponse
import com.jonathan.droidchat.data.network.model.PaginatedUserResponse
import com.jonathan.droidchat.data.network.model.PaginationParams
import com.jonathan.droidchat.data.network.model.TokenResponse
import com.jonathan.droidchat.data.network.model.UserResponse
import com.jonathan.droidchat.model.RegisterTokenRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.plugin
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.URLBuilder
import java.io.File
import javax.inject.Inject

class NetworkDataSourceImpl @Inject constructor(
    private val httpClient: HttpClient,
) : NetworkDataSource {
    override suspend fun signUp(request: CreateAccountRequest) {
        httpClient.post("signup") {
            setBody(request)
        }.body<Unit>()
    }

    override suspend fun signIn(request: AuthRequest): TokenResponse {
        return httpClient.post("signin") {
            setBody(request)
        }.body<TokenResponse>().also {
            httpClient.plugin(Auth)
                .providers
                .filterIsInstance<BearerAuthProvider>()
                .first()
                .clearToken()
        }
    }

    override suspend fun uploadProfilePicture(filePath: String): ImageResponse {
        val file = File(filePath)
        return httpClient.submitFormWithBinaryData(
            url = "profile-picture",
            formData = formData {
                append("image", file.readBytes(), Headers.build {
                    append(HttpHeaders.ContentType, "image/${file.extension}")
                    append(HttpHeaders.ContentDisposition, "filename=${file.name}")
                })
            }
        ).body()

    }

    override suspend fun authenticate(): UserResponse {
        return httpClient.get("authenticate").body()
    }

    override suspend fun registerNotificationToken(registerTokenRequest: RegisterTokenRequest) {
        httpClient.post("notifications/register") {
            setBody(registerTokenRequest)
        }.body<Unit>()
    }

    override suspend fun getChats(
        paginationParams: PaginationParams
    ): PaginatedChatResponse {
        return httpClient.get("conversations") {
            url {
                appendPaginationParams(paginationParams)
            }
        }.body()
    }

    override suspend fun getUsers(
        paginationParams: PaginationParams
    ): PaginatedUserResponse {
        return httpClient.get("users") {
            url {
                appendPaginationParams(paginationParams)
            }
        }.body()
    }

    override suspend fun getUser(userId: Int): UserResponse {
        return httpClient.get("users/$userId").body()
    }

    override suspend fun getMessages(
        receiverId: Int,
        paginationParams: PaginationParams
    ): PaginatedMessageResponse {
        return httpClient.get("messages/$receiverId") {
            url {
                appendPaginationParams(paginationParams)
            }
        }.body()
    }

    private fun URLBuilder.appendPaginationParams(paginationParams: PaginationParams) {
        parameters.append("offset", paginationParams.offset)
        parameters.append("limit", paginationParams.limit)
    }
}