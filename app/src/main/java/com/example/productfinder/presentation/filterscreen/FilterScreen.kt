@file:OptIn(ExperimentalFoundationApi::class)

package com.example.productfinder.presentation.filterscreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.productfinder.presentation.homescreen.viewmodel.HomeScreenViewModel

@Composable
fun FilterScreen(
    navController: NavController,
    homeScreenViewModel: HomeScreenViewModel = hiltViewModel()
) {
    val categories by homeScreenViewModel.categories.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                tint = Color.White,
                contentDescription = null,
                modifier = Modifier.clickable { navController.popBackStack() }
            )
            Spacer(Modifier.weight(1f))
            Text("Настройка поиска", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(Modifier.weight(1f))
        }

        Spacer(Modifier.height(12.dp))
        Text("Магазины", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 12.dp)
        ) {
            categories.forEach { cat ->
                stickyHeader {
                    CategoryHeader(
                        title = cat.title,
                        checked = cat.enabled,
                        onToggle = { homeScreenViewModel.toggleCategory(cat.id, it) }
                    )
                }

                items(cat.items, key = { it.id }) { item ->
                    ItemRow(
                        item = item,
                        onToggle = { homeScreenViewModel.toggleItem(cat.id, item.id, it) }
                    )
                    Divider(thickness = 1.dp, color = Color(0xFF282B34))
                }
            }
        }
    }
}

@Composable
private fun CategoryHeader(
    title: String,
    checked: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0063FF), shape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(Modifier.weight(1f))
        Switch(
            checked = checked,
            onCheckedChange = onToggle,
            colors = customSwitchColors()
        )
    }
}

/* ---------- Строка магазина ---------- */
@Composable
private fun ItemRow(
    item: FilterItem,
    onToggle: (Boolean) -> Unit          // ← убрали enabled
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = item.iconUrl,
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
        )
        Spacer(Modifier.width(12.dp))

        Text(
            text = item.title,
            fontSize = 16.sp,
            color = if (item.enabled) Color.White else Color(0xFF60646E),
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.weight(1f))

        Switch(
            checked = item.enabled,
            onCheckedChange = onToggle,
            colors = customSwitchColors()
        )
    }
}


/* ---------- Единые цвета свитча ---------- */
@Composable
private fun customSwitchColors() = SwitchDefaults.colors(
    checkedThumbColor   = Color.White,
    checkedTrackColor   = Color(0xFF0063FF),
    uncheckedThumbColor = Color.White,
    uncheckedTrackColor = Color(0xFF5C5F66),
    uncheckedBorderColor = Color.White,
    checkedBorderColor   = Color.White
)

