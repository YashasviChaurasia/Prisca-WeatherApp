package com.example.marsphotos.data

import android.os.Build
import androidx.annotation.RequiresExtension
import com.example.marsphotos.network.MarsApiService
import com.example.marsphotos.network.MarsPhoto

//interface ItemsRepository {
//    suspend fun insertItem(item: Item)
////    suspend fun deleteItem(item: Item)
////
////    /**
////     * Update item in the data source
////     */
////    suspend fun updateItem(item: Item)
//}

interface MarsPhotosRepository {
    suspend fun getMarsPhotos(location: String, startDate: String): MarsPhoto
    suspend fun insertItem(item: Item)



    fun getItem(city: String, date: String)
}

class NetworkMarsPhotosRepository(
    private val marsApiService: MarsApiService,
    private val itemDao: ItemDao
) : MarsPhotosRepository {

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun getMarsPhotos(location: String, startDate: String): MarsPhoto = marsApiService.getPhotos(location, startDate)
    override suspend fun insertItem(item: Item) = itemDao.insert(item)

    override fun getItem(city: String, date: String) {
        itemDao.getItem(city, date)
    }

}

//class NetworkMarsPhotosRepository(private val marsApiService: MarsApiService) : MarsPhotosRepository {
//
//
//    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
//    var location = viewModel.plocation // Use the location set in the ViewModel
//    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
//    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
//    val startDate = dateFormat.format(viewModel.selectedDate.time).toString()
//
//    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
//    override suspend fun getMarsPhotos(): MarsPhoto = marsApiService.getPhotos(location, startDate)
//}