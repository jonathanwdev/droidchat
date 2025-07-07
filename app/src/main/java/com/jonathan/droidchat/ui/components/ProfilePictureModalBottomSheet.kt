package com.jonathan.droidchat.ui.components

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.jonathan.droidchat.DroidChatFileProvider
import com.jonathan.droidchat.R
import com.jonathan.droidchat.ui.theme.DroidChatTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePictureModalBottomSheet(
    onPictureSelected: (uri: Uri) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    sheetState: SheetState = rememberModalBottomSheetState(),
) {
    var photoUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { picture ->
            picture?.let {
                onPictureSelected(picture)
            }
        }
    )
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { isSuccess ->
            if (isSuccess && photoUri != null) {
                onPictureSelected(photoUri!!)
            }
        }
    )

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        modifier = modifier,
        contentColor = MaterialTheme.colorScheme.onSurface,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        ProfilePictureModalItem(
            label = R.string.common_take_photo,
            icon = R.drawable.ic_photo_camera,
            onClickItem = {
                photoUri = DroidChatFileProvider.getImageUri(context.applicationContext)
                cameraLauncher.launch(photoUri!!)
            }
        )
        ProfilePictureModalItem(
            label = R.string.common_upload_photo,
            icon = R.drawable.ic_photo_library,
            onClickItem = {
                imagePicker.launch("image/*")
            }
        )

    }
}

@Composable
private fun ProfilePictureModalItem(
    @StringRes
    label: Int,
    @DrawableRes
    icon: Int,
    onClickItem: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp)
            .clickable {
                onClickItem()
            }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(id = label),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.labelLarge
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun ProfilePictureModalBottomSheetPreview() {
    DroidChatTheme {
        ProfilePictureModalBottomSheet(
            onPictureSelected = {},
            onDismissRequest = {},
            sheetState = SheetState(
                skipPartiallyExpanded = false,
                density = Density(LocalContext.current),
                initialValue = SheetValue.Expanded
            )
        )
    }
}