package com.example.sriramjewellers.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.widget.Toast

@Composable
fun Home(
    username: String,
    onLogout: () -> Unit,
    onViewMoreProducts: () -> Unit
) {
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize()) {

        TopBar(
            username = username, onLogout = onLogout,
            cartItemCount = TODO(),
            onCartClick = TODO(),
            onProfileClick = TODO()
        )

        Spacer(modifier = Modifier.height(8.dp))

        BannerCarousel()

        Spacer(modifier = Modifier.height(16.dp))

        CategoriesSection()

        Spacer(modifier = Modifier.height(16.dp))

        // Pass onAddToCart lambda here
        ProductsSection(
            limit = 3,
            onViewMore = onViewMoreProducts,
            onAddToCart = { product ->
                // Handle adding product to cart
                Toast.makeText(context, "${product.name} added to cart", Toast.LENGTH_SHORT).show()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TabBar()
    }
}
