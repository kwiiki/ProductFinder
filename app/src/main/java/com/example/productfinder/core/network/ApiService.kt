package com.example.productfinder.core.network

import com.example.productfinder.data.Filters
import com.example.productfinder.data.Product
import com.example.productfinder.data.SearchByTextRequest
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
        @Part data: MultipartBody.Part
    ): List<Product>

    @POST("search/text")
    suspend fun searchByText(
        @Body searchByTextRequest:SearchByTextRequest
    ): List<Product>
}
