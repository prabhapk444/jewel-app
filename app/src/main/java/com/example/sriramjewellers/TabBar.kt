package com.example.sriramjewellers.ui.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color




@Composable
fun TabBar(selectedIndex: Int, onTabSelected: (Int) -> Unit) {
    val colors = MaterialTheme.colorScheme

    NavigationBar(
        containerColor = colors.primary,
        contentColor = colors.onPrimary
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = selectedIndex == 0,
            onClick = { onTabSelected(0) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = colors.onPrimary,
                unselectedIconColor = colors.onPrimary.copy(alpha = 0.7f),
                selectedTextColor = colors.onPrimary,
                unselectedTextColor = colors.onPrimary.copy(alpha = 0.7f),
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Info, contentDescription = "About") },
            label = { Text("About") },
            selected = selectedIndex == 1,
            onClick = { onTabSelected(1) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = colors.onPrimary,
                unselectedIconColor = colors.onPrimary.copy(alpha = 0.7f),
                selectedTextColor = colors.onPrimary,
                unselectedTextColor = colors.onPrimary.copy(alpha = 0.7f),
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Store, contentDescription = "Products") },
            label = { Text("Products") },
            selected = selectedIndex == 2,
            onClick = { onTabSelected(2) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = colors.onPrimary,
                unselectedIconColor = colors.onPrimary.copy(alpha = 0.7f),
                selectedTextColor = colors.onPrimary,
                unselectedTextColor = colors.onPrimary.copy(alpha = 0.7f),
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = "Funds") },
            label = { Text("Funds") },
            selected = selectedIndex == 3,
            onClick = { onTabSelected(3) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = colors.onPrimary,
                unselectedIconColor = colors.onPrimary.copy(alpha = 0.7f),
                selectedTextColor = colors.onPrimary,
                unselectedTextColor = colors.onPrimary.copy(alpha = 0.7f),
                indicatorColor = Color.Transparent
            )
        )
    }
}
