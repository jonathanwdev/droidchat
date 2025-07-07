package com.jonathan.droidchat.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jonathan.droidchat.ui.theme.DroidChatTheme

@Composable
fun PrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(64.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isLoading) {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
            } else MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Box(
            modifier = Modifier.animateContentSize()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(30.dp)
                        .aspectRatio(1f),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeCap = StrokeCap.Round
                )
            } else {
                Text(
                    text = text,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

            }
        }
    }
}


@Preview
@Composable
private fun PrimaryButtonPreview() {
    DroidChatTheme {
        PrimaryButton(
            text = "SignIn",
            onClick = {}
        )
    }
}