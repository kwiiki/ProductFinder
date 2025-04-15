package com.example.productfinder.core.network

import com.example.productfinder.data.Filters
import com.example.productfinder.data.Product
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @POST("search/image")
    @Multipart
    suspend fun upload(
        @Part image: MultipartBody.Part,
    ): List<Product>

    @GET("search/text")
    suspend fun searchByText(
        @Query("text") text: String,
        @Body filters:Filters
    ): List<Product>
}
