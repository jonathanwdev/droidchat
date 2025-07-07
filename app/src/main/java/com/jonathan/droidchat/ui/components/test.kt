package com.jonathan.droidchat.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MyView() {
    val scrollState = rememberScrollState()


    // Approach that can cause performance issues
    val showButtonWrong = remember(scrollState.value) {
        scrollState.value > 200
    }
    // 👆 This recalculates on every scroll change, even if the
    // result (true/false) doesn't change!



    // Optimized and Performant Approach
    val showButtonCorrect by remember {
        derivedStateOf {
            scrollState.value > 200
        }
    }
    // 👆 The block runs on every scroll change,
    // but recomposition only triggers when the result changes from
    // false to true (and vice-versa).


}