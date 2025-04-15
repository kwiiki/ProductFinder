package com.example.productfinder.core.di

import android.content.Context
import androidx.room.Room
import com.example.productfinder.data.db.dao.ProductDao
import com.example.productfinder.data.db.database.ProductDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {
    @Provides
    @Singleton
    fun provideProductDatabase(@ApplicationContext context: Context): ProductDatabase {
        return Room.databaseBuilder(
            context,
            ProductDatabase::class.java,
            "product.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideProductDao(productDatabase: ProductDatabase): ProductDao =
        productDatabase.getProductDao()
}