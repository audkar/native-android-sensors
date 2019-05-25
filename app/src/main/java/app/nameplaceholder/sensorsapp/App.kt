package app.nameplaceholder.sensorsapp

import android.app.Application

class App : Application() {

    val repository: DataRepository by lazy { DataRepository(this) }

    override fun onCreate() {
        super.onCreate()
        instance = this
        bindNotificationState(this)
    }

    companion object {
        lateinit var instance: App
    }
}