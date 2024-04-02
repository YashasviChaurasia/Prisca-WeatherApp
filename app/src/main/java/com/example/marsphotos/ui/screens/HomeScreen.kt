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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.marsphotos.R

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import java.util.Calendar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch


@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@SuppressLint("RememberReturnType")
@Composable
fun HomeScreen(
    viewModel: MarsViewModel,
//    marsUiState: MarsUiState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) { val coroutineScope = rememberCoroutineScope()
    ItemEntryBody(viewModel = viewModel, onSaveClick = {
        coroutineScope.launch {
            viewModel.saveItem()
        }
    }, onItemValueChange = viewModel::updateUiState, itemUiState = viewModel.itemUiState)
}
@OptIn(ExperimentalMaterial3Api::class)
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun ItemEntryBody(viewModel: MarsViewModel,
    onSaveClick: () -> Unit,
    itemUiState: ItemUiState,
    onItemValueChange: (ItemDetails) -> Unit,
    modifier: Modifier = Modifier
) {
//    val ivModel : ItemEntryViewModel = viewModel(factory = MarsViewModel.Factory)
//    val viModel: ItemEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item{

            val selectedDate = remember { mutableStateOf(Calendar.getInstance()) }

            var text by rememberSaveable { mutableStateOf("") }
            var flag by remember {
                mutableIntStateOf(0)
            }

            TextField(
                value = text,
                onValueChange = { value ->
                    text = value
                    viewModel.plocation = value // Update the location in the ViewModel
                    flag = 0
                },
                maxLines = 1,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                label = { Text("Location") },
                leadingIcon = {
                    Icon(
                        Icons.Filled.LocationOn,
                        contentDescription = "Localized description"
                    )
                },
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
                                selectedDate.value = selectedCalendar
                                viewModel.updateSelectedDate(selectedCalendar)
                                viewModel.pastyear = 1
                                //                            selectedDate.value = currentDate.clone() as Calendar
                                //                            viewModel.updateSelectedDate(currentDate)
                            } else {
                                viewModel.pastyear = 0
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
                //            Text(text = "Selected Date: ${selectedDate.value.time}")
            }
            val coroutineScope = rememberCoroutineScope()
            Button(
                onClick = {
                    if (viewModel.pastyear == 0) {
                        viewModel.getMarsPhotos()
                        val itemDetails = viewModel.packup()
                        if (itemDetails != null) {
                            // Call saveItem with the prepared itemDetails
                            viewModel.updateUiState(itemDetails)
                        }
                        onSaveClick()
                    } else {
                        coroutineScope.launch {
                            viewModel.getAverageMarsPhotos()
                        }
                        val itemDetails = viewModel.packup2()
                        viewModel.updateUiState(itemDetails)
                    }
                    viewModel.ef = 0
                    flag = 1
                },
                modifier = Modifier,
            ) {
                Text("Get Mars Photos")
            }
            Text(text = "Past   : ${viewModel.pastyear}")
            if (flag == 1) {
                when (viewModel.marsUiState) {
                    MarsUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxWidth())
                    MarsUiState.Error -> ErrorScreen(viewModel, modifier = modifier.fillMaxWidth())
                    is MarsUiState.Success -> ResultScreen(
                        vmodel = viewModel,
                        modifier = modifier.fillMaxWidth()
                    )

                    else -> {}
                }
                //            ResultScreen(vmodel =viewModel, modifier = modifier.fillMaxWidth())
            }
        }
}
}


@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {

    Column(horizontalAlignment = Alignment.CenterHorizontally){ Text("Loading...",modifier = Modifier.padding(16.dp)
        .wrapContentSize(Alignment.Center)) }
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun ErrorScreen(viewModel: MarsViewModel, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (viewModel.ef == 4) {
            ConnectionErrorScreen(viewModel)
        } else {
            BadRequestScreen()
        }
//        val itemDetails = viewModel.getStuff()
        // Check if itemDetails is not null before accessing its properties

//        if(viewModel.ef==4){
//            Image(
//                modifier = modifier.size(100.dp).padding(16.dp), painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
//            )
//            viewModel.homeui()
//            val homeUiState by viewModel.homeUiState.collectAsState()
//
//            val firstItem = homeUiState.itemList.firstOrNull()
//
//            val mintValue: Double
//            val maxValue: Double
//            if (firstItem != null) {
//                // If the first item is not null, use its mint and maxt values
//                mintValue = firstItem.mint
//                maxValue = firstItem.maxt
//                Text(text = "Data from Local Database")
//                Text(text = "Maximum Temperature: $maxValue °C",style = TextStyle(fontSize = 20.sp))
//                Text(text = "Minimum Temperature: $mintValue °C",style = TextStyle(fontSize = 20.sp))
//            } else {
//                Text(text = "Cannot fetch data from Local Database")
//            }
//            Text(text = "No Network Connection!")
//
//
//        }
//        else{
//            Image(
//                modifier = modifier.size(100.dp).padding(16.dp),painter = painterResource(id = R.drawable.ic_broken_image), contentDescription = ""
//            )
//            Text(text = "Bad Request", modifier = Modifier.padding(16.dp))
////            Text(text = viewModel.ef.toString(), modifier = Modifier.padding(16.dp))
//        }
    }
}


@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun ConnectionErrorScreen(viewModel: MarsViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .size(100.dp)
                .padding(16.dp),
            painter = painterResource(id = R.drawable.ic_connection_error),
            contentDescription = ""
        )
//        viewModel.homeui()
        val homeUiState by viewModel.homeUiState.collectAsState()
        val firstItem = homeUiState.itemList.firstOrNull()
        if (firstItem != null) {
            // If the first item is not null, use its mint and maxt values
            val mintValue = firstItem.mint
            val maxValue = firstItem.maxt
            Text(text = "Data from Local Database")
            Text(text = "Maximum Temperature: $maxValue °C", style = TextStyle(fontSize = 20.sp))
            Text(text = "Minimum Temperature: $mintValue °C", style = TextStyle(fontSize = 20.sp))
        } else {
            Text(text = "Cannot fetch data from Local Database")
        }
        Text(text = "No Network Connection!")
    }
}

@Composable
fun BadRequestScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .size(100.dp)
                .padding(16.dp),
            painter = painterResource(id = R.drawable.ic_broken_image),
            contentDescription = ""
        )
        Text(text = "Bad Request", modifier = Modifier.padding(16.dp))
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
                mutableIntStateOf(0)
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
//            Text(text = "maxTemp: ${vmodel.maximumt},minTemp: ${vmodel.minimumt}")


//            if(eflag == 1) {
//                Text("Error: Unable to retrieve temperature data")
//                ErrorScreen()
//            }
        }


    }
}

