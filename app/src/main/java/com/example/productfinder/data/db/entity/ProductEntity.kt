package com.example.productfinder.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "product")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("title")
    val title: String,
    @SerializedName("link_for_market")
    val linkForMarket: String,
    @SerializedName("image_link")
    val imageLink: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("logo_url")
    val logoUrl: String,
    @SerializedName("rating")
    val rating: Double,
    @SerializedName("free_delivery")
    val freeDelivery: Boolean,
    @SerializedName("source")
    val source: String
)