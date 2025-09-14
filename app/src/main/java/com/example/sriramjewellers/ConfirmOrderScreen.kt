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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sriramjewellers.ui.theme.BackgroundColor
import com.example.sriramjewellers.ui.theme.ButtonColor
import com.example.sriramjewellers.ui.theme.HeadlineColor
import com.example.sriramjewellers.ui.theme.ParagraphColor
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.util.*
import androidx.compose.material.icons.filled.ArrowBack




@Composable
fun CustomBasicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    readOnly: Boolean = false,
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
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val scope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var aadhar by remember { mutableStateOf("") }
    var deliveryDate by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    // DatePickerDialog
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, day ->
            deliveryDate = "%02d/%02d/%04d".format(day, month + 1, year)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val totalAmount = cartItems.sumOf { it.price * it.stock }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Confirm Order", color = HeadlineColor) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = HeadlineColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundColor,
                    titleContentColor = HeadlineColor
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = BackgroundColor
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            CustomBasicTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = "Name"
            )
            CustomBasicTextField(
                value = phone,
                onValueChange = { phone = it },
                placeholder = "Phone Number"
            )
            CustomBasicTextField(
                value = address,
                onValueChange = { address = it },
                placeholder = "Address"
            )
            CustomBasicTextField(
                value = aadhar,
                onValueChange = { aadhar = it },
                placeholder = "Aadhar Number"
            )
            CustomBasicTextField(
                value = deliveryDate,
                onValueChange = {},
                placeholder = "Delivery Date",
                readOnly = true,
                onClick = { datePickerDialog.show() }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Total & Confirm Order Button
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Total: ₹$totalAmount", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, fontSize = 18.sp)
                Button(
                    onClick = {
                        isSubmitting = true
                        val orderRef = db.collection("orders").document()
                        val orderData = hashMapOf(
                            "id" to orderRef.id,
                            "username" to username,
                            "name" to name,
                            "phone" to phone,
                            "address" to address,
                            "aadhar" to aadhar,
                            "deliveryDate" to deliveryDate,
                            "status" to "Pending",
                            "timestamp" to System.currentTimeMillis()
                        )
                        orderRef.set(orderData).addOnSuccessListener {
                            val batch = db.batch()
                            cartItems.forEach { item ->
                                val itemRef = db.collection("order_items").document()
                                batch.set(itemRef, mapOf(
                                    "orderId" to orderRef.id,
                                    "productId" to item.id,
                                    "name" to item.name,
                                    "price" to item.price,
                                    "quantity" to item.stock
                                ))
                            }
                            batch.commit().addOnSuccessListener {
                                isSubmitting = false
                                scope.launch {
                                    snackbarHostState.showSnackbar("Order confirmed!")
                                    onNavigateHome()
                                }
                            }.addOnFailureListener {
                                isSubmitting = false
                                scope.launch { snackbarHostState.showSnackbar("Failed to save order items.") }
                            }
                        }.addOnFailureListener {
                            isSubmitting = false
                            scope.launch { snackbarHostState.showSnackbar("Failed to confirm order.") }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isSubmitting
                ) {
                    Text(if (isSubmitting) "Submitting..." else "Confirm Order")
                }
            }
        }
    }
}
