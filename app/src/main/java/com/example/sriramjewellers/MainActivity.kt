package com.example.sriramjewellers

import com.example.sriramjewellers.ui.theme.components.GlobalLoader
import AboutScreen
import FundsScreen
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.sriramjewellers.ui.theme.SriramJewellersTheme
import com.google.firebase.FirebaseApp
import com.example.sriramjewellers.ui.home.Home
import androidx.activity.compose.BackHandler
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton



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
    var selectedTabIndex by remember { mutableStateOf(0) }
    var isLoading by remember { mutableStateOf(false) }


    var showLogoutDialog by remember { mutableStateOf(false) }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            GlobalLoader()
        }
    } else {
        if (currentScreen == "home") {
            BackHandler {
                if (selectedTabIndex != 0) {

                    selectedTabIndex = 0
                } else {

                    showLogoutDialog = true
                }
            }
        }


        if (showLogoutDialog) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = {
                    Text(text = "Confirm Logout")
                },
                text = {
                    Text(text = "Are you sure you want to logout?")
                },
                confirmButton = {
                    TextButton(onClick = {
                        loggedInUsername = ""
                        currentScreen = "login"
                        showLogoutDialog = false
                    }) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showLogoutDialog = false
                    }) {
                        Text("No")
                    }
                }
            )
        }

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
                1 -> AboutScreen(
                    username = loggedInUsername,
                    onLogout = {
                        loggedInUsername = ""
                        currentScreen = "login"
                    },
                    selectedTabIndex = selectedTabIndex,
                    onTabSelected = { index -> selectedTabIndex = index }
                )
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
                3 -> FundsScreen(
                    username = loggedInUsername,
                    onBack = { selectedTabIndex = 0 },
                    onLogout = {
                        loggedInUsername = ""
                        currentScreen = "login"
                    },
                    selectedTabIndex = selectedTabIndex,
                    onTabSelected = { index -> selectedTabIndex = index }
                )
            }
        }
    }
}

