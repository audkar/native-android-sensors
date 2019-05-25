package app.nameplaceholder.sensorsapp

interface SensorData {
    fun getPrintable(): String
}

data class LocationData(val latitude: Double) : SensorData{
    override fun getPrintable(): String = "Location: $latitude"
}

data class LightData(val light: Float): SensorData{
    override fun getPrintable(): String = "Light: $light"
}

data class BluetoothData(val name: String): SensorData{
    override fun getPrintable(): String = "Bluetooth: $name"
}