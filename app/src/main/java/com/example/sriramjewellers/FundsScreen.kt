
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sriramjewellers.BackgroundColor
import com.example.sriramjewellers.ui.home.TabBar
import com.example.sriramjewellers.ui.home.TopBar
import com.example.sriramjewellers.ui.theme.HeadlineColor
import com.example.sriramjewellers.ui.theme.ButtonColor
import com.example.sriramjewellers.ui.theme.ButtonTextColor
import com.example.sriramjewellers.ui.theme.ParagraphColor
import com.example.sriramjewellers.ui.theme.headlineColorCompat
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.ui.text.TextStyle



data class FundRecord(
    val month: String = "",
    val amount: String = "",
    val phone: String = "",
    val year: String = ""
)

@Composable
fun FundsScreen(
    username: String,
    onBack: () -> Unit,
    onLogout: () -> Unit,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    var phoneInput by remember { mutableStateOf("") }
    var fundRecords by remember { mutableStateOf(listOf<FundRecord>()) }
    var isLoading by remember { mutableStateOf(false) }
    var hasSearched by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context: Context = LocalContext.current

    BackHandler {
        onBack()
    }

    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            TopBar(
                username,
                cartItemCount = 0,
                onCartClick = { /* handle cart */ },
                onLogout = onLogout,
                showCartIcon = false,
            )
        },
        bottomBar = {
            TabBar(selectedIndex = selectedTabIndex, onTabSelected = onTabSelected)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)

        ) {


            Spacer(modifier = Modifier.height(24.dp))


            Card(

                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BackgroundColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBalance,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.headlineColorCompat
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Monthly Funds Tracker",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = HeadlineColor,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Track your monthly fund contributions",
                        fontSize = 14.sp,
                        color = ParagraphColor.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))


            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Search Funds",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = HeadlineColor,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    OutlinedTextField(
                        value = phoneInput,
                        onValueChange = { phoneInput = it },

                        label = { Text("Phone Number") },
                        placeholder = { Text("Enter 10-digit phone number")},
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = "Phone"
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        textStyle = TextStyle(color = ParagraphColor)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            if (phoneInput.isNotBlank()) {
                                isLoading = true
                                hasSearched = true
                                errorMessage = null

                                db.collection("funds")
                                    .whereEqualTo("phone", phoneInput.trim())
                                    .get()
                                    .addOnSuccessListener { documents ->
                                        isLoading = false
                                        val list = documents.mapNotNull { doc ->
                                            val month = doc.getString("month") ?: return@mapNotNull null
                                            val amount = doc.get("amount")?.toString() ?: "0"
                                            val phone = doc.getString("phone") ?: return@mapNotNull null
                                            val year = when(val y = doc.get("year")) {
                                                is String -> y
                                                is Number -> y.toInt().toString()
                                                else -> "Unknown"
                                            }
                                            FundRecord(
                                                month = month,
                                                amount = amount,
                                                phone = phone,
                                                year = year
                                            )
                                        }.sortedWith(compareByDescending<FundRecord> { it.year }.thenByDescending { it.month })

                                        fundRecords = list
                                    }
                                    .addOnFailureListener { exception ->
                                        isLoading = false
                                        errorMessage = "Failed to fetch records. Please try again."
                                    }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
                        enabled = phoneInput.isNotBlank() && !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = ButtonTextColor,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        } else {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text(
                            text = if (isLoading) "Searching..." else "Search Records",
                            color = ButtonTextColor,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


            errorMessage?.let { error ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor =  com.example.sriramjewellers.ui.theme.BackgroundColor)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = com.example.sriramjewellers.ui.theme.BackgroundColor,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = error,
                            color = com.example.sriramjewellers.ui.theme.BackgroundColor,
                            fontSize = 14.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }


            when {
                !hasSearched -> {

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = BackgroundColor.copy(alpha = 0.5f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = ParagraphColor.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Welcome to Fund Tracker",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = HeadlineColor,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Enter a phone number above to view fund contribution history",
                                fontSize = 14.sp,
                                color = ParagraphColor.copy(alpha = 0.8f),
                                textAlign = TextAlign.Center,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }

                fundRecords.isEmpty() && hasSearched && !isLoading -> {

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = BackgroundColor.copy(alpha = 0.3f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = ParagraphColor.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "No Records Found",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = HeadlineColor,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No fund records found for phone number: $phoneInput\n\nPlease verify the number and try again.",
                                fontSize = 14.sp,
                                color = ParagraphColor.copy(alpha = 0.8f),
                                textAlign = TextAlign.Center,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }

                else -> {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Fund Records",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = HeadlineColor
                        )
                        Text(
                            text = "${fundRecords.size} record${if (fundRecords.size != 1) "s" else ""}",
                            fontSize = 14.sp,
                            color = ParagraphColor.copy(alpha = 0.7f)
                        )
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(fundRecords) { record ->
                            EnhancedFundItem(record)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EnhancedFundItem(record: FundRecord) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${record.month} ${record.year}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = HeadlineColor
                    )
                }

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = com.example.sriramjewellers.ui.theme.BackgroundColor)
                ) {
                    Text(
                        text = "₹${record.amount}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = ParagraphColor.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = record.phone,
                    fontSize = 14.sp,
                    color = ParagraphColor.copy(alpha = 0.8f)
                )
            }
        }
    }
}