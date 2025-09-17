package com.example.sriramjewellers

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore
import com.example.sriramjewellers.ui.theme.*
import coil.compose.rememberAsyncImagePainter

@Composable
fun LoginScreen(
    onNavigateToHome: (String) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val db = FirebaseFirestore.getInstance()


    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }


    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    var showContent by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) { showContent = true }

    Scaffold(containerColor = BackgroundColor) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AnimatedVisibility(visible = showContent) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Login to Continue",
                        fontSize = 28.sp,
                        color = HeadlineColor,
                        modifier = Modifier.padding(bottom = 28.dp)
                    )
                    Image(
                        painter = rememberAsyncImagePainter("file:///android_asset/login.gif"),
                        contentDescription = "Login Logo",
                        modifier = Modifier.size(230.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            CustomBasicTextField(
                value = username,
                onValueChange = { username = it },
                placeholder = "Username"
            )
            Spacer(modifier = Modifier.height(12.dp))

            PasswordBasicTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = "Password",
                passwordVisible = passwordVisible,
                onVisibilityChange = { passwordVisible = it }
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    when {
                        username.isBlank() || password.isBlank() -> {
                            dialogMessage = "Enter all fields"
                            showDialog = true
                        }
                        else -> {
                            val hashedPassword = hashPassword(password)
                            db.collection("users").document(username).get()
                                .addOnSuccessListener { doc ->
                                    if (doc.exists() && doc.getString("password") == hashedPassword) {
                                        dialogMessage = "Login Successful!"
                                        showDialog = true
                                        onNavigateToHome(username)
                                    } else {
                                        dialogMessage = "User not found. Please register."
                                        showDialog = true
                                        onNavigateToRegister()
                                    }
                                }
                                .addOnFailureListener { e ->
                                    dialogMessage = "Error: ${e.message}"
                                    showDialog = true
                                }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ButtonColor)
            ) {
                Text(text = "Login", color = ButtonTextColor
                    , fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            val annotatedText = buildAnnotatedString {
                append("Don't have an account? ")
                pushStringAnnotation(tag = "register", annotation = "register")
                withStyle(
                    style = SpanStyle(
                        color = ParagraphColor,
                        textDecoration = TextDecoration.Underline
                    )
                ) {
                    append("Register")
                }
                pop()
            }

            ClickableText(
                text = annotatedText,
                onClick = { offset ->
                    annotatedText.getStringAnnotations(tag = "register", start = offset, end = offset)
                        .firstOrNull()?.let { onNavigateToRegister() }
                },
                style = TextStyle(color = ParagraphColor, fontSize = 14.sp)
            )
        }
    }


    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("OK")
                }
            },
            title = { Text("Message") },
            text = { Text(dialogMessage) }
        )
    }
}
