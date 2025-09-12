import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sriramjewellers.Product
import com.example.sriramjewellers.ui.theme.ButtonColor
import com.example.sriramjewellers.ui.theme.ButtonTextColor

@Composable
fun ProductCard(product: Product, onAddToCart: (Product) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Decode Base64 image
            val imageBitmap = remember(product.image_url) {
                try {
                    val base64String = product.image_url.substringAfter("base64,")
                    val bytes = Base64.decode(base64String, Base64.DEFAULT)
                    BitmapFactory.decodeByteArray(bytes, 0, bytes.size).asImageBitmap()
                } catch (e: Exception) {
                    null
                }
            }

            if (imageBitmap != null) {
                Image(
                    bitmap = imageBitmap,
                    contentDescription = product.name,
                    modifier = Modifier
                        .fillMaxWidth(1.0f)
                        .height(230.dp),
                    contentScale = ContentScale.Crop
                )

            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = product.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(text = "Category: ${product.category}", fontSize = 14.sp)
            Text(text = "Material: ${product.material}", fontSize = 14.sp)
            Text(text = "Price: ₹${product.price}", fontWeight = FontWeight.Medium, fontSize = 16.sp)
            Text(text = "Description: ${product.description}", fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))

            if (product.stock == 0) {
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Out of Stock", color = Color.White)
                }
            } else {
                Button(
                    onClick = { onAddToCart(product) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonColor,
                        contentColor = ButtonTextColor
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add to Cart")
                }
            }

        }
    }
}
