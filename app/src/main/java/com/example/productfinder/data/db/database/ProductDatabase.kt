package com.example.productfinder.data.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.productfinder.data.db.dao.ProductDao
import com.example.productfinder.data.db.entity.ProductEntity

@Database(version = 1, entities = [ProductEntity::class], exportSchema = false)
abstract class ProductDatabase(): RoomDatabase(){
    abstract fun getProductDao(): ProductDao
}