package com.jonathan.droidchat.ui.features.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleStartEffect
import com.jonathan.droidchat.R
import com.jonathan.droidchat.ui.components.AppDialog
import com.jonathan.droidchat.ui.theme.BackgroundGradient
import com.jonathan.droidchat.ui.theme.DroidChatTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    onNavigateToSignIn: () -> Unit,
    onNavigateToMain: () -> Unit,
    onCloseApp: () -> Unit,
) {
    SplashView()

    LifecycleStartEffect(Unit) {
        viewModel.checkSession()
        onStopOrDispose {}
    }

    LaunchedEffect(true) {
        viewModel.authenticationState.collect  { authenticationState ->
            when (authenticationState) {
                SplashViewModel.AuthenticationState.UserAuthenticated -> {
                    onNavigateToMain()
                }

                SplashViewModel.AuthenticationState.UserUnAuthenticated -> {
                    onNavigateToSignIn()
                }
            }
        }

    }

    if (viewModel.showErrorDialogState) {
        AppDialog(
            onDismissRequest = {},
            onConfirmButtonClick = {
                viewModel.disMissDialog()
                onCloseApp()
            },
            confirmButtonText = stringResource(R.string.error_confirm_button_close_app),
            text = stringResource(R.string.error_message_when_opening_app),
        )
    }

}


@Composable
private fun SplashView(modifier: Modifier = Modifier) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGradient)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(painter = painterResource(R.drawable.logo), contentDescription = null)
        Spacer(modifier = Modifier.height(77.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(painter = painterResource(R.drawable.ic_safety), contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.splash_safety_info),
                textAlign = TextAlign.Center,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun SplashScreenPreview() {
    DroidChatTheme {
        SplashView()
    }
}