/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.marsphotos.ui.screens

import android.net.http.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.marsphotos.MarsPhotosApplication
import com.example.marsphotos.data.Item
import com.example.marsphotos.data.ItemsRepository
import com.example.marsphotos.data.MarsPhotosRepository
import com.example.marsphotos.network.MarsPhoto
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

sealed interface MarsUiState {
    data class Success(val photos: String) : MarsUiState
    object Error : MarsUiState
    object Loading : MarsUiState
}
data class HomeUiState(val itemList: List<Item> = listOf())
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
class MarsViewModel(private val marsPhotosRepository: MarsPhotosRepository) : ViewModel() {
    /** The mutable State that stores the status of the most recent request */


    var itemUiState by mutableStateOf(ItemUiState())
        private set

    /**
     * Updates the [itemUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(itemDetails: ItemDetails) {
        itemUiState =
            ItemUiState(itemDetails = itemDetails, isEntryValid = validateInput(itemDetails))
    }

    private fun validateInput(uiState: ItemDetails = itemUiState.itemDetails): Boolean {
        return with(uiState) {
            city.isNotBlank() && date.isNotBlank()
        }
    }
    suspend fun getAverageMarsPhotos() {
        // Get the current year
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)

        // Store the initial selected date
        val initialDate = selectedDate.clone() as Calendar

        // Initialize variables to store total mint and maxt
        var totalMinT = 0.0
        var totalMaxT = 0.0

        // Iterate over the past 10 years, keeping day and month the same
        for (i in 1..10) {
            // Update the selected date to the initial date with year changed
            selectedDate = initialDate.clone() as Calendar
            selectedDate.set(Calendar.YEAR, currentYear - i)

            // Make API call to get Mars photos
            getMarsPhotos()
            delay(50)
            // If listResult is not null and contains valid data
            listResult?.let { marsPhoto ->
                marsPhoto.locations[plocation]?.values?.firstOrNull()?.let { values ->
                    // Add the mint and maxt to total
                    totalMinT += values.mint
                    totalMaxT += values.maxt
                }
            }
        }

        // Calculate the average mint and maxt
        val averageMinT = totalMinT / 10
        val averageMaxT = totalMaxT / 10

        // Update mutable state variables
        minimumt = averageMinT
        maximumt = averageMaxT
        delay(5000)
        saveItem()
    }


    fun packup2(): ItemDetails {

        return ItemDetails(
            city = plocation,
            date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate.time),
            maxt = maximumt.toString(),
            mint = minimumt.toString()
        )
    }
    fun packup(): ItemDetails? {
        val (maxTemp, minTemp) = try {
            listResult?.locations?.get(getlocation())?.values?.firstOrNull()?.let { it.maxt to it.mint }
        } catch (e: Exception) {
            null to null
        } ?: return null
        if (maxTemp != null) {
            maximumt= maxTemp
        }
        if (minTemp != null) {
            minimumt= minTemp
        }
        return ItemDetails(
            city = plocation,
            date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate.time),
            maxt = maxTemp.toString(),
            mint = minTemp.toString()
        )
    }

    suspend fun saveItem() {
        if (validateInput()) {
            marsPhotosRepository.insertItem(itemUiState.itemDetails.toItem())

        }
    }





    fun getStuff() {
//        return marsPhotosRepository.getItem(plocation, SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate.time))


    }





    var pastyear:Int by mutableIntStateOf(0)
    var ef:Int by mutableIntStateOf(0)
    var maximumt:Double by mutableDoubleStateOf(0.0)
    var minimumt:Double by mutableDoubleStateOf(0.0)
    var marsUiState: MarsUiState by mutableStateOf(MarsUiState.Success(""))
        private set

    var selectedDate: Calendar by mutableStateOf(Calendar.getInstance().apply {
        // Set the selected date to a past time (e.g., one year ago)
        add(Calendar.YEAR, -1)
    })
    var plocation: String by mutableStateOf("New York")
    var listResult: MarsPhoto? by mutableStateOf(null)
    /**
     * Call getMarsPhotos() on init so we can display status immediately.
     */
    init {
        getMarsPhotos()

//            .onEach { homeUiState.value = it }
    }

//    val homeUiState: StateFlow<HomeUiState> =
//        marsPhotosRepository.getItemStream(city = plocation,
//            date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate.time)).map { HomeUiState(it) }
//            .stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(5_000L),
//                initialValue = HomeUiState()
//            )
var homeUiState: StateFlow<HomeUiState> = MutableStateFlow(HomeUiState())
fun homeui() {
    homeUiState= marsPhotosRepository.getItemStream(
        city = plocation,
        date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate.time)
    ).map { HomeUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = HomeUiState()
        )
}
     // Update the value of homeUiState


    /**
     * Gets Mars photos information from the Mars API Retrofit service and updates the
     * [MarsPhoto] [List] [MutableList].
     */
    fun updateSelectedDate(calendar: Calendar) {
        selectedDate = calendar
    }
    fun updatelocation(loc:String) {
        plocation = loc
    }
    fun getlocation():String {
        return plocation
    }


    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun getMarsPhotos() {
//        if (plocation.isEmpty() || marsUiState is MarsUiState.Success) return

        viewModelScope.launch {
            ef=0
            marsUiState = MarsUiState.Loading
            var location = plocation // Use the location set in the ViewModel
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val startDate = dateFormat.format(selectedDate.time).toString()

//                val startDate = "2019-06-13T00:00:00"
//                Log.d("MarsViewModel", "getMarsPhotos: $sDate")
//                val endDate = "2019-06-13T23:59:59"
            //repository is given to viewmodel
//                listResult = MarsApi.retrofitService.getPhotos(location, startDate)//, endDate)
            marsUiState = try {
                listResult = marsPhotosRepository.getMarsPhotos(location, startDate)
                MarsUiState.Success("Success")
            }
            catch (e: IOException) {
                ef=4//Internet connection,
                MarsUiState.Error
            } catch (e: HttpException) {
                ef=1
                MarsUiState.Error
            } catch (e: NullPointerException) {
                ef=2
                MarsUiState.Error
            }
            catch (e: Exception) {
                ef=3//wrong location //future date//mostly this error
                MarsUiState.Error
            }

        }

    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MarsPhotosApplication)
                val marsPhotosRepository = application.container.marsPhotosRepository
                MarsViewModel(marsPhotosRepository = marsPhotosRepository)
            }
