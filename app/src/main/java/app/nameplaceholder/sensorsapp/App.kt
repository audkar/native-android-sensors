package app.nameplaceholder.sensorsapp

import android.app.Application
import android.content.Intent
import androidx.core.content.ContextCompat

class App : Application() {

    val repository: DataRepository by lazy { DataRepository(this) }

    override fun onCreate() {
        super.onCreate()
        instance = this
        bindNotificationState(this)
        ContextCompat.startForegroundService(this, Intent(this, ForegroundService::class.java))
    }

    companion object {
        lateinit var instance: App
    }
}