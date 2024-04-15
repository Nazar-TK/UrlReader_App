package com.example.urlreader.data

import kotlinx.coroutines.flow.Flow

interface RequestRepository {

    suspend fun insertRequest(request: Request)

    suspend fun deleteRequest(request: Request)

    fun getRequests() : Flow<List<Request>>
}