package com.example.marsphotos

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.marsphotos.data.AppContainer
import com.example.marsphotos.data.DefaultAppContainer
import com.example.marsphotos.ui.screens.MarsViewModel

class MarsPhotosApplication() : Application() {

    lateinit var container: AppContainer
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate() {
        super.onCreate()

        container = DefaultAppContainer()
}
}