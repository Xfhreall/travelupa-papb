package com.example.travelupa.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.travelupa.data.dao.ImageDao
import com.example.travelupa.data.entity.ImageEntity

@Database(entities = [ImageEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun imageDao(): ImageDao
}
