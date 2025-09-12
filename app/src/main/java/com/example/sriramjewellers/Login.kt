package com.example.sriramjewellers

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore
import com.example.sriramjewellers.hashPassword
import com.example.sriramjewellers.ui.theme.*

@Composable
fun LoginScreen(onNavigateToHome: () -> Unit, onNavigateToRegister: () -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Animation state for visibility
    var showContent by remember { mutableStateOf(false) }
    val alphaAnim by animateFloatAsState(targetValue = if (showContent) 1f else 0f)

    LaunchedEffect(Unit) {
        showContent = true
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(visible = showContent, enter = fadeIn()) {
                Text(
                    text = "Login to Continue",
                    fontSize = 28.sp,
                    color = HeadlineColor,
                    modifier = Modifier.padding(bottom = 28.dp)
                )
            }

            // Username Field
            CustomBasicTextField(
                value = username,
                onValueChange = { username = it },
                placeholder = "Username"
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Password Field
            PasswordBasicTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = "Password",
                passwordVisible = passwordVisible,
                onVisibilityChange = { passwordVisible = it }
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Login Button
            Button(
                onClick = {
                    if (username.isBlank() || password.isBlank()) {
                        Toast.makeText(context, "Enter all fields", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    val hashedPassword = hashPassword(password)

                    db.collection("users").document(username).get()
                        .addOnSuccessListener { doc ->
                            if (doc.exists() && doc.getString("password") == hashedPassword) {
                                Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()
                                onNavigateToHome()
                            } else {
                                Toast.makeText(context, "User not found. Please register.", Toast.LENGTH_SHORT).show()
                                onNavigateToRegister()
                            }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ButtonColor)
            ) {
                Text(text = "Login", color = ButtonTextColor, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Register redirect text
            val annotatedText = buildAnnotatedString {
                append("Don't have an account? ")
                pushStringAnnotation(tag = "register", annotation = "register")
                withStyle(style = SpanStyle(color = ButtonColor, textDecoration = TextDecoration.Underline)) {
                    append("Register")
                }
                pop()
            }

            ClickableText(
                text = annotatedText,
                onClick = { offset ->
                    annotatedText.getStringAnnotations(tag = "register", start = offset, end = offset)
                        .firstOrNull()?.let {
                            onNavigateToRegister()
                        }
                },
                style = TextStyle(color = ParagraphColor, fontSize = 14.sp)
            )
        }
    }
}
