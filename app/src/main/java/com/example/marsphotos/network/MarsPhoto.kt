package com.example.marsphotos.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class MarsPhoto(
    val id: String,
    @SerialName(value = "img_src")
    val imgSrc: String
)
//
//@Serializable
//data class MarsPhoto(
//    @SerialName("locations")
//    val locations: Map<String, Location>
//) {
//    @Serializable
//    data class Location(
//        @SerialName("values")
//        val values: List<Value>
//    ) {
//        @Serializable
//        data class Value(
//            @SerialName("temp")
//            val temp: Double,
//            @SerialName("maxt")
//            val maxt: Double,
//            @SerialName("mint")
//            val mint: Double
//        )
//    }
//}


//@Serializable
//data class Value(
//    @SerialName(value = "temp")
//    val temp: Double,
//    @SerialName(value = "maxt")
//    val maxt: Double,
//    @SerialName(value = "mint")
//    val mint: Double
//)
//
//
//@Serializable
//data class Location(
//    @SerialName(value = "values")
//    val values: List<Value>
//)
//
//
//@Serializable
//data class MarsPhoto(
//    @SerialName(value = "locations")
//    val locations: Map<String, Location>
//)



//
//{
//    "locations": {
//        "NewDelhi": {
//            "values": [
//                {
//                    "temp": 35.9,
//                    "maxt": 41.3,
//                    "mint": 30,
//                }
//            ],
//        }
//    }
//}