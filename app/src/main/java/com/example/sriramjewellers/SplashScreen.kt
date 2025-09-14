package com.example.sriramjewellers

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.compose.ui.graphics.Color
import com.example.sriramjewellers.ui.theme.components.GlobalLoader

class SplashScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SplashContent {

                startActivity(Intent(this, MainActivity::class.java))
                finish() // Close SplashScreen
            }
        }
    }
}

@Composable
fun SplashContent(onTimeout: () -> Unit) {

    LaunchedEffect(key1 = true) {
        delay(4000)
        onTimeout()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            GlobalLoader()


            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Sriram Jewellers",
                fontSize = 24.sp,
                color = Color(0xFFFFB800)

            )

        }
    }
}