//            initializer {
////                ItemEntryViewModel(inventoryApplication().container.itemsRepository)
//                val application = (this[APPLICATION_KEY] as MarsPhotosApplication)
//                ItemEntryViewModel(inventoryApplication().container.itemsRepository)
//            }
        }
    }

}

data class ItemUiState(
    val itemDetails: ItemDetails = ItemDetails(),
    val isEntryValid: Boolean = false
)

data class ItemDetails(
    val id: Int = 0,
    var city: String = "",
    var date: String = "",
    val maxt: String = "",
    val mint: String = ""
)

/**
 * Extension function to convert [ItemDetails] to [Item]. If the value of [ItemDetails.price] is
 * not a valid [Double], then the price will be set to 0.0. Similarly if the value of
 * [ItemDetails.quantity] is not a valid [Int], then the quantity will be set to 0
 */
fun ItemDetails.toItem(): Item = Item(
    id = id,
    city = city,
    date = date,
    maxt = maxt.toDoubleOrNull() ?: 0.0,
    mint = mint.toDoubleOrNull() ?: 0.0
)

//fun Item.formatedPrice(): String {
//    return NumberFormat.getCurrencyInstance().format(price)
//}

/**
 * Extension function to convert [Item] to [ItemUiState]
 */
fun Item.toItemUiState(isEntryValid: Boolean = false): ItemUiState = ItemUiState(
    itemDetails = this.toItemDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Item] to [ItemDetails]
 */
fun Item.toItemDetails(): ItemDetails = ItemDetails(
    id = id,
    city = city,
    date = date,
    maxt = maxt.toString(),
    mint = mint.toString()
)



//fun CreationExtras.inventoryApplication(): MarsPhotosApplication =
//    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MarsPhotosApplication)
