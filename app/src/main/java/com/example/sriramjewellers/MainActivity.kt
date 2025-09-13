package com.example.sriramjewellers

import AboutScreen
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import com.example.sriramjewellers.ui.theme.SriramJewellersTheme
import com.google.firebase.FirebaseApp
import com.example.sriramjewellers.ui.home.Home
import com.example.sriramjewellers.ProductScreen

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppContent() {
    var currentScreen by remember { mutableStateOf("login") }
    var loggedInUsername by remember { mutableStateOf("") }
    var selectedTabIndex by remember { mutableStateOf(0) } // Add tab state here

    when (currentScreen) {
        "register" -> RegisterScreen(onNavigateToLogin = { currentScreen = "login" })

        "login" -> LoginScreen(
            onNavigateToHome = { username ->
                loggedInUsername = username
                currentScreen = "home"
            },
            onNavigateToRegister = { currentScreen = "register" }
        )

        "home" -> when (selectedTabIndex) {
            0 -> Home(
                username = loggedInUsername,
                onLogout = {
                    loggedInUsername = ""
                    currentScreen = "login"
                },
                onViewMoreProducts = {
                    selectedTabIndex = 2
                },
                selectedTabIndex = selectedTabIndex,
                onTabSelected = { index -> selectedTabIndex = index }
            )
            1 -> AboutScreen( username = loggedInUsername,
                onLogout = {
                    loggedInUsername = ""
                    currentScreen = "login"
                },
                selectedTabIndex = selectedTabIndex,
                onTabSelected = { index -> selectedTabIndex = index })
            2 -> ProductScreen(
                username = loggedInUsername,
                onBack = { selectedTabIndex = 0 },
                onLogout = {
                    loggedInUsername = ""
                    currentScreen = "login"
                },
                selectedTabIndex = selectedTabIndex,
                onTabSelected = { index -> selectedTabIndex = index }
            )
            3 -> FundsScreen()
        }
    }
}
