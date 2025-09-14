package com.example.sriramjewellers.ui.home

import BackgroundColor
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sriramjewellers.Product
import java.text.NumberFormat
import java.util.Locale


fun formatIndianCurrency(amount: Double): String {
    val formatter = NumberFormat.getNumberInstance(Locale("en", "IN"))
    return formatter.format(amount)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    username: String,
    cartItems: List<Product>,
    onQuantityChange: (Product, Int) -> Unit,
    onRemove: (Product) -> Unit,
    selectedTabIndex: Int,
    cartItemCount: Int,
    onLogout: () -> Unit,
    onTabSelected: (Int) -> Unit,
    onNavigateToConfirmOrder: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Cart") },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { onLogout() }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        },
        bottomBar = {
            TabBar(selectedIndex = selectedTabIndex, onTabSelected = onTabSelected)
        },
        containerColor = BackgroundColor
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (cartItems.isEmpty()) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Empty Cart",
                            tint = Color.Gray,
                            modifier = Modifier.size(100.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Your cart is empty!", fontSize = 18.sp, color = Color.Gray)
                    }
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(cartItems) { product ->
                        Card(
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                if (product.image_url.isNotEmpty()) {
                                    val bitmap = remember(product.image_url) {
                                        try {
                                            val imageBytes = Base64.decode(product.image_url, Base64.DEFAULT)
                                            BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                                        } catch (e: Exception) {
                                            null
                                        }
                                    }
                                    if (bitmap != null) {
                                        Image(
                                            bitmap = bitmap.asImageBitmap(),
                                            contentDescription = product.name,
                                            modifier = Modifier
                                                .size(64.dp)
                                                .padding(end = 16.dp)
                                        )
                                    } else {

                                        Box(
                                            modifier = Modifier
                                                .size(64.dp)
                                                .padding(end = 16.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.ShoppingCart,
                                                contentDescription = "Placeholder",
                                                tint = Color.Gray,
                                                modifier = Modifier.size(40.dp)
                                            )
                                        }
                                    }
                                } else {

                                    Box(
                                        modifier = Modifier
                                            .size(64.dp)
                                            .padding(end = 16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ShoppingCart,
                                            contentDescription = "Placeholder",
                                            tint = Color.Gray,
                                            modifier = Modifier.size(40.dp)
                                        )
                                    }
                                }

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(product.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                    Text("Price: ₹${formatIndianCurrency(product.price)}")
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(onClick = {
                                        if (product.stock > 1) {
                                            onQuantityChange(product, -1)
                                        }
                                    }) {
                                        Icon(
                                            imageVector = Icons.Rounded.Remove,
                                            contentDescription = "Decrease quantity"
                                        )

                                    }
                                    Text("${product.stock}", fontSize = 16.sp, modifier = Modifier.padding(horizontal = 8.dp))
                                    IconButton(onClick = { onQuantityChange(product, 1) }) {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = "Increase quantity"
                                        )
                                    }
                                    IconButton(onClick = { onRemove(product) }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Remove item",
                                            tint = Color.Red
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                val total = cartItems.sumOf { it.price * it.stock }
                Text("Total: ₹${formatIndianCurrency(total)}", fontSize = 20.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(16.dp))


                Button(
                    onClick = onNavigateToConfirmOrder,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Confirm Order")
                }



            }
        }
    }
}
