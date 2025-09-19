import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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

@Composable
fun AboutScreen(
    username: String,
    onLogout: () -> Unit,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                username,
                cartItemCount = 0,
                onCartClick = { /* handle cart */ },
                onLogout = onLogout,
                showCartIcon = false,
            )
        },
        bottomBar = { TabBar(selectedIndex = selectedTabIndex, onTabSelected = onTabSelected) },
        containerColor = BackgroundColor
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                    .shadow(8.dp, RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.sri),
                    contentDescription = "About Banner",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )


                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Color.Black.copy(alpha = 0.2f)
                        )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))


            Text(
                text = "About Sriram Jewellers",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = HeadlineColor,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )


            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(4.dp)
                    .background(
                        ButtonColor,
                        RoundedCornerShape(2.dp)
                    )
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(32.dp))


            VisionMissionSection()

            Spacer(modifier = Modifier.height(40.dp))


            Text(
                text = "Get In Touch",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = HeadlineColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "We'd love to hear from you",
                fontSize = 16.sp,
                color = ParagraphColor,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ContactItem(
                    icon = Icons.Filled.Phone,
                    label = "Phone",
                    value = "04562-226414"
                )
                ContactItem(
                    icon = Icons.Filled.Phone,
                    label = "WhatsApp",
                    value = "9047688283"
                )
                ContactItem(
                    icon = Icons.Filled.LocationOn,
                    label = "Address",
                    value = "110, South Car Street, Sivakasi - 626124, Tamil Nadu"
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun VisionMissionSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = ButtonColor.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Filled.Visibility,
                        contentDescription = "Vision Icon",
                        tint = ButtonColor,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Our Vision",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = HeadlineColor
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "To be the most trusted and admired jewellery brand delivering elegance and quality to our customers.",
                    fontSize = 16.sp,
                    color = ParagraphColor,
                    textAlign = TextAlign.Justify,
                    lineHeight = 24.sp
                )
            }
        }


        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = ButtonColor.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Filled.Flag,
                        contentDescription = "Mission Icon",
                        tint = ButtonColor,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Our Mission",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = HeadlineColor
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "To craft timeless jewellery pieces with exceptional craftsmanship, offering personalized services that delight our customers.",
                    fontSize = 16.sp,
                    color = ParagraphColor,
                    textAlign = TextAlign.Justify,
                    lineHeight = 24.sp
                )
            }
        }
    }
}

@Composable
fun ContactItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        ButtonColor.copy(alpha = 0.15f),
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = "$label Icon",
                    tint = ButtonColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = label,
                    fontWeight = FontWeight.SemiBold,
                    color = HeadlineColor,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = value,
                    color = ParagraphColor,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
        }
    }
}