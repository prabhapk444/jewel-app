package com.example.sriramjewellers.ui.theme.components

import com.airbnb.lottie.compose.*
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sriramjewellers.R

@Composable
fun GlobalLoader() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loader))
    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        modifier = Modifier.size(150.dp)
    )
}
