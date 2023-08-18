package com.rounak.testapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rounak.testapp.data.db.entities.FavouriteFeed

@Dao
interface AppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateFavouriteFeed(favouriteFeed: FavouriteFeed):Long // suspend is blocking since paused and resumed later-> will be called from coroutine

    @Query("SELECT * FROM  tbl_favourite_feeds WHERE id = :id")
    fun getFavouriteFeed(id:Long): FavouriteFeed?
}