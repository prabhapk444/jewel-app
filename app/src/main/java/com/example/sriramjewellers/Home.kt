package com.example.sriramjewellers.ui.home

import android.os.Build
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
import androidx.annotation.RequiresApi
import com.example.sriramjewellers.ProductScreen
import com.example.sriramjewellers.AboutScreen
import com.example.sriramjewellers.FundsScreen
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Home(
    username: String,
    onLogout: () -> Unit,
    onViewMoreProducts: () -> Unit
) {
    val context = LocalContext.current
    var selectedTabIndex by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopBar(
                    username = username,
                    onLogout = onLogout,
                    cartItemCount = 0,
                    onCartClick = { /* handle cart click */ }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                BannerCarousel()

                ProductsSection(
                    limit = 3,
                    onViewMore = onViewMoreProducts,
                    onAddToCart = { product ->
                        Toast.makeText(context, "${product.name} added to cart", Toast.LENGTH_SHORT)
                            .show()
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Your TabBar appears here, after the content
                TabBar(
                    selectedTabIndex = selectedTabIndex,
                    onTabSelected = { selectedTabIndex = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Switch content based on selected tab
                when (selectedTabIndex) {
                    0 -> AboutScreen()
                    1 -> ProductScreen(onBack = TODO())
                    2 -> FundsScreen()
                }
            }
        }
    }
}
