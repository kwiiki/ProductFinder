package com.example.productfinder.data

data class SearchByTextRequest(
    val filters: Filters,
    val text: String
)
