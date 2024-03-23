package by.siarhiejbahdaniec.telepartacyja

import by.siarhiejbahdaniec.telepartacyja.api.TelepartacyjaApi
import by.siarhiejbahdaniec.telepartacyja.api.TelepartacyjaApiImpl
import by.siarhiejbahdaniec.telepartacyja.logic.SpawnCommandExecutor
import by.siarhiejbahdaniec.telepartacyja.config.ConfigHolder
import by.siarhiejbahdaniec.telepartacyja.logic.SpawnEventListener
import by.siarhiejbahdaniec.telepartacyja.logic.TeleportExecutor
import by.siarhiejbahdaniec.telepartacyja.repo.TeleportRepository
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.plugin.ServicePriority
import org.bukkit.plugin.java.JavaPlugin

class Telepartacyja : JavaPlugin(), ConfigHolder {

    override fun onEnable() {
        initConfig()

        val repository = TeleportRepository(dataFolder)
        val teleportExecutor = TeleportExecutor(
            teleportRepository = repository,
            configHolder = this,
            plugin = this,
        )

        requireNotNull(getCommand("spawn")) {
            "spawn command must be not null!"
        }.setExecutor(
            SpawnCommandExecutor(
                configHolder = this,
                teleportExecutor = teleportExecutor
            )
        )

        with(Bukkit.getPluginManager()) {
            val plugin = this@Telepartacyja
            registerEvents(
                SpawnEventListener(plugin),
                plugin
            )
            registerEvents(
                teleportExecutor,
                plugin
            )
        }

        Bukkit.getServicesManager()
            .register(
                TelepartacyjaApi::class.java,
                TelepartacyjaApiImpl(teleportExecutor),
                this,
                ServicePriority.Normal
            )
    }

    private fun initConfig() {
        saveDefaultConfig()
        config.options().copyDefaults(true)
        saveConfig()
        reloadConfig()
    }

    override fun getString(key: String): String {
        return config.getString(key).orEmpty()
    }

    override fun getDouble(key: String): Double {
        return config.getDouble(key, 0.0)
    }

    override fun getInt(key: String): Int {
        return config.getInt(key, 0)
    }

    override fun getLocation(key: String): Location? {
        return config.getLocation(key)
    }

    override fun setLocation(key: String, location: Location) {
        config.set(key, location)
        saveConfig()
    }

    override fun reloadConfigFromDisk() {
        reloadConfig()
    }
}