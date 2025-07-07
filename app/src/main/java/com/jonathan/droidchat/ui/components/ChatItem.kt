package com.jonathan.droidchat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.Visibility
import com.jonathan.droidchat.R
import com.jonathan.droidchat.model.Chat
import com.jonathan.droidchat.ui.preview.ChatPreviewProviderParameter
import com.jonathan.droidchat.ui.theme.DroidChatTheme

@Composable
fun ChatItem(
    chat: Chat,
    onClick: () -> Unit
) {
    val sender = remember(chat.members) {
        chat.otherMember
    }

    ConstraintLayout(
        modifier = Modifier
            .clickable {
                onClick()
            }
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)

    ) {
        val (avatarRef, firstNameRef, lastMessageRef, lastMessageTimeRef, unreadCountRef) = createRefs()
        RoundedAvatar(
            imageUri = sender.profilePictureUrl,
            contentDescription = null,
            placeholder = R.drawable.no_profile_image,
            modifier = Modifier
                .clip(CircleShape)
                .size(60.dp)
                .constrainAs(avatarRef) {
                    top.linkTo(parent.top, margin = 16.dp)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                }
        )
        Text(
            text = sender.firstName,
            modifier = Modifier
                .constrainAs(firstNameRef) {
                    top.linkTo(avatarRef.top)
                    start.linkTo(avatarRef.end, margin = 16.dp)
                    bottom.linkTo(lastMessageRef.top)
                    end.linkTo(lastMessageTimeRef.start, margin = 16.dp)
                    width = Dimension.fillToConstraints
                },
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = chat.lastMessage ?: "",
            modifier = Modifier
                .constrainAs(lastMessageRef) {
                    top.linkTo(firstNameRef.bottom)
                    start.linkTo(avatarRef.end, margin = 16.dp)
                    bottom.linkTo(avatarRef.bottom)
                    end.linkTo(unreadCountRef.start, margin = 16.dp)
                    width = Dimension.fillToConstraints
                },
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = chat.timestamp,
            modifier = Modifier
                .constrainAs(lastMessageTimeRef) {
                    top.linkTo(firstNameRef.top)
                    bottom.linkTo(unreadCountRef.top)
                    end.linkTo(parent.end)
                    width = Dimension.wrapContent
                },
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = chat.unreadCount.toString(),
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(horizontal = 4.dp)
                .constrainAs(unreadCountRef) {
                    top.linkTo(lastMessageTimeRef.bottom)
                    bottom.linkTo(lastMessageRef.bottom)
                    end.linkTo(parent.end)
                    width = Dimension.wrapContent
                    visibility = if (chat.unreadCount > 0) Visibility.Visible else Visibility.Gone
                },
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium
        )

    }
}


@Preview
@Composable
private fun ChatItemPreview(
    @PreviewParameter(ChatPreviewProviderParameter::class)
    chat: Chat
) {
    DroidChatTheme {
        ChatItem(
            chat = chat,
            onClick = {}
        )
    }
}