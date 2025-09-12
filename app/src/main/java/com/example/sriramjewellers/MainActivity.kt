package com.example.sriramjewellers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sriramjewellers.ui.theme.SriramJewellersTheme


import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        FirebaseApp.initializeApp(this)

        enableEdgeToEdge()

        setContent {
            var currentScreen by remember { mutableStateOf("register") }

            when(currentScreen) {
                "register" -> RegisterScreen(onNavigateToLogin = { currentScreen = "login" })
                "login" -> LoginScreen(
                    onNavigateToHome = { currentScreen = "home" },
                    onNavigateToRegister = { currentScreen = "register" }
                )
                "home" -> HomeScreen()
            }
        }

    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SriramJewellersTheme {
        Greeting("Android")
    }
}
