import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.sriramjewellers.R
import kotlinx.coroutines.delay

@Composable
fun BannerCarousel() {
    val banners = listOf(
        R.drawable.b1,
        R.drawable.b2,
        R.drawable.b3
    )

    var currentIndex by remember { mutableStateOf(0) }


    LaunchedEffect(currentIndex) {
        delay(3000)
        currentIndex = (currentIndex + 1) % banners.size
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        contentAlignment = Alignment.Center
    ) {
        val bannerRes = banners[currentIndex]
        Image(
            painter = painterResource(id = bannerRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(8.dp))
        )
    }
}
