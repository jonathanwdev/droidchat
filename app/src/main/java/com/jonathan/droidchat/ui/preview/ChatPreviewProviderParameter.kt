package com.jonathan.droidchat.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.jonathan.droidchat.model.Chat
import com.jonathan.droidchat.model.User
import com.jonathan.droidchat.model.fake.chat1
import com.jonathan.droidchat.model.fake.chat2
import com.jonathan.droidchat.model.fake.chat3

class ChatPreviewProviderParameter : PreviewParameterProvider<Chat> {
    override val values: Sequence<Chat> = sequenceOf(
        chat1,
        chat2,
        chat3

    )
}

class ChatListPreviewProviderParameter : PreviewParameterProvider<List<Chat>> {
    override val values: Sequence<List<Chat>> = sequenceOf(
        listOf(
            chat1,
            chat2,
            chat3
        )

    )
}