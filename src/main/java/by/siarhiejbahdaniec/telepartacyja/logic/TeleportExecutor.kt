package by.siarhiejbahdaniec.telepartacyja.logic

import by.siarhiejbahdaniec.telepartacyja.config.ConfigHolder
import by.siarhiejbahdaniec.telepartacyja.config.ConfigKeys
import by.siarhiejbahdaniec.telepartacyja.repo.TeleportRepository
import by.siarhiejbahdaniec.telepartacyja.utils.sendMessageWithColors
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
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

        private const val PERMISSION_DELAY_BYPASS = "spawn.bypass.delay"
        private const val PERMISSION_MOVE_CANCEL_BYPASS = "spawn.bypass.cancel-on-move"
        private const val PERMISSION_SPAWN_COOLDOWN_BYPASS = "spawn.bypass.cooldown"
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

        if (!player.hasPermission(PERMISSION_SPAWN_COOLDOWN_BYPASS)) {
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
        }

        teleportRepository.setPlayerLastTeleportTime(player.uniqueId, System.currentTimeMillis())

        val delay = configHolder.getInt(ConfigKeys.Teleport.delay)
        if (delay <= 0 || player.hasPermission(PERMISSION_DELAY_BYPASS)) {
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
        val player = event.player
        if (activeJobs.containsKey(player.uniqueId)) {
            if (player.hasPermission(PERMISSION_MOVE_CANCEL_BYPASS)) {
                return
            }

            val to = event.to
            if (to != null && (event.from.distanceSquared(to) < 0.01)) {
                return
            }

            cancelTeleportation(player)
        }
    }

    @EventHandler
    fun onDamage(event: EntityDamageEvent) {
        val entity = event.entity
        if (entity is Player && activeJobs.containsKey(entity.uniqueId)) {
            if (entity.hasPermission(PERMISSION_MOVE_CANCEL_BYPASS)) {
                return
            }

            cancelTeleportation(entity)
        }
    }

    private fun cancelTeleportation(player: Player) {
        activeJobs[player.uniqueId]?.cancel()
        player.sendMessageWithColors(
            message = configHolder.getString(ConfigKeys.Messages.teleportCancelled)
        )
    }

    @EventHandler
    fun onLeave(event: PlayerQuitEvent) {
        val id = event.player.uniqueId
        activeJobs[id]?.cancel()
    }
}