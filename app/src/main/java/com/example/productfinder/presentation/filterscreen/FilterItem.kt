package com.example.productfinder.presentation.filterscreen

data class FilterItem(
    val id: String,               // "ozon", "yandex_market"
    val title: String,
    val iconUrl: String,
    var enabled: Boolean = true   // меняется в рантайме
)