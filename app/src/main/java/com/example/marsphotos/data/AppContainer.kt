package com.example.marsphotos.data

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.marsphotos.network.MarsApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface AppContainer {
    val marsPhotosRepository: MarsPhotosRepository
    val itemsRepository: ItemsRepository
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
class DefaultAppContainer(private val context: Context) : AppContainer {


    private val BASE_URL = "https://weather.visualcrossing.com"
    private val API_KEY = "4GNLVPUCTARWJEFN87MKFDRLZ"


    private val json = Json { ignoreUnknownKeys = true }

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()


    private val retrofitService : MarsApiService by lazy {
            retrofit.create(MarsApiService::class.java)
    }

    override val marsPhotosRepository: MarsPhotosRepository by lazy {

        NetworkMarsPhotosRepository(retrofitService)
    }


        /**
         * Implementation for [ItemsRepository]
         */
    override val itemsRepository: ItemsRepository by lazy {
        OfflineItemsRepository(InventoryDatabase.getDatabase(context).itemDao())
    }

}

