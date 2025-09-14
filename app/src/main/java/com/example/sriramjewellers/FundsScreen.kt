import BackgroundColor
import android.R
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sriramjewellers.ui.home.TabBar
import com.example.sriramjewellers.ui.home.TopBar
import com.example.sriramjewellers.ui.theme.BackgroundColor
import com.example.sriramjewellers.ui.theme.HeadlineColor
import com.example.sriramjewellers.ui.theme.ButtonColor
import com.example.sriramjewellers.ui.theme.ButtonTextColor
import com.example.sriramjewellers.ui.theme.ParagraphColor
import com.google.firebase.firestore.FirebaseFirestore


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
    val context: Context = LocalContext.current


    BackHandler {
        onBack()
    }

    Scaffold(
        bottomBar = {
            TabBar(selectedIndex = selectedTabIndex, onTabSelected = onTabSelected)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            TopBar(username, cartItemCount = 0,
                onLogout=onLogout,
                onCartClick = { /* handle cart */ },
                showCartIcon = false,

            )

            Spacer(modifier = Modifier.height(2.dp))


            Text(
                text = "Your Monthly Funds",
                fontSize = 20.sp,
                color = HeadlineColor,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = phoneInput,
                onValueChange = { phoneInput = it },
                label = { Text("Enter Phone Number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (phoneInput.isNotBlank()) {
                        db.collection("funds")
                            .whereEqualTo("phone", phoneInput)
                            .get()
                            .addOnSuccessListener { documents ->
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
                                }
                                fundRecords = list

                            }
                            .addOnFailureListener {
                                // Handle error if needed
                            }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ButtonColor)
            ) {
                Text("Search", color = ButtonTextColor)
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(fundRecords) { record ->
                    FundItem(record)
                }
            }
        }
    }
}

@Composable
fun FundItem(record: FundRecord) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Month: ${record.month}", fontSize = 14.sp, color = ParagraphColor)
                Text("Amount: ${record.amount}", fontSize = 14.sp, color = ParagraphColor)
            }
            Column {
                Text("Phone: ${record.phone}", fontSize = 14.sp, color = ParagraphColor)
                Text("Year: ${record.year}", fontSize = 14.sp, color = ParagraphColor)
            }
        }
    }
}
