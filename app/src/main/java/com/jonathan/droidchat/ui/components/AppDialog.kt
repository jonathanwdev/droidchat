package com.jonathan.droidchat.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jonathan.droidchat.R

@Composable
fun AppDialog(
    modifier: Modifier = Modifier,
    confirmButtonText: String = stringResource(R.string.common_ok),
    title: String? = null,
    text: String,
    onDismissRequest: () -> Unit,
    onConfirmButtonClick: () -> Unit,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = onConfirmButtonClick
            ) {
                Text(text = confirmButtonText)
            }
        },
        title = {
            title?.let {
                Text(text = it)
            }
        },
        text = {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        textContentColor = MaterialTheme.colorScheme.onSurface,
        tonalElevation = 2.dp

    )
}
