package app.nameplaceholder.sensorsapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.switchMap
import kotlinx.coroutines.launch

const val NOTIFICATION_ID = 1

@UseExperimental(FlowPreview::class)
fun bindNotificationState(context: Context) {
    GlobalScope.launch {
        ForegroundService.isActive.switchMap { isActive ->
            if (isActive) {
                App.instance.repository.track()
            } else {
                App.instance.repository.stop()
                emptyFlow()
            }
        }.collect {
            context.getSystemService<NotificationManager>()!!.notify(
                NOTIFICATION_ID,
                buildNotification(context, it)
            )
        }
    }
}

fun buildNotification(context: Context, sensorData: List<SensorData>? = null): Notification {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            "foreground",
            "Foreground service",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            setSound(null, null)
        }
        context.getSystemService<NotificationManager>()!!.createNotificationChannel(channel)
    }
    return NotificationCompat.Builder(context, "foreground")
        .setContentTitle("Tracking active...")
        .setSmallIcon(R.drawable.ic_android_black_24dp)
        .setStyle(NotificationCompat.BigTextStyle().bigText(sensorData?.formatNotificationContent() ?: ""))
        .setContentIntent(
            PendingIntent.getActivity(
                context,
                0,
                Intent(context, MainActivity::class.java),
                0
            )
        )
        .build()
}

private fun List<SensorData>.formatNotificationContent(): String {
    return StringBuilder().also { builder ->
        findLast { it is LocationData }?.run { builder.appendln(this.getPrintable()) }
        findLast { it is LightData }?.run { builder.appendln(this.getPrintable()) }
        findLast { it is BluetoothData }?.run { builder.appendln(this.getPrintable()) }
    }.toString()
}
