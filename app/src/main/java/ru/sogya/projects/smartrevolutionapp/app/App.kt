package ru.sogya.projects.smartrevolutionapp.app

import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp
import com.sogya.data.repository.FirebaseRepositoryImpl
import com.sogya.data.repository.LocalDataBaseRepositoryImpl
import com.sogya.data.repository.NetworkRepositoryImpl
import com.sogya.data.repository.WebSocketRepositoryImpl
import com.sogya.domain.repository.FirebaseRepository
import com.sogya.domain.repository.LocalDataBaseRepository
import com.sogya.domain.repository.NetworkRepository
import com.sogya.domain.repository.WebSocketRepository

class App : Application() {


    companion object {
        private lateinit var app: App
        private lateinit var repository: LocalDataBaseRepository
        private lateinit var firebaseRepository: FirebaseRepository
        private lateinit var webSocketRepository: WebSocketRepository
        private lateinit var networkRepository: NetworkRepository

        fun getApplicationContext(): Context {
            return app.applicationContext
        }

        fun getRoom(): LocalDataBaseRepository {
            return repository
        }

        fun getFirebase(): FirebaseRepository {
            return firebaseRepository
        }

        fun getWebSocketRepository(): WebSocketRepository {
            return webSocketRepository
        }
        fun getNetworkRepository():NetworkRepository{
            return networkRepository
        }
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        FirebaseApp.initializeApp(app.applicationContext)
        repository = LocalDataBaseRepositoryImpl(app.applicationContext)
        firebaseRepository = FirebaseRepositoryImpl()
        webSocketRepository = WebSocketRepositoryImpl()
        networkRepository = NetworkRepositoryImpl()

    }
}