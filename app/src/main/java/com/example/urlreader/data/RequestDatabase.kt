package com.example.urlreader.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Request::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class RequestDatabase: RoomDatabase() {

    abstract val dao: RequestDao
}