package com.example.sriramjewellers

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.example.sriramjewellers.ui.theme.*
import com.example.sriramjewellers.ui.theme.components.GlobalLoader

class SplashScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SriramJewellersTheme {
                SplashContent {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        }
    }
}

@Composable
fun SplashContent(onTimeout: () -> Unit) {

    var startAnimation by remember { mutableStateOf(false) }


    val logoScale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.3f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "logoScale"
    )

    val logoAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ),
        label = "logoAlpha"
    )


    val textAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 800,
            delayMillis = 500,
            easing = FastOutSlowInEasing
        ),
        label = "textAlpha"
    )

    val textSlideUp by animateFloatAsState(
        targetValue = if (startAnimation) 0f else 50f,
        animationSpec = tween(
            durationMillis = 800,
            delayMillis = 500,
            easing = FastOutSlowInEasing
        ),
        label = "textSlideUp"
    )


    val loaderAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 600,
            delayMillis = 1200,
            easing = FastOutSlowInEasing
        ),
        label = "loaderAlpha"
    )


    val taglineAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 600,
            delayMillis = 1500,
            easing = FastOutSlowInEasing
        ),
        label = "taglineAlpha"
    )


    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(4500)
        onTimeout()
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        BackgroundColor,
                        BackgroundColor.copy(alpha = 0.95f),

                    ),
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {


            Card(
                modifier = Modifier
                    .size(120.dp)
                    .scale(logoScale)
                    .alpha(logoAlpha),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 12.dp
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    ButtonColor.copy(alpha = 0.1f),
                                    Color.Transparent
                                ),
                                radius = 100f
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "💎",
                        fontSize = 48.sp,
                        color = MaterialTheme.colorScheme.buttonColorCompat
                    )


                }
            }

            Spacer(modifier = Modifier.height(32.dp))


            Text(
                text = "Sriram Jewellers",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.headlineColorCompat,
                textAlign = TextAlign.Center,
                letterSpacing = 1.2.sp,
                modifier = Modifier
                    .alpha(textAlpha)
                    .offset(y = textSlideUp.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))


            Text(
                text = "Crafting Timeless Elegance",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.paragraphColorCompat.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                letterSpacing = 0.8.sp,
                modifier = Modifier.alpha(taglineAlpha)
            )

            Spacer(modifier = Modifier.height(48.dp))


            Box(
                modifier = Modifier.alpha(loaderAlpha)
            ) {
                GlobalLoader()
            }

            Spacer(modifier = Modifier.height(16.dp))


            Text(
                text = "Loading your premium experience...",
                fontSize = 12.sp,
                color =  MaterialTheme.colorScheme.paragraphColorCompat.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.alpha(loaderAlpha)
            )
        }

    }
}