package app.nameplaceholder.sensorsapp

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

@UseExperimental(ExperimentalCoroutinesApi::class, FlowPreview::class)
class ForegroundService : Service() {

    override fun onCreate() {
        super.onCreate()
        startForeground(NOTIFICATION_ID, buildNotification(this))
        isActiveChannel.offer(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        isActiveChannel.offer(false)
        stopForeground(true)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        private val isActiveChannel = ConflatedBroadcastChannel(false)
        val isActive: Flow<Boolean>
            get() = isActiveChannel.asFlow()
    }
}