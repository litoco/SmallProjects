package com.example.migrateittocompose.repository.localstorage

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RoomDAO {
    @Query("SELECT userId FROM Userid")
    fun getUserId(): LiveData<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserId(userId: UserId)

}