import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sriramjewellers.ui.home.TabBar
import com.example.sriramjewellers.ui.home.TopBar
import androidx.compose.material3.Icon
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.sriramjewellers.R



val BackgroundColor = Color(0xFFFCFAF8)
val HeadlineColor = Color(0xFF1C1410)
val ParagraphColor = Color(0xFF4D3F33)

val ButtonColor = Color(0xFFD4AF37)
val ButtonTextColor = Color(0xFF0F0A07)

@Composable
fun AboutScreen(
    username: String,
    onLogout: () -> Unit,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    Scaffold(

        bottomBar = { TabBar(selectedIndex = selectedTabIndex, onTabSelected = onTabSelected) },
        containerColor = BackgroundColor
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {


                TopBar(username = username, onLogout = onLogout, cartItemCount = 0, onCartClick = {}, showCartIcon = false,)


            Image(
                painter = painterResource(id = R.drawable.sri),
                contentDescription = "About Banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )


            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "About Sriram Jewellers",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = HeadlineColor,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))


            Text("Vision", fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = HeadlineColor, modifier = Modifier.padding(horizontal = 16.dp))
            Text(
                "To be the most trusted and admired jewellery brand delivering elegance and quality to our customers.",
                fontSize = 16.sp,
                color = ParagraphColor,
                textAlign = TextAlign.Justify,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )


            Text("Mission", fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = HeadlineColor, modifier = Modifier.padding(horizontal = 16.dp))
            Text(
                "To craft timeless jewellery pieces with exceptional craftsmanship, offering personalized services that delight our customers.",
                fontSize = 16.sp,
                color = ParagraphColor,
                textAlign = TextAlign.Justify,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))


            Text(
                "Contact Us",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = HeadlineColor,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                ContactItem(icon = Icons.Filled.Phone, label = "Phone", value = "04562-226414")
                ContactItem(icon = Icons.Filled.Phone, label = "Whatsapp", value = "9047688283") // generic phone

                ContactItem(icon = Icons.Filled.LocationOn, label = "Address", value = "110, South car Street, Sivakasi - 626124, Tamil Nadu")
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}



@Composable
fun ContactItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = ButtonColor.copy(alpha = 0.1f)),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "$label Icon",
                tint = HeadlineColor,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = label, fontWeight = FontWeight.Bold, color = HeadlineColor, fontSize = 16.sp)
                Text(text = value, color = ParagraphColor, fontSize = 14.sp)
            }
        }
    }
}
