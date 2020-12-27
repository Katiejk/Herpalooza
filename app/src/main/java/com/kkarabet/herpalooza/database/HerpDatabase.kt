package com.kkarabet.herpalooza.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Herp::class], version = 1)
abstract class HerpDatabase : RoomDatabase() {
    abstract fun herpDao(): HerpDao
}