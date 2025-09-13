package com.example.sriramjewellers.ui.home

import BannerCarousel
import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.widget.Toast
import androidx.annotation.RequiresApi


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Home(
    username: String,
    onLogout: () -> Unit,
    onViewMoreProducts: () -> Unit,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        bottomBar = {
            TabBar(selectedIndex = selectedTabIndex, onTabSelected = onTabSelected)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            TopBar(username,onLogout, cartItemCount = 0,
                onCartClick = { /* handle cart */ }

            )

            Spacer(modifier = Modifier.height(2.dp))

            BannerCarousel()

            Spacer(modifier = Modifier.height(16.dp))

            ProductsSection(
                limit = 3,
                onViewMore = onViewMoreProducts,
                onAddToCart = { product ->
                    Toast.makeText(context, "${product.name} added to cart", Toast.LENGTH_SHORT).show()
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

