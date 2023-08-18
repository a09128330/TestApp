package com.rounak.testapp.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tbl_favourite_feeds")
data class FavouriteFeed(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Long=0,

    @ColumnInfo(name = "album_id") val albumId: Long,

    @ColumnInfo(name = "title") val title: String,

    @ColumnInfo(name = "url") val url: String,

    @ColumnInfo(name = "thumbnail_url") val thumbnailUrl: String,

    @ColumnInfo(name = "is_favourite") val isFavourite: Boolean

)
