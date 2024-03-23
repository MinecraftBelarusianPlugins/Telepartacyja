package by.siarhiejbahdaniec.telepartacyja.config

import org.bukkit.Location

interface ConfigHolder {
    fun getString(key: String): String
    fun getDouble(key: String): Double
    fun getInt(key: String): Int
    fun getLocation(key: String): Location?
    fun setLocation(key: String, location: Location)
    fun reloadConfigFromDisk()
}