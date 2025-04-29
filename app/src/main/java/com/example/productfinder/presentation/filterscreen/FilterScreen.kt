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
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
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
    val allSwitchState = remember(categories) {
        derivedStateOf { categories.all { it.enabled } }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
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
            Text(
                "Фильтр маркетплейсов",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray, RoundedCornerShape(16.dp))
                .padding(horizontal = 16.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Выделить все",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
            Spacer(Modifier.weight(1f))
            Switch(
                checked = allSwitchState.value,
                onCheckedChange = { checked ->
                    homeScreenViewModel.toggleAllCategories(checked)
                },
                colors = customSwitchColors()
            )
        }

        val listState = rememberLazyListState()
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 12.dp)
        ) {
            categories.forEach { cat ->
                item {
                    CategoryHeader(
                        title = cat.title,
                        checked = cat.enabled,
                        onToggle = { homeScreenViewModel.toggleCategory(cat.id, it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(16.dp))
                    )
                }
                val itemsList = cat.items
                items(itemsList, key = { it.id }) { item ->
                    ItemRow(
                        item = item,
                        onToggle = { homeScreenViewModel.toggleItem(cat.id, item.id, it) }
                    )
                    if (item != itemsList.last()) {
                        Divider(thickness = 1.dp, color = Color(0xFF282B34))
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryHeader(
    title: String,
    checked: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
        Spacer(Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onToggle, colors = customSwitchColors())
    }
}
@Composable
private fun ItemRow(
    item: FilterItem,
    onToggle: (Boolean) -> Unit
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
                .background(color = Color.White)
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
@Composable
private fun customSwitchColors() = SwitchDefaults.colors(
    checkedThumbColor = Color.White,
    checkedTrackColor = Color(0xFFDA6600),
    uncheckedThumbColor = Color.White,
    uncheckedTrackColor = Color(0xFF5C5F66),
    uncheckedBorderColor = Color.White,
    checkedBorderColor = Color.Transparent
)

