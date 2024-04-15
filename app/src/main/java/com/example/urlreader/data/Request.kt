package com.example.urlreader.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Request(
    var requestBody: String?,
    var url: String?,
    @PrimaryKey val id: Int? = null
    )
