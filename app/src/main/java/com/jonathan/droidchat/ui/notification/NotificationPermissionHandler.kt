package com.jonathan.droidchat.ui.notification

import android.os.Build
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat
import com.jonathan.droidchat.R
import com.jonathan.droidchat.ui.components.AppDialog

@Composable
fun NotificationPermissionHandler(
    onPermissionGranted: () -> Unit = {},
    onPermissionPermanentlyDenied: () -> Unit = {},
) {
    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return

    val notificationPermission = android.Manifest.permission.POST_NOTIFICATIONS
    val activity = LocalActivity.current

    var showPermissionRationaleDialog by remember {
        mutableStateOf(false)
    }
    var permissionRequested by rememberSaveable {
        mutableStateOf(false)
    }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        permissionRequested = true
        if(granted) {
            onPermissionGranted()
        } else {
            val shouldShowRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                activity!!,
                notificationPermission,
            )
            if(shouldShowRationale) {
                showPermissionRationaleDialog = true
            }else {
                onPermissionPermanentlyDenied()
            }

        }
    }

    LaunchedEffect(Unit) {
        if(!permissionRequested) {
            notificationPermissionLauncher.launch(notificationPermission)

        }
    }

    if(showPermissionRationaleDialog) {
        AppDialog(
            title = stringResource(R.string.notification_request_permission_title),
            text = stringResource(R.string.notification_request_permission_message),
            onDismissRequest = {
                showPermissionRationaleDialog = false
            },
            onConfirmButtonClick = {
                showPermissionRationaleDialog = false
            }
        )
    }
}