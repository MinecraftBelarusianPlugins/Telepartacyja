package by.siarhiejbahdaniec.telepartacyja.config

import org.bukkit.Location

interface ConfigHolder {
    fun getString(key: String): String
    fun getDouble(key: String): Double
    fun getLocation(key: String): Location?
    fun reloadConfigFromDisk()
}