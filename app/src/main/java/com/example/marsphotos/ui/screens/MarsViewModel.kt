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
    suspend fun saveItem() {
        if (validateInput()) {
            marsPhotosRepository.insertItem(itemUiState.itemDetails.toItem())

        }
    }






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
    }


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
                MarsUiState.Error
            } catch (e: HttpException) {
                 MarsUiState.Error
            } catch (e: NullPointerException) {

                MarsUiState.Error
            }
            catch (e: Exception) {
                Log.e("MarsViewModel", "getMarsPhotos: ${e.message}")
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
    val city: String = "",
    val date: String = "",
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
