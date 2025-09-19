package com.example.sriramjewellers

import android.app.DatePickerDialog
import android.os.Build
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
import com.example.sriramjewellers.ui.theme.ButtonTextColor
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*

val BackgroundColor = Color(0xFFFCFAF8)
val HeadlineColor = Color(0xFF1C1410)
val ParagraphColor = Color(0xFF4D3F33)
val ButtonColor = Color(0xFFD4AF37)

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
    val today = Calendar.getInstance()

    var showAlert by remember { mutableStateOf(false) }

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->

            val selectedDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }


            if (selectedDate.before(today)) {
                showAlert = true
            } else {
                onDateSelected("%02d/%02d/%04d".format(dayOfMonth, month + 1, year))
            }
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


    if (showAlert) {
        AlertDialog(
            onDismissRequest = { showAlert = false },
            confirmButton = {
                TextButton(onClick = { showAlert = false }) {
                    Text("OK")
                }
            },
            title = { Text("Invalid Date Selection") },
            text = { Text("Please select a future date for delivery. Past dates are not allowed.") }
        )
    }
}

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
                    Text("₹${(item.price * item.stock).toInt()}", fontWeight = FontWeight.Medium)
                }
            }

            Divider()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total Amount:", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("₹${totalAmount.toInt()}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
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

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = HeadlineColor)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text("Back", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = HeadlineColor)
            }

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

            OrderSummaryCard(cartItems = cartItems, totalAmount = totalAmount)

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
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

                    db.collection("orders")
                        .document(orderId)
                        .set(orderData)
                        .addOnSuccessListener {

                            val batch = db.batch()

                            scope.launch {
                                try {
                                    var stockError = false
                                    var errorMessage = ""

                                    for (item in cartItems) {
                                        val productDoc = db.collection("products")
                                            .document(item.id)
                                            .get()
                                            .await()

                                        val currentStock = productDoc.getLong("stock") ?: 0
                                        if (currentStock < item.stock) {
                                            stockError = true
                                            errorMessage = "${item.name} has only $currentStock items in stock"
                                            break
                                        }
                                    }

                                    if (stockError) {
                                        db.collection("orders").document(orderId).delete()
                                        isSubmitting = false
                                        dialogMessage = "Order failed: $errorMessage"
                                        showDialog = true
                                    } else {

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

                                            val productRef = db.collection("products").document(item.id)
                                            batch.update(productRef, "stock", FieldValue.increment(-item.stock.toLong()))

                                            val stockMovementId = "${orderId}_STOCK_${index + 1}"
                                            val stockMovementRef = db.collection("stock_movements").document(stockMovementId)
                                            val stockMovementData = hashMapOf(
                                                "movementId" to stockMovementId,
                                                "orderId" to orderId,
                                                "productId" to item.id,
                                                "productName" to item.name,
                                                "movementType" to "OUT",
                                                "quantity" to item.stock,
                                                "reason" to "Order Placed",
                                                "timestamp" to FieldValue.serverTimestamp(),
                                                "createdAt" to System.currentTimeMillis()
                                            )
                                            batch.set(stockMovementRef, stockMovementData)
                                        }

                                        val userCartRef = db.collection("carts").document(username)
                                        batch.delete(userCartRef)

                                        batch.commit()
                                            .addOnSuccessListener {
                                                isSubmitting = false
                                                dialogMessage = "Order confirmed successfully!\nOrder ID: $orderId\n\nYour items will be delivered on $deliveryDate"
                                                showDialog = true
                                            }
                                            .addOnFailureListener { exception ->
                                                isSubmitting = false
                                                db.collection("orders").document(orderId).delete()
                                                dialogMessage = "Failed to process order: ${exception.message}"
                                                showDialog = true
                                            }
                                    }
                                } catch (e: Exception) {
                                    isSubmitting = false
                                    db.collection("orders").document(orderId).delete()
                                    dialogMessage = "Error processing order: ${e.message}"
                                    showDialog = true
                                }
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
                        Text("Processing Order...", color = Color.White)
                    }
                } else {
                    Text("Confirm Order", color = ButtonTextColor, fontWeight = FontWeight.Bold)
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

                    if (dialogMessage.contains("Order confirmed successfully")) {
                        onNavigateHome()
                    }
                }) {
                    Text("OK")
                }
            },
            title = { Text(if (dialogMessage.contains("success")) "Success" else "Notice") },
            text = { Text(dialogMessage) }
        )
    }
}