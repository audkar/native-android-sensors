package app.nameplaceholder.sensorsapp

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.core.content.PermissionChecker.checkSelfPermission
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

@UseExperimental(FlowPreview::class)
class MainActivity : AppCompatActivity(), CoroutineScope {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        launch {
            ForegroundService.isActive.collect { isActive ->
                bindButtonState(isActive)
                if (isActive) {
                    launch {
                        App.instance.repository.track().collect {
                            output.text = it.reversed()
                                .joinToString(separator = "\n") { sensorData -> sensorData.getPrintable() }
                        }
                    }
                } else {
                    App.instance.repository.stop()
                }
            }
        }
    }

    private fun bindButtonState(isActive: Boolean) {
        button.text = if (isActive) {
            "stop service"
        } else {
            "start service"
        }
        button.setOnClickListener {
            output.text = ""
            if (isActive) {
                stopService(Intent(this@MainActivity, ForegroundService::class.java))
            } else {
                if (checkSelfPermission(this, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
                    startTracking()
                } else {
                    requestPermissions(arrayOf(ACCESS_FINE_LOCATION), 1)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startTracking()
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startTracking() {
        if (checkSelfPermission(this, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
            ContextCompat.startForegroundService(this, Intent(this, ForegroundService::class.java))
        } else {
            Log.e(TAG, "Missing permissions")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private val job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main


    companion object {
        private const val TAG = "MainActivity"
    }
}
