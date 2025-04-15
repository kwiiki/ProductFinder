package com.example.productfinder.presentation.itemfoundscreen

import com.example.productfinder.data.Product

sealed class ProductsUiState {
    object Loading : ProductsUiState()
    data class Success(val products: List<Product>) : ProductsUiState()
    data class Error(val message: String) : ProductsUiState()
    object Empty : ProductsUiState()
}
