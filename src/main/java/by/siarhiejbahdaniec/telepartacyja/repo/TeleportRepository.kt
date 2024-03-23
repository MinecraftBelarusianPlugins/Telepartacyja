package by.siarhiejbahdaniec.telepartacyja.repo

import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException
import java.util.*
import java.util.logging.Level

internal class TeleportRepository(dir: File) {

    companion object {
        private const val FILENAME = "teleport.yml"

        private fun getLastTeleportKey(id: UUID): String {
            return "last_teleport.$id"
        }
    }

    private val file by lazy { File(dir, FILENAME) }
    private val configuration by lazy { YamlConfiguration.loadConfiguration(file) }

    fun setPlayerLastTeleportTime(id: UUID, time: Long) {
        configuration.set(getLastTeleportKey(id), time)
        saveData()
    }

    fun getPlayerLastTeleportTime(id: UUID): Long {
        return configuration.getLong(getLastTeleportKey(id), 0)
    }

    private fun saveData() {
        try {
            configuration.save(file)
        } catch (t: IOException) {
            Bukkit.getLogger().log(Level.WARNING, "Failed to save players timestamps for the Telepartacyja to a file")
        }
    }
}