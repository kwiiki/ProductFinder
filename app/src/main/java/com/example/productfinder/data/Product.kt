package com.example.productfinder.data

import com.google.gson.annotations.SerializedName

data class Product(
    val title:String? = "qw",
    val link:String? = "qw",
    @SerializedName("image_link")
    val imageLink :String? = "",
    val price:String? = null,
    @SerializedName("logo_url")
    val logoUrl:String? = "пустой",
    val rating: Double,
    val freeDelivery: Boolean,
    val source:String? = "qw"
)
