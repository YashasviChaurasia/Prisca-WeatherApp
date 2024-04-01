package com.example.marsphotos.data

import kotlinx.coroutines.flow.Flow

class OfflineItemsRepository(private val itemDao: ItemDao) : ItemsRepository {


    override suspend fun insertItem(item: Item) = itemDao.insert(item)

//    override suspend fun deleteItem(item: Item) = itemDao.delete(item)
//
//    override suspend fun updateItem(item: Item) = itemDao.update(item)
}