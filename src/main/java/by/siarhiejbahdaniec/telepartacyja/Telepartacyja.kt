package by.siarhiejbahdaniec.telepartacyja

import by.siarhiejbahdaniec.telepartacyja.config.ConfigHolder
import org.bukkit.Location
import org.bukkit.plugin.java.JavaPlugin

class Telepartacyja : JavaPlugin(), ConfigHolder {

    override fun onEnable() {
        setupConfig()
    }

    private fun setupConfig() {
        saveDefaultConfig()
        getConfig().options().copyDefaults(true)
        saveConfig()
        reloadConfig()
    }

    override fun getString(key: String): String {
        return config.getString(key).orEmpty()
    }

    override fun getDouble(key: String): Double {
        return config.getDouble(key, 0.0)
    }

    override fun getLocation(key: String): Location? {
        return config.getLocation(key)
    }

    override fun reloadConfigFromDisk() {
        reloadConfig()
    }
}