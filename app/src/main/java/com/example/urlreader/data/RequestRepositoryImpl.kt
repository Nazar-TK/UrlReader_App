package com.example.urlreader.data

import kotlinx.coroutines.flow.Flow

class RequestRepositoryImpl(private val dao: RequestDao): RequestRepository {
    override suspend fun insertRequest(request: Request) {
        dao.insertRequest(request)
    }

    override suspend fun deleteRequest(request: Request) {
        dao.deleteRequest(request)
    }

    override fun getRequests(): Flow<List<Request>> {
        return dao.getRequests()
    }
}