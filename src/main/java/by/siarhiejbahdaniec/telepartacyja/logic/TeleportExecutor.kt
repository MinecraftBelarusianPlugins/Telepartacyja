package by.siarhiejbahdaniec.telepartacyja.logic

import by.siarhiejbahdaniec.telepartacyja.config.ConfigHolder
import by.siarhiejbahdaniec.telepartacyja.config.ConfigKeys
import by.siarhiejbahdaniec.telepartacyja.repo.SpawnRepository
import by.siarhiejbahdaniec.telepartacyja.utils.setMessageWithColors
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class TeleportExecutor(
    private val spawnRepository: SpawnRepository,
    private val configHolder: ConfigHolder,
) : Listener {

    companion object {
        private val PARTICLES = Particle.PORTAL
        private const val PARTICLES_AMOUNT = 40
    }

    fun execute(player: Player, location: Location) {
        val lastTime = spawnRepository.getPlayerLastTeleportTime(player.uniqueId)
        val cooldown = configHolder.getInt(ConfigKeys.Teleport.cooldown)
            .seconds
            .inWholeMilliseconds

        val difference = System.currentTimeMillis() - lastTime
        if (difference < cooldown) {
            player.setMessageWithColors(
                message = configHolder.getString(ConfigKeys.Messages.cooldownLeft)
                    .format(difference.milliseconds.inWholeSeconds)
            )
            return
        }

        val delay = configHolder.getInt(ConfigKeys.Teleport.delay)
        if (delay <= 0) {
            teleportPlayer(player, location)
            return
        }
    }

    private fun teleportPlayer(player: Player, location: Location) {
        with(player) {
            teleport(location)
            spawnParticle(PARTICLES, location, PARTICLES_AMOUNT)

            getNearbyEntities(16.0, 16.0, 16.0)
                .filter { it is Player && it.canSee(player) }
                .forEach {
                    (it as Player).spawnParticle(PARTICLES, location, PARTICLES_AMOUNT)
                }
        }
    }

    @EventHandler
    fun onMove(event: PlayerMoveEvent) {

    }

    @EventHandler
    fun onLeave(event: PlayerQuitEvent) {

    }
}