package com.jonathan.droidchat.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jonathan.droidchat.R
import com.jonathan.droidchat.ui.extension.bottomBorder
import com.jonathan.droidchat.ui.extension.getVisualTransformationForPassword
import com.jonathan.droidchat.ui.theme.ColorSuccess
import com.jonathan.droidchat.ui.theme.DroidChatTheme

@Composable
fun SecondaryTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    extraText: String? = null,
    errorText: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next
) {
    var passwordIsVisible by remember {
        mutableStateOf(false)
    }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        maxLines = 1,
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = .7f),
            fontWeight = FontWeight.Bold
        ),
        keyboardOptions = KeyboardOptions(
            capitalization = if (keyboardType == KeyboardType.Text) {
                KeyboardCapitalization.Sentences
            } else KeyboardCapitalization.None,
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        visualTransformation = keyboardType.getVisualTransformationForPassword(passwordIsVisible),

        ) { innerTextField ->
        Surface(
            color = MaterialTheme.colorScheme.surface
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.bottomBorder(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        strokeWidth = 1.dp
                    )
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().weight(1f)
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Box(
                                modifier = Modifier.weight(1f)
                            ) {
                                innerTextField()
                            }
                            extraText?.let {
                                Text(
                                    text = extraText,
                                    modifier = Modifier.padding(4.dp),
                                    color = ColorSuccess,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    if (keyboardType == KeyboardType.Password && value.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                passwordIsVisible = !passwordIsVisible
                            }
                        ) {
                            Icon(
                                painter = painterResource(
                                    if (passwordIsVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                                ),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
                errorText?.let {
                    Text(
                        text = errorText,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun SecondaryTextFieldEmailPreview() {
    DroidChatTheme {
        SecondaryTextField(
            label = "E-mail",
            value = "mail@mail.com",
            onValueChange = {},
            keyboardType = KeyboardType.Email
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SecondaryTextFieldPassPreview() {
    DroidChatTheme {
        SecondaryTextField(
            label = "Password",
            value = "Value",
            onValueChange = {},
            extraText = "Password does not match",
            keyboardType = KeyboardType.Password
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SecondaryTextFieldErrorPreview() {
    DroidChatTheme {
        SecondaryTextField(
            label = "Password",
            value = "",
            onValueChange = {},
            errorText = "Password does not match",
            keyboardType = KeyboardType.Password
        )
    }
}