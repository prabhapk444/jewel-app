package com.example.sriramjewellers.ui.home

import ProductCard
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore
import com.example.sriramjewellers.Product
import com.example.sriramjewellers.ui.theme.ButtonColor
import com.example.sriramjewellers.ui.theme.ParagraphColor
import com.example.sriramjewellers.ui.theme.components.GlobalLoader

@Composable
fun ProductsSection(limit: Int, onViewMore: () -> Unit, onAddToCart: (Product) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    var isLoading by remember { mutableStateOf(true) }  // <- loader state
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
                            image_url = doc.getString("image_url") ?: "",
                            price = doc.getDouble("price") ?: 0.0,
                            description = doc.getString("description") ?: "",
                            stock = doc.getLong("stock")?.toInt() ?: 0
                        )
                    } catch (e: Exception) {
                        null
                    }
                }
                isLoading = false
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to load products", Toast.LENGTH_SHORT).show()
                isLoading = false
            }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        // Title
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Our Products",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Product List
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(products) { product ->
                ProductCard(product = product, onAddToCart = onAddToCart)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // View More Button centered
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            TextButton(onClick = onViewMore) {
                Text("View More", color = ParagraphColor)
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
