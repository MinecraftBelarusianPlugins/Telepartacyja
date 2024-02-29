package by.siarhiejbahdaniec.telepartacyja.logic

import by.siarhiejbahdaniec.telepartacyja.config.ConfigHolder
import by.siarhiejbahdaniec.telepartacyja.config.ConfigKeys
import by.siarhiejbahdaniec.telepartacyja.repo.TeleportRepository
import by.siarhiejbahdaniec.telepartacyja.utils.sendMessageWithColors
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import java.util.UUID
import java.util.logging.Level
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class TeleportExecutor(
    private val teleportRepository: TeleportRepository,
    private val configHolder: ConfigHolder,
    private val plugin: Plugin,
) : Listener {

    companion object {
        private val PARTICLES = Particle.PORTAL
        private const val PARTICLES_AMOUNT = 40
    }

    private val activeJobs = hashMapOf<UUID, BukkitRunnable>()

    fun execute(
        player: Player,
        location: Location,
        teleportMessage: String? = null
    ) {
        val id = player.uniqueId
        if (activeJobs.contains(id)) {
            return
        }

        val lastTime = teleportRepository.getPlayerLastTeleportTime(player.uniqueId)
        val cooldown = configHolder.getInt(ConfigKeys.Teleport.cooldown)
            .seconds
            .inWholeMilliseconds

        val difference = System.currentTimeMillis() - lastTime
        if (difference < cooldown) {
            kotlin.runCatching {
                val remain = (cooldown - difference).milliseconds.inWholeSeconds
                player.sendMessageWithColors(
                    message = configHolder.getString(ConfigKeys.Messages.cooldownLeft)
                        .format(remain.toString())
                )
            }
            return
        }

        val delay = configHolder.getInt(ConfigKeys.Teleport.delay)
        if (delay <= 0) {
            teleportPlayer(player, location, teleportMessage)
            return
        }

        startTeleportTimer(delay, player, location, teleportMessage)
    }

    private fun teleportPlayer(
        player: Player,
        location: Location,
        teleportMessage: String?
    ) {
        with(player) {
            teleport(location)
            spawnParticle(PARTICLES, location, PARTICLES_AMOUNT)

            getNearbyEntities(16.0, 16.0, 16.0)
                .filter { it is Player && it.canSee(player) }
                .forEach {
                    (it as Player).spawnParticle(PARTICLES, location, PARTICLES_AMOUNT)
                }

            if (teleportMessage != null) {
                player.sendMessageWithColors(teleportMessage)
            }
        }
        teleportRepository.setPlayerLastTeleportTime(player.uniqueId, System.currentTimeMillis())
    }

    private inner class TeleportDelayRunnable(
        delay: Int,
        private val player: Player,
        private val location: Location,
        private val teleportMessage: String?,
        private val onCancel: () -> Unit
    ): BukkitRunnable() {

        private var remain = delay

        override fun run() {
            when {
                remain > 0 -> {
                    kotlin.runCatching {
                        player.sendMessageWithColors(
                            message = configHolder.getString(ConfigKeys.Messages.delayLeft)
                                .also {
                                    Bukkit.getLogger().log(Level.WARNING, it)
                                }
                                .format(remain.toString())
                        )
                    }
                    remain--
                }

                remain == 0 -> {
                    teleportPlayer(player, location, teleportMessage)
                    cancel()
                }
            }
        }

        override fun cancel() {
            onCancel()
            super.cancel()
        }
    }

    private fun startTeleportTimer(
        delay: Int,
        player: Player,
        location: Location,
        teleportMessage: String?
    ) {
        val id = player.uniqueId
        activeJobs[id] = TeleportDelayRunnable(
            delay = delay,
            player = player,
            location = location,
            teleportMessage = teleportMessage,
            onCancel = { activeJobs.remove(id) },
        ).apply {
            runTaskTimer(plugin, 0, configHolder.getInt(ConfigKeys.Server.tps).toLong())
        }
    }

    @EventHandler
    fun onMove(event: PlayerMoveEvent) {

    }

    @EventHandler
    fun onLeave(event: PlayerQuitEvent) {

    }
}