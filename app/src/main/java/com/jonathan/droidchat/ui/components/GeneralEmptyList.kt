package com.jonathan.droidchat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jonathan.droidchat.ui.theme.DroidChatTheme

@Composable
fun GeneralEmptyList(
    message: String,
    modifier: Modifier = Modifier,
    resource: (@Composable () -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        resource?.let { res ->
            Box(
                modifier = Modifier.sizeIn(
                    maxWidth = 200.dp,
                    maxHeight = 200.dp
                )
            ) {
                res()
            }
            Spacer(Modifier.height(32.dp))

        }
        Spacer(Modifier.height(32.dp))
        Text(
            text = message,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge
        )

    }

}


@Preview
@Composable
private fun GeneralEmptyListPreview() {
    DroidChatTheme {
        GeneralEmptyList(
            message = "Ops ! Lista vazia"
        )
    }
}