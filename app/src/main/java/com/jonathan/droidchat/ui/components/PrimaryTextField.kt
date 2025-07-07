package com.jonathan.droidchat.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jonathan.droidchat.R
import com.jonathan.droidchat.ui.extension.getVisualTransformationForPassword
import com.jonathan.droidchat.ui.theme.DroidChatTheme

@Composable
fun PrimaryTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes
    leadingIcon: Int? = null,
    errorMessage: String? = null,
    placeholder: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next
) {
    var passwordIsVisible by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            shape = CircleShape,
            singleLine = true,
            placeholder = {
                Text(placeholder)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedBorderColor = if (errorMessage != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
            ),
            leadingIcon = {
                leadingIcon?.let { icon ->
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            visualTransformation = keyboardType.getVisualTransformationForPassword(passwordIsVisible),
            trailingIcon = {
                if (keyboardType == KeyboardType.Password && value.isNotEmpty()) {
                    Icon(
                        painter = painterResource(if(passwordIsVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable {
                            passwordIsVisible = !passwordIsVisible
                        }
                    )
                }
            }
        )
        errorMessage?.let { error ->
            Spacer(Modifier.height(2.dp))
            Text(
                text = error,
                modifier = Modifier.padding(start = 16.dp),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelMedium
            )
        }

    }

}


@Preview(name = "Common")
@Composable
private fun PrimaryTextFieldPreview() {
    DroidChatTheme {
        PrimaryTextField(
            value = "123456",
            onValueChange = {},
            leadingIcon = R.drawable.ic_envelope
        )
    }
}
@Preview(name = "Password")
@Composable
private fun PrimaryTextFieldPassPreview() {
    DroidChatTheme {
        PrimaryTextField(
            value = "123456",
            keyboardType = KeyboardType.Password,
            onValueChange = {},
            leadingIcon = R.drawable.ic_envelope
        )
    }
}
@Preview(name = "Error")
@Composable
private fun PrimaryTextFieldErrPreview() {
    DroidChatTheme {
        PrimaryTextField(
            value = "123456",
            errorMessage = "Any error",
            onValueChange = {},
            leadingIcon = R.drawable.ic_envelope
        )
    }
}