package com.example.sriramjewellers


import BackgroundColor
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
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import android.graphics.BitmapFactory
import android.util.Base64
import com.example.sriramjewellers.ui.theme.ButtonColor
import com.example.sriramjewellers.ui.theme.ButtonTextColor
import com.google.firebase.firestore.FirebaseFirestore
import java.text.NumberFormat
import java.util.Locale


fun formatIndianCurrency(amount: Double): String {
    val formatter = NumberFormat.getNumberInstance(Locale("en", "IN"))
    return formatter.format(amount)
}

@Composable
fun Base64Image(
    base64String: String?,
    contentDescription: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    var bitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }

    LaunchedEffect(base64String) {
        isLoading = true
        hasError = false

        if (!base64String.isNullOrBlank()) {
            try {
                val cleanBase64 = base64String.substringAfter(",").takeIf { it.isNotEmpty() }
                    ?: base64String

                val decodedBytes = Base64.decode(cleanBase64, Base64.DEFAULT)
                val decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                bitmap = decodedBitmap
                isLoading = false
            } catch (e: Exception) {
                hasError = true
                isLoading = false
                println("Error decoding Base64 image: ${e.message}")
            }
        } else {
            hasError = true
            isLoading = false
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            }
            hasError || bitmap == null -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = "No Image Available",
                        tint = Color.Gray,
                        modifier = Modifier.size(40.dp)
                    )
                    Text(
                        "No Image",
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                }
            }
            else -> {
                Image(
                    bitmap = bitmap!!.asImageBitmap(),
                    contentDescription = contentDescription,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = contentScale
                )
            }
        }
    }
}@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    username: String,
    cartItems: List<Product>,
    onQuantityChange: (Product, Int) -> Unit,
    onRemove: (Product) -> Unit,
    cartItemCount: Int,
    onLogout: () -> Unit,
    onNavigateToConfirmOrder: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()

    var productImages by remember { mutableStateOf<Map<String, String>>(emptyMap()) }
    var isLoadingImages by remember { mutableStateOf(false) }

    LaunchedEffect(cartItems) {
        if (cartItems.isNotEmpty()) {
            isLoadingImages = true
            val productIds = cartItems.map { it.id }.distinct()
            productIds.forEach { productId ->
                if (productId.isNotEmpty()) {
                    db.collection("products")
                        .document(productId)
                        .get()
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                                val imageUrl = document.getString("image_url")
                                if (!imageUrl.isNullOrBlank()) {
                                    productImages = productImages + (productId to imageUrl)
                                }
                            }
                            isLoadingImages = false
                        }
                        .addOnFailureListener { exception ->
                            println("Error loading image for product $productId: ${exception.message}")
                            isLoadingImages = false
                        }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Cart") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        },
        containerColor = BackgroundColor
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
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

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(cartItems) { product ->
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(200.dp),
                                        shape = RoundedCornerShape(8.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8))
                                    ) {
                                        Base64Image(
                                            base64String = productImages[product.id],
                                            contentDescription = product.name,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text(
                                        text = product.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = Color.Black
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = "₹${formatIndianCurrency(product.price)}",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF2E7D32)
                                    )

                                    if (!product.category.isNullOrBlank()) {
                                        Text("Category: ${product.category}", fontSize = 12.sp, color = Color.Gray)
                                    }
                                    if (!product.material.isNullOrBlank()) {
                                        Text("Material: ${product.material}", fontSize = 12.sp, color = Color.Gray)
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        IconButton(
                                            onClick = {
                                                if (product.stock > 1) {
                                                    onQuantityChange(product, -1)
                                                }
                                            },
                                            enabled = product.stock > 1
                                        ) {
                                            Icon(
                                                Icons.Rounded.Remove,
                                                contentDescription = "Decrease quantity",
                                                tint = if (product.stock > 1) Color.Black else Color.Gray
                                            )
                                        }

                                        Surface(
                                            modifier = Modifier.padding(horizontal = 16.dp),
                                            color = Color(0xFFE3F2FD),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text(
                                                text = "${product.stock}",
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                            )
                                        }

                                        IconButton(onClick = { onQuantityChange(product, 1) }) {
                                            Icon(Icons.Default.Add, contentDescription = "Increase quantity")
                                        }

                                        Spacer(modifier = Modifier.width(16.dp))

                                        IconButton(onClick = { onRemove(product) }) {
                                            Icon(Icons.Default.Delete, contentDescription = "Remove item", tint = Color.Red)
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = "Subtotal: ₹${formatIndianCurrency(product.price * product.stock)}",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF2E7D32)
                                    )
                                }
                            }
                        }
                    }

                    val total = cartItems.sumOf { it.price * it.stock }
                    val totalItems = cartItems.sumOf { it.stock }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1976D2)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Order Summary",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Total Items:", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.White)
                                Text("$totalItems", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.White)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Grand Total:", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text("₹${formatIndianCurrency(total)}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Yellow)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onNavigateToConfirmOrder,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ButtonColor)
                    ) {
                        Text("Process Order", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = ButtonTextColor)
                    }
                }
            }
        }
    }
}
