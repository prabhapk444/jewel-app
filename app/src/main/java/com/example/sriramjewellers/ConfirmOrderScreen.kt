package com.example.sriramjewellers

import android.app.DatePickerDialog
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import com.example.sriramjewellers.Product
import kotlinx.coroutines.launch
import java.util.*



val BackgroundColor = Color(0xFFFFFBF5)
val HeadlineColor = Color(0xFF3E2C2C)
val ParagraphColor = Color(0xFF5C4B4B)
val ButtonColor = Color(0xFFB8860B)

val ButtonTextColor = Color(0xFFFFFFFF)



@Composable
fun CustomBasicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    readOnly: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onClick: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF0F0F0), RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
    ) {
        if (value.isEmpty()) {
            Text(text = placeholder, color = ParagraphColor.copy(alpha = 0.5f))
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            readOnly = readOnly,
            textStyle = TextStyle(color = ParagraphColor, fontSize = 16.sp),
            keyboardOptions = keyboardOptions,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun DatePickerField(
    date: String,
    placeholder: String,
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            onDateSelected("%02d/%02d/%04d".format(dayOfMonth, month + 1, year))
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF0F0F0), RoundedCornerShape(12.dp))
            .clickable { datePickerDialog.show() }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = if (date.isNotEmpty()) date else placeholder,
            color = if (date.isNotEmpty()) ParagraphColor else ParagraphColor.copy(alpha = 0.5f),
            fontSize = 16.sp
        )
    }
}

// -------------------- Order Summary Card --------------------
@Composable
fun OrderSummaryCard(cartItems: List<Product>, totalAmount: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Order Summary", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Divider()

            cartItems.forEach { item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(item.name, fontWeight = FontWeight.Medium)
                        Text("Qty: ${item.stock}", fontSize = 14.sp, color = Color.Gray)
                    }
                    Text("₹${item.price * item.stock}", fontWeight = FontWeight.Medium)
                }
            }

            Divider()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total Amount:", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("₹$totalAmount", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ConfirmOrderScreen(
    username: String,
    cartItems: List<Product>,
    onNavigateHome: () -> Unit,
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    val scope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var aadhar by remember { mutableStateOf("") }
    var deliveryDate by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }

    // AlertDialog state
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    val totalAmount = cartItems.sumOf { it.price * it.stock }

    Scaffold(containerColor = BackgroundColor) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = HeadlineColor)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text("Confirm Order", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = HeadlineColor)
            }

            // Customer Information Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Customer Information", fontWeight = FontWeight.Bold, fontSize = 16.sp)

                    CustomBasicTextField(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = "Full Name*"
                    )
                    CustomBasicTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        placeholder = "Phone Number*",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )
                    CustomBasicTextField(
                        value = address,
                        onValueChange = { address = it },
                        placeholder = "Delivery Address*"
                    )
                    CustomBasicTextField(
                        value = aadhar,
                        onValueChange = { aadhar = it },
                        placeholder = "Aadhar Number*",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    DatePickerField(
                        date = deliveryDate,
                        placeholder = "Preferred Delivery Date*",
                        onDateSelected = { deliveryDate = it }
                    )
                }
            }

            // Order Summary
            OrderSummaryCard(cartItems = cartItems, totalAmount = totalAmount)

            Spacer(modifier = Modifier.height(8.dp))

            // Submit Button
            Button(
                onClick = {
                    // Validation
                    when {
                        name.isBlank() -> {
                            dialogMessage = "Please enter your name"
                            showDialog = true
                            return@Button
                        }
                        phone.isBlank() || phone.length < 10 -> {
                            dialogMessage = "Please enter a valid phone number"
                            showDialog = true
                            return@Button
                        }
                        address.isBlank() -> {
                            dialogMessage = "Please enter delivery address"
                            showDialog = true
                            return@Button
                        }
                        aadhar.isBlank() || aadhar.length != 12 -> {
                            dialogMessage = "Please enter a valid 12-digit Aadhar number"
                            showDialog = true
                            return@Button
                        }
                        deliveryDate.isBlank() -> {
                            dialogMessage = "Please select delivery date"
                            showDialog = true
                            return@Button
                        }
                    }

                    isSubmitting = true

                    // Generate order ID
                    val orderId = "ORD_${System.currentTimeMillis()}"

                    val orderData = hashMapOf(
                        "orderId" to orderId,
                        "username" to username.trim(),
                        "customerName" to name.trim(),
                        "phone" to phone.trim(),
                        "address" to address.trim(),
                        "aadharNumber" to aadhar.trim(),
                        "deliveryDate" to deliveryDate,
                        "status" to "Pending",
                        "totalAmount" to totalAmount,
                        "itemCount" to cartItems.size,
                        "timestamp" to FieldValue.serverTimestamp(),
                        "createdAt" to System.currentTimeMillis()
                    )

                    // Create order document
                    db.collection("orders")
                        .document(orderId)
                        .set(orderData)
                        .addOnSuccessListener {
                            // Save order items
                            val batch = db.batch()
                            cartItems.forEachIndexed { index, item ->
                                val orderItemId = "${orderId}_ITEM_${index + 1}"
                                val orderItemRef = db.collection("order_items").document(orderItemId)
                                val orderItemData = hashMapOf(
                                    "orderItemId" to orderItemId,
                                    "orderId" to orderId,
                                    "productId" to item.id,
                                    "productName" to item.name,
                                    "category" to item.category,
                                    "material" to item.material,
                                    "price" to item.price,
                                    "quantity" to item.stock,
                                    "subtotal" to (item.price * item.stock),
                                    "timestamp" to FieldValue.serverTimestamp(),
                                    "createdAt" to System.currentTimeMillis()
                                )
                                batch.set(orderItemRef, orderItemData)
                            }
                            batch.commit()
                                .addOnSuccessListener {
                                    isSubmitting = false
                                    dialogMessage = "Order confirmed! Order ID: $orderId"
                                    showDialog = true
                                }
                                .addOnFailureListener { exception ->
                                    isSubmitting = false
                                    db.collection("orders").document(orderId).delete()
                                    dialogMessage = "Failed to save order items: ${exception.message}"
                                    showDialog = true
                                }
                        }
                        .addOnFailureListener { exception ->
                            isSubmitting = false
                            dialogMessage = "Failed to confirm order: ${exception.message}"
                            showDialog = true
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = !isSubmitting,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSubmitting) Color.Gray else ButtonColor
                )
            ) {
                if (isSubmitting) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Submitting...", color = Color.White)
                    }
                } else {
                    Text("Confirm Order", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }


    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false

                    if (dialogMessage.startsWith("Order confirmed")) onNavigateHome()
                }) {
                    Text("OK")
                }
            },
            title = { Text("Message") },
            text = { Text(dialogMessage) }
        )
    }
}
