package com.example.marsphotos.data

import android.os.Build
import androidx.annotation.RequiresExtension
import com.example.marsphotos.network.MarsApiService
import com.example.marsphotos.network.MarsPhoto

interface MarsPhotosRepository {
    suspend fun getMarsPhotos(location: String, startDate: String): MarsPhoto
}

class NetworkMarsPhotosRepository(
    private val marsApiService: MarsApiService
) : MarsPhotosRepository {

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun getMarsPhotos(location: String, startDate: String): MarsPhoto = marsApiService.getPhotos(location, startDate)
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