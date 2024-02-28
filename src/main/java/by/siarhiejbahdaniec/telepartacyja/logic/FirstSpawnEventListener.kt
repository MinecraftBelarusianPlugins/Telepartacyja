package by.siarhiejbahdaniec.telepartacyja.logic

import by.siarhiejbahdaniec.telepartacyja.config.ConfigHolder
import by.siarhiejbahdaniec.telepartacyja.config.ConfigKeys
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class FirstSpawnEventListener(
    private val configHolder: ConfigHolder,
): Listener {

    @EventHandler
    fun onEvent(event: PlayerJoinEvent) {
        val player = event.player
        if (!player.hasPlayedBefore()) {
            val location = configHolder.getLocation(ConfigKeys.firstSpawn)

            if (location != null) {
                player.teleport(location)
            }
        }
    }
}