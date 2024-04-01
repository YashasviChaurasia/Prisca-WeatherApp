package com.example.marsphotos.data

interface ItemsRepository {
    suspend fun insertItem(item: Item)

    fun getItem(city: String, date: String)
//    suspend fun deleteItem(item: Item)
//
//    /**
//     * Update item in the data source
//     */
//    suspend fun updateItem(item: Item)
}