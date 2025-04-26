package com.example.productfinder.presentation.homescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.productfinder.presentation.homescreen.viewmodel.HomeScreenViewModel

@Composable
fun FilterBottomSheet(
    homeScreenViewModel: HomeScreenViewModel,
    onDismissRequest: () -> Unit
) {
    val currentFilters by homeScreenViewModel.filters.collectAsState()

    fun isMarketplaceChecked(marketplace: String): Boolean {
        return currentFilters.marketplaces.contains(marketplace)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondary)
            .navigationBarsPadding()
            .padding(horizontal = 16.dp)
            .heightIn(min = 200.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Ozon",
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.W700
            )
            Switch(
                checked = isMarketplaceChecked("Ozon"),
                onCheckedChange = { isChecked ->
                    homeScreenViewModel.toggleMarketplace("Ozon", isChecked)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xffDA6600),
                    checkedBorderColor = Color(0xffDA6600),
                    uncheckedThumbColor = Color.Gray,
                    uncheckedTrackColor = Color.LightGray,
                    uncheckedBorderColor = Color.Gray
                )
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Kaspi",
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.W700
            )
            Switch(
                checked = isMarketplaceChecked("Kaspi"),
                onCheckedChange = { isChecked ->
                    homeScreenViewModel.toggleMarketplace("Kaspi", isChecked)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xffDA6600),
                    checkedBorderColor = Color(0xffDA6600),
                    uncheckedThumbColor = Color.Gray,
                    uncheckedTrackColor = Color.LightGray,
                    uncheckedBorderColor = Color.Gray
                )
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Satu.kz",
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.W700
            )
            Switch(
                checked = isMarketplaceChecked("Satu"),
                onCheckedChange = { isChecked ->
                    homeScreenViewModel.toggleMarketplace("Satu", isChecked)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xffDA6600),
                    checkedBorderColor = Color(0xffDA6600),
                    uncheckedThumbColor = Color.Gray,
                    uncheckedTrackColor = Color.LightGray,
                    uncheckedBorderColor = Color.Gray
                )
            )
        }

        // Sulpak
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Sulpak",
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.W700
            )
            Switch(
                checked = isMarketplaceChecked("Sulpak"),
                onCheckedChange = { isChecked ->
                    homeScreenViewModel.toggleMarketplace("Sulpak", isChecked)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xffDA6600),
                    checkedBorderColor = Color(0xffDA6600),
                    uncheckedThumbColor = Color.Gray,
                    uncheckedTrackColor = Color.LightGray,
                    uncheckedBorderColor = Color.Gray
                )
            )
        }
    }
}
