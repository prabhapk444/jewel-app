package com.example.sriramjewellers

import ProductCard
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.example.sriramjewellers.Product
import com.example.sriramjewellers.ui.home.Home
import com.example.sriramjewellers.ui.home.TabBar
import com.example.sriramjewellers.ui.home.TopBar
import com.example.sriramjewellers.ui.theme.ButtonColor
import com.example.sriramjewellers.ui.theme.components.GlobalLoader

@RequiresApi(Build.VERSION_CODES.O)

@Composable
fun ProductScreen(
    onBack: () -> Unit,
    username: String,
    onLogout: () -> Unit,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    var products by remember { mutableStateOf(listOf<Product>()) }
    var isLoading by remember { mutableStateOf(true) }  // <- loader state

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    // Load products from Firestore
    LaunchedEffect(Unit) {
        db.collection("products")
            .get()
            .addOnSuccessListener { result ->
                products = result.documents.mapNotNull { doc ->
                    try {
                        Product(
                            id = doc.id,
                            name = doc.getString("name") ?: "",
                            category = doc.getString("category") ?: "",
                            material = doc.getString("material") ?: "",
                            description = doc.getString("description") ?: "",
                            image_url = doc.getString("image_url") ?: "",
                            price = doc.getDouble("price") ?: 0.0,
                            stock = doc.getLong("stock")?.toInt() ?: 0
                        )
                    } catch (e: Exception) {
                        null
                    }
                }
                isLoading = false // <- data loaded
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to load products", Toast.LENGTH_SHORT).show()
                isLoading = false // <- stop loader even if failed
            }
    }

    val filteredProducts = products.filter { product ->
        (selectedCategory == null || product.category == selectedCategory) &&
                product.name.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        bottomBar = {
            TabBar(selectedIndex = selectedTabIndex, onTabSelected = onTabSelected)
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {

            Column {
                TopBar(
                    username = username,
                    onLogout = onLogout,
                    showCartIcon = true,
                    cartItemCount = 0,
                    onCartClick = { /* handle cart */ }
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search Products") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                val categories = products.map { it.category }.distinct()
                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { category ->
                        Button(
                            onClick = { selectedCategory = if (selectedCategory == category) null else category },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedCategory == category) ButtonColor else Color.Gray
                            )
                        ) {
                            Text(category)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredProducts) { product ->
                        ProductCard(
                            product = product,
                            onAddToCart = {

                                Toast.makeText(context, "${product.name} added to cart", Toast.LENGTH_SHORT).show()


                            }
                        )
                    }
                }

            }


            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    GlobalLoader()
                }
            }
        }
    }
}
