package com.example.sriramjewellers.ui.home

import BackgroundColor
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
    cartItems: MutableList<Product>,
    onQuantityChange: (Product, Int) -> Unit,
    onRemove: (Product) -> Unit,
    selectedTabIndex: Int,
    cartItemCount: Int,
    onLogout: () -> Unit,
    onTabSelected: (Int) -> Unit
) {
    Scaffold(
        bottomBar = { TabBar(selectedIndex = selectedTabIndex, onTabSelected = onTabSelected) },
        containerColor = BackgroundColor
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            TopBar(username, cartItemCount = 0,onLogout= onLogout,
                onCartClick = { /* handle cart */ }

            )
                Text("Your Cart", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                if (cartItems.isEmpty()) {
                    Text("Cart is empty!", fontSize = 18.sp)
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(cartItems) { product ->
                            Card(
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(product.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                        Text("Price: ₹${formatIndianCurrency(product.price)}")
                                    }

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        IconButton(onClick = { onQuantityChange(product, -1) }) {
                                            Text("-", fontSize = 20.sp)
                                        }

                                        Text("${product.stock}", fontSize = 16.sp, modifier = Modifier.padding(horizontal = 8.dp))

                                        IconButton(onClick = { onQuantityChange(product, 1) }) {
                                            Text("+", fontSize = 20.sp)
                                        }

                                        IconButton(onClick = { onRemove(product) }) {
                                            Text("🗑️")
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    val total = cartItems.sumOf { it.price * it.stock }
                    Text("Total: ₹${formatIndianCurrency(total)}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

}

