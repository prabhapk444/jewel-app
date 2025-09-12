package com.example.sriramjewellers.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable


fun TabBar(selectedTabIndex: Int, onTabSelected: (Int) -> Unit) {
    val tabs = listOf("About", "Products", "Funds")

    TabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = Color(0xFF272343),
        contentColor = Color.White,
        modifier = Modifier.height(60.dp)
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onTabSelected(index) },
                text = { Text(title, fontSize = 14.sp) }
            )
        }
    }
}
