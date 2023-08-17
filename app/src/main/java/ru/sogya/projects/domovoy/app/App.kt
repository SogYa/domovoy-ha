package ru.sogya.projects.domovoy.app

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    companion object {
        private lateinit var app: App

        fun getAppContext(): Context {
            return app.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        app = this
    }
}