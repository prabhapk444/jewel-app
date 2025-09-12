package com.example.sriramjewellers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.example.sriramjewellers.ui.theme.SriramJewellersTheme
import com.google.firebase.FirebaseApp
import com.example.sriramjewellers.ui.home.Home

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

    var currentScreen by remember { mutableStateOf("login") }
    var loggedInUsername by remember { mutableStateOf("") }  // store username

    when (currentScreen) {
        "register" -> RegisterScreen(onNavigateToLogin = { currentScreen = "login" })
        "login" -> LoginScreen(
            onNavigateToHome = { username ->
                loggedInUsername = username
                currentScreen = "home"
            },
            onNavigateToRegister = { currentScreen = "register" }
        )
        "home" -> Home(
            username = loggedInUsername,
            onLogout = {
                loggedInUsername = ""
                currentScreen = "login"
            },
            onViewMoreProducts = {
                // Navigate to full products screen, or leave empty for now
            }
        )

    }
}

