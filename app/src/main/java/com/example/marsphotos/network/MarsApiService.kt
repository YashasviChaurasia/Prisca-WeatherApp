package com.example.marsphotos.network
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL =
//    "https://android-kotlin-fun-mars-server.appspot.com"
    "https://weather.visualcrossing.com"

private const val API_KEY = "4GNLVPUCTARWJEFN87MKFDRLZ"


private val json = Json { ignoreUnknownKeys = true }

private val retrofit = Retrofit.Builder()
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

interface MarsApiService {
//    @GET("photos")
//    suspend fun getPhotos(): List<MarsPhoto>


    @GET("VisualCrossingWebServices/rest/services/weatherdata/history")
    suspend fun getPhotos(
        @Query("location") location: String,
        @Query("startDateTime") startDateTime: String,
//        @Query("endDateTime") endDateTime: String,
        @Query("aggregateHours") aggregateHours: Int = 24,
        @Query("unitGroup") unitGroup: String = "uk",
        @Query("contentType") contentType: String = "json",
        @Query("dayStartTime") dayStartTime: String = "0:0:00",
        @Query("dayEndTime") dayEndTime: String = "0:0:00",
        @Query("key") apiKey: String = API_KEY
    ): MarsPhoto


}
//object MarsApi {
//    val retrofitService : MarsApiService by lazy {
//
//        retrofit.create(MarsApiService::class.java) }
//}