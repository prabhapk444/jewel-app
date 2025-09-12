package com.example.sriramjewellers.ui.home

import ProductCard
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore
import com.example.sriramjewellers.Product
@Composable
fun ProductsSection(limit: Int, onViewMore: () -> Unit, onAddToCart: (Product) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    var products by remember { mutableStateOf(listOf<Product>()) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        db.collection("products").limit(limit.toLong())
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
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to load products", Toast.LENGTH_SHORT).show()
            }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Our Products",
                fontSize = 20.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )

        }

        Spacer(modifier = Modifier.height(8.dp))



        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(products) { product ->
                ProductCard(product = product, onAddToCart = onAddToCart)
            }
        }

        TextButton(onClick = onViewMore) { Text("View More") }
    }
}
