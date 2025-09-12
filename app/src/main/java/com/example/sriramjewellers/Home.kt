package com.example.sriramjewellers

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.firebase.firestore.FirebaseFirestore

// Product data model
data class Product(
    val id: String = "",
    val category: String = "",
    val description: String = "",
    val image_url: String = "",
    val is_available: Boolean = false,
    val material: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val stock: Int = 0
)

@Composable
fun Home() {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    var products by remember { mutableStateOf(listOf<Product>()) }

    // Fetch products once when the composable is loaded
    LaunchedEffect(Unit) {
        db.collection("products")
            .limit(3) // Only fetch 3 products
            .get()
            .addOnSuccessListener { result ->
                val list = result.documents.mapNotNull { doc ->
                    try {
                        Product(
                            id = doc.id,
                            category = doc.getString("category") ?: "",
                            description = doc.getString("description") ?: "",
                            image_url = doc.getString("image_url") ?: "",
                            is_available = doc.getBoolean("is_available") ?: false,
                            material = doc.getString("material") ?: "",
                            name = doc.getString("name") ?: "",
                            price = doc.getDouble("price") ?: 0.0,
                            stock = doc.getLong("stock")?.toInt() ?: 0
                        )
                    } catch (e: Exception) {
                        null
                    }
                }
                products = list
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error loading products: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Our Products",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(products) { product ->
                ProductCard(product = product)
            }
        }
    }
}

@Composable
fun ProductCard(product: Product) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = product.image_url,
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(text = product.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(text = "Category: ${product.category}", fontSize = 14.sp)
            Text(text = "Material: ${product.material}", fontSize = 14.sp)
            Text(text = "Price: ₹${product.price}", fontWeight = FontWeight.Medium, fontSize = 16.sp)
            Text(text = "Description: ${product.description}", fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))

            if (product.stock == 0) {
                Button(
                    onClick = { /* Do nothing */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Out of Stock", color = Color.White)
                }
            } else {
                Text(
                    text = "In Stock: ${product.stock}",
                    color = Color(0xFF388E3C),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
