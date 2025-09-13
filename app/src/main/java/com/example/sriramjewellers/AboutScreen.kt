import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.Image
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import kotlin.random.Random
import com.example.sriramjewellers.R
import com.example.sriramjewellers.ui.home.TopBar
import com.example.sriramjewellers.ui.home.TabBar

val BackgroundColor = Color(0xFFFFFfFE)
val HeadlineColor = Color(0xFF272343)
val ParagraphColor = Color(0xFF2D334A)
val ButtonColor = Color(0xFFFFD803)
val ButtonTextColor = Color(0xFF272343)
val BorderColors = listOf(Color(0xFFFFD803), Color(0xFF272343), Color(0xFF2D334A))

@Composable
fun AboutScreen(
    username: String,
    onLogout: () -> Unit,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    val testimonials = listOf(
        Testimonial("Ramesh K.", "Absolutely loved the necklace I bought. Exceptional quality and service!"),
        Testimonial("Anita P.", "Sriram Jewellers is my go-to for all occasions. Highly recommended!"),
        Testimonial("Vikram S.", "Great craftsmanship and friendly staff. Will shop again!")
    )

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    Scaffold(

        bottomBar = {
            TabBar(selectedIndex = selectedTabIndex, onTabSelected = onTabSelected)
        },
        containerColor = BackgroundColor
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {


                TopBar(username = username, onLogout = onLogout, cartItemCount = 0, onCartClick = {})

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
                "What Our Customers Say",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = HeadlineColor,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                testimonials.forEachIndexed { index, testimonial ->

                    val borderColor by remember { mutableStateOf(BorderColors[Random.nextInt(BorderColors.size)]) }

                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(animationSpec = tween(500, index * 150)) +
                                slideInVertically(initialOffsetY = { it / 2 }, animationSpec = tween(500, index * 150))
                    ) {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(3.dp, borderColor, RoundedCornerShape(16.dp)),
                            colors = CardDefaults.cardColors(containerColor = ButtonColor.copy(alpha = 0.1f)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = testimonial.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = HeadlineColor
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = testimonial.message,
                                    fontSize = 14.sp,
                                    color = ParagraphColor,
                                    textAlign = TextAlign.Justify
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

data class Testimonial(val name: String, val message: String)
