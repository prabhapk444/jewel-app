package com.example.sriramjewellers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.example.sriramjewellers.ui.theme.SriramJewellersTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()

        setContent {
            SriramJewellersTheme {
                AppContent()
            }
        }
    }
}

@Composable
fun AppContent() {

    var currentScreen by remember { mutableStateOf("register") }

    when (currentScreen) {
        "register" -> RegisterScreen(onNavigateToLogin = { currentScreen = "login" })
        "login" -> LoginScreen(
            onNavigateToHome = { currentScreen = "home" },
            onNavigateToRegister = { currentScreen = "register" }
        )
        "home" -> Home()
    }
}
