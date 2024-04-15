package com.example.urlreader.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Request(
    var requestBody: String?,
    var url: String?,
    val date: LocalDateTime,
    @PrimaryKey val id: Int? = null
    ): Parcelable {
    val createdDateFormatted : String
        get() =date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss"))
    }
