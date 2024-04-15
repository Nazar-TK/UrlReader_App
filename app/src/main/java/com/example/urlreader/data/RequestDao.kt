package com.example.urlreader.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RequestDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequest(request: Request)
    @Delete
    suspend fun deleteRequest(request: Request)
    @Query("SELECT * FROM request")
    fun getRequests() : Flow<List<Request>>
}