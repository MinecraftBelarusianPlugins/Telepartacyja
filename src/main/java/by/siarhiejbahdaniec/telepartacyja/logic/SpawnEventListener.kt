package by.siarhiejbahdaniec.telepartacyja.logic

import by.siarhiejbahdaniec.telepartacyja.config.ConfigHolder
import by.siarhiejbahdaniec.telepartacyja.config.ConfigKeys
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerRespawnEvent

internal class SpawnEventListener(
    private val configHolder: ConfigHolder,
): Listener {

    @EventHandler
    fun onEvent(event: PlayerJoinEvent) {
        val player = event.player
        if (!player.hasPlayedBefore()) {
            val location = configHolder.getLocation(ConfigKeys.firstSpawn)
                ?: configHolder.getLocation(ConfigKeys.spawn)

            if (location != null) {
                player.teleport(location)
            }
        }
    }

    @EventHandler
    fun onEvent(event: PlayerRespawnEvent) {
        if (event.isBedSpawn || event.isAnchorSpawn) {
            return
        }

        val spawnLocation = configHolder.getLocation(ConfigKeys.spawn)
        if (spawnLocation != null) {
            event.respawnLocation = spawnLocation
        }
    }
}