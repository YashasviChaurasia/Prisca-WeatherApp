package com.example.marsphotos.data

import kotlinx.coroutines.flow.Flow

interface ItemsRepository {
    suspend fun insertItem(item: Item)

    fun getItemStream(city: String, date: String): Flow<List<Item>>
//    suspend fun deleteItem(item: Item)
//
//    /**
//     * Update item in the data source
//     */
//    suspend fun updateItem(item: Item)
}