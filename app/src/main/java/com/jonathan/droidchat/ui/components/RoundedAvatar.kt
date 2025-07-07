package com.jonathan.droidchat.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jonathan.droidchat.R

@Composable

fun RoundedAvatar(
    modifier: Modifier = Modifier,
    imageUri: Any?,
    contentDescription: String?,
    size: Dp = 60.dp,
    placeholder: Int = R.drawable.no_profile_image
) {
    AsyncImage(
        model = imageUri,
        placeholder = painterResource(id =placeholder),
        error = painterResource(id = placeholder),
        fallback = painterResource(id = placeholder),
        contentDescription = contentDescription,
        modifier = modifier
            .clip(CircleShape)
            .size(size)

    )
}


@Preview
@Composable
private fun RoundedAvatarPreview() {
    RoundedAvatar(
        imageUri = null,
        contentDescription = null

    )
}