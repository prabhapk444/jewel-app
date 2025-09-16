package com.example.sriramjewellers

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Lock
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore
import com.example.sriramjewellers.hashPassword
import com.example.sriramjewellers.ui.theme.*
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
@Composable
fun LoginScreen(
    onNavigateToHome: (String) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Dialog state
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    // Animation state
    var showContent by remember { mutableStateOf(false) }
    val alphaAnim by animateFloatAsState(targetValue = if (showContent) 1f else 0f)

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
                Text(text = "Login", color = Color.White, fontSize = 16.sp)
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
