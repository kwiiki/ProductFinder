package com.example.productfinder.presentation.filterscreen

data class FilterCategory(
    val id: String,               // "marketplaces", "electronics"
    val title: String,
    var enabled: Boolean = true,  // состояние свитча у шапки
    val items: List<FilterItem>
)