package com.example.marsphotos.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Query("SELECT * FROM item WHERE city =:city AND date =:date")
    fun getItem(city:String,date:String):Flow<List<Item>>
//    @Update
//    suspend fun update(item: Item)
//
//    @Delete
//    suspend fun delete(item: Item)

}

