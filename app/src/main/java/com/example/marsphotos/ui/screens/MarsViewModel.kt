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
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marsphotos.network.MarsApi
import com.example.marsphotos.network.MarsPhoto
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Calendar
//sealed interface MarsUiState {
//    data class Success(val photos: String) : MarsUiState
//    object Error : MarsUiState
//    object Loading : MarsUiState
//}
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
class MarsViewModel : ViewModel() {
    /** The mutable State that stores the status of the most recent request */
//    var marsUiState: MarsUiState by mutableStateOf(MarsUiState.Loading)
//        private set

    var selectedDate: Calendar = Calendar.getInstance()
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
        try {
            viewModelScope.launch {

                var location = plocation // Use the location set in the ViewModel
                val startDate = "2019-06-13T00:00:00"
                val endDate = "2019-06-13T23:59:59"
                listResult = MarsApi.retrofitService.getPhotos(location, startDate, endDate)

            }
        }
        catch (e: IOException) {
//                marsUiState = MarsUiState.Error
        } catch (e: HttpException) {
//                marsUiState = MarsUiState.Error
        } catch (e: NullPointerException) {
//                marsUiState = MarsUiState.Error
        }
    }
}
