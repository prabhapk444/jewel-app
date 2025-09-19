package com.example.sriramjewellers.ui.home


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    username: String,
    cartItemCount: Int,
    showCartIcon: Boolean = true,
    onCartClick: () -> Unit,
    onLogout: () -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    val colors = MaterialTheme.colorScheme

    TopAppBar(
        title = {
            Text(
                "Welcome To Sriram Jewellers, $username",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colors.onPrimary
            )
        },
        actions = {
            if (showCartIcon) {
                IconButton(onClick = onCartClick) {
                    BadgedBox(
                        badge = {
                            if (cartItemCount > 0) {
                                Badge { Text("$cartItemCount") }
                            }
                        }
                    ) {
                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = "Cart",
                            tint = colors.onPrimary
                        )
                    }
                }
            }
            IconButton(onClick = { showLogoutDialog = true }) {
                Icon(
                    Icons.Default.ExitToApp,
                    contentDescription = "Logout",
                    tint = colors.onPrimary
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colors.primary
        ),

    )

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Confirm Logout") },
            text = { Text("Are you sure you want to logout?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
