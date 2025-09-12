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
fun TabBar() {
    var selectedTabIndex by remember { mutableStateOf(0) }

    val tabs = listOf("About","Products", "Funds")

    Column(modifier = Modifier.fillMaxWidth()) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color(0xFF272343),
            contentColor = Color.White,
            modifier = Modifier.height(50.dp)
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title, fontSize = 14.sp) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


    }
}
