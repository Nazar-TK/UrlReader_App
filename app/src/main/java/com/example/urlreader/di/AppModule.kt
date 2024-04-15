package com.example.urlreader.di

import android.app.Application
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import com.example.urlreader.data.RequestDatabase
import com.example.urlreader.data.RequestRepository
import com.example.urlreader.data.RequestRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTodoDatabase(app: Application) : RequestDatabase {
        return databaseBuilder(
            app,
            RequestDatabase::class.java,
            "requestDB"
        ).build()
    }

    @Provides
    @Singleton
    fun provideRequestRepository(db: RequestDatabase): RequestRepository {
        return RequestRepositoryImpl(db.dao)
    }
}