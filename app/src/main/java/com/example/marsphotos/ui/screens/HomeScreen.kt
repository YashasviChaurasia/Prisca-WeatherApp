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

import android.annotation.SuppressLint
import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.marsphotos.R
import com.example.marsphotos.ui.theme.MarsPhotosTheme

import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import java.util.Calendar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp
import com.example.marsphotos.network.MarsPhoto
import java.text.SimpleDateFormat
import java.util.Locale
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MarsViewModel,
//    marsUiState: MarsUiState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val selectedDate = remember { mutableStateOf(Calendar.getInstance()) }

        var text by rememberSaveable { mutableStateOf("") }
        var flag by remember {
            mutableIntStateOf(0)
        }
        TextField(
            value = text,
            onValueChange = { value ->
                text = value
                viewModel.plocation=value // Update the location in the ViewModel
                flag=0
            },maxLines = 1,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            label = { Text("Location") },
            leadingIcon = { Icon(Icons.Filled.LocationOn, contentDescription = "Localized description") },
        )

//        Column(modifier = Modifier.padding(16.dp),horizontalAlignment = Alignment.CenterHorizontally) {
//            val state = rememberDatePickerState(initialDisplayMode = DisplayMode.Input,)
//            DatePicker(state = state, modifier = Modifier.padding(16.dp))
//
//            DisposableEffect(state.selectedDateMillis) {
//                onDispose {
//                    // Ensure selectedDateMillis isn't null to avoid NPE
//                    if (state.selectedDateMillis != null) {
//                        // Convert selectedDateMillis to Calendar
//                        val selectedCalendar = Calendar.getInstance().apply {
//                            timeInMillis = state.selectedDateMillis!!
//                        }
//                        // Update ViewModel's selected date
//                        viewModel.updateSelectedDate(selectedCalendar)
//                    }
//                }
//            }
//
//            // Display selected date in a text field
//            TextField(
//                value = selectedDate.value.time.toString(), // Convert date to string
//                onValueChange = {},
//                maxLines = 1,
//                label = { Text("Selected Date") },
//                leadingIcon = { Icon(Icons.Filled.DateRange, contentDescription = "Date Icon") },
//                readOnly = true // Make the text field read-only
//            )
//        }
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val state = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)
            val currentDate = remember { Calendar.getInstance() }
            val selectedDate = remember { mutableStateOf(currentDate) }

            // Date picker with manual validation
            DatePicker(
                state = state,
                modifier = Modifier.padding(16.dp)
            )

            DisposableEffect(state.selectedDateMillis) {
                onDispose {
                    // Ensure selectedDateMillis isn't null to avoid NPE
                    if (state.selectedDateMillis != null) {
                        // Convert selectedDateMillis to Calendar
                        val selectedCalendar = Calendar.getInstance().apply {
                            timeInMillis = state.selectedDateMillis!!
                        }
                        // If future date is selected, store the current date instead
                        if (selectedCalendar > currentDate) {
                            selectedDate.value = currentDate.clone() as Calendar
                            viewModel.updateSelectedDate(currentDate)
                        } else {
                            selectedDate.value = selectedCalendar
                            viewModel.updateSelectedDate(selectedCalendar)
                        }
                    }
                }
            }

            // Display selected date in a text field
            TextField(
                value = selectedDate.value.time.toString(), // Convert date to string
                onValueChange = {},
                maxLines = 1,
                label = { Text("Selected Date") },
                leadingIcon = { Icon(Icons.Filled.DateRange, contentDescription = "Date Icon") },
                readOnly = true // Make the text field read-only
            )
            Text(text = "Selected Date: ${selectedDate.value.time}")
        }


        Button(
            onClick = {
                viewModel.getMarsPhotos()
                flag=1
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Get Mars Photos")
        }

        if(flag==1){
            when(viewModel.marsUiState) {
                MarsUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxWidth())
                MarsUiState.Error -> ErrorScreen(modifier = modifier.fillMaxWidth())
                is MarsUiState.Success -> ResultScreen(vmodel =viewModel, modifier = modifier.fillMaxWidth())
                else -> {}
            }
//            ResultScreen(vmodel =viewModel, modifier = modifier.fillMaxWidth())
        }



    }

}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_broken_image), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
    }
}
/**
 * ResultScreen displaying number of photos retrieved.
 */
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun ResultScreen(vmodel: MarsViewModel, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val (maxTemp, minTemp) = try {
            vmodel.listResult?.locations?.get(vmodel.getlocation())?.values?.firstOrNull()?.let { it.maxt to it.mint }
        } catch (e: HttpException) {
            // Handle the exception (e.g., log the error)

            null // Return null if an exception occurs
        } ?: (Double.NaN to Double.NaN)

        Column(modifier = Modifier.padding(20.dp)) {
            var eflag by remember {
                mutableStateOf(0)
            }
            Text(
                text = "Max Temp: ${if (maxTemp.isNaN()) {
                    eflag = 1 // Set eflag to 1 if maxTemp is null or NaN
                    "N/A"
                } else {
                    eflag = 0 // Set eflag back to 0 if maxTemp is not null or NaN
                    maxTemp
                }}°C",
                style = TextStyle(fontSize = 20.sp), // Increase font size for Max Temp
            )
            Text(
                text = "Min Temp: ${if (minTemp.isNaN()) {
                    eflag = 1 // Set eflag to 1 if minTemp is null or NaN
                    "N/A"
                } else {
                    eflag = 0 // Set eflag back to 0 if minTemp is not null or NaN
                    minTemp
                }}°C",
                style = TextStyle(fontSize = 20.sp), // Increase font size for Min Temp
            )
//            if(eflag == 1) {
//                Text("Error: Unable to retrieve temperature data")
//                ErrorScreen()
//            }
        }


    }
}

