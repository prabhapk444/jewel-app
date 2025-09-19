package com.example.sriramjewellers

import ProductCard
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.example.sriramjewellers.ui.home.*
import com.example.sriramjewellers.ui.theme.ButtonColor
import com.example.sriramjewellers.ui.theme.ParagraphColor
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
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()

    var showConfirmOrder by remember { mutableStateOf(false) }
    var showCart by remember { mutableStateOf(false) }
    var products by remember { mutableStateOf(listOf<Product>()) }
    var isLoading by remember { mutableStateOf(true) }
    val cartItems = remember { mutableStateListOf<Product>() }
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<String?>(null) }


    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }



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
                            stock = 1
                        )
                    } catch (e: Exception) {
                        null
                    }
                }
                isLoading = false
            }
            .addOnFailureListener {
                dialogMessage = "Failed to load products"
                showDialog = true
                isLoading = false
            }
    }

    val filteredProducts = products.filter { product ->
        (selectedCategory == null || product.category == selectedCategory) &&
                product.name.contains(searchQuery, ignoreCase = true)
    }

    when {
        showConfirmOrder -> {
            ConfirmOrderScreen(
                username = username,
                cartItems = cartItems,
                onNavigateHome = {
                    showConfirmOrder = false
                    showCart = false
                    cartItems.clear()
                },
                onBack = { showConfirmOrder = false },
                onLogout = onLogout
            )
        }
        showCart -> {
            CartScreen(
                username = username,
                cartItems = cartItems,
                onQuantityChange = { product, delta ->
                    val index = cartItems.indexOfFirst { it.id == product.id }
                    if (index != -1) {
                        val updated = cartItems[index].copy(stock = (cartItems[index].stock + delta).coerceAtLeast(1))
                        cartItems[index] = updated
                    }
                },
                onRemove = { product -> cartItems.removeAll { it.id == product.id } },
                cartItemCount = cartItems.sumOf { it.stock },
                onLogout = onLogout,
                onBack = { showCart = false },
                onNavigateToConfirmOrder = { showConfirmOrder = true }
            )
        }
        else -> {
            Scaffold(
                containerColor = BackgroundColor,
                topBar = {
                    TopBar(
                        username = username,
                        onLogout = onLogout,
                        showCartIcon = true,
                        cartItemCount = cartItems.sumOf { it.stock },
                        onCartClick = { showCart = true }
                    )
                },

                bottomBar = { TabBar(selectedIndex = selectedTabIndex, onTabSelected = onTabSelected) }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    Column {

                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            label = { Text("Search Products") },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            textStyle = TextStyle(color = ParagraphColor),
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
                                        val index = cartItems.indexOfFirst { it.id == product.id }
                                        if (index != -1) {
                                            val updated = cartItems[index].copy(stock = cartItems[index].stock + 1)
                                            cartItems[index] = updated
                                        } else {
                                            cartItems.add(product.copy(stock = 1))
                                        }
                                        dialogMessage = "${product.name} added to cart"
                                        showDialog = true
                                    }
                                )
                            }
                        }
                    }
                    if (isLoading) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            GlobalLoader()
                        }
                    }
                }
            }
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
