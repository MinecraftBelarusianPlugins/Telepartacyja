package by.siarhiejbahdaniec.telepartacyja.config

interface ConfigHolder {
    fun getString(key: String): String
    fun getDouble(key: String): Double
    fun reloadConfigFromDisk()
}