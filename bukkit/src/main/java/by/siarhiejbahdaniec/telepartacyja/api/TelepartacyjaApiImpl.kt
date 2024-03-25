package by.siarhiejbahdaniec.telepartacyja.api

import by.siarhiejbahdaniec.telepartacyja.logic.TeleportExecutor
import org.bukkit.Location
import org.bukkit.entity.Player

class TelepartacyjaApiImpl(
    private val executor: TeleportExecutor,
): TelepartacyjaApi {

    override fun teleport(
        player: Player,
        location: Location,
        teleportMessage: String?,
        onComplete: () -> Unit,
        onFailed: () -> Unit,
    ) {
        executor.execute(player, location, teleportMessage, onComplete, onFailed)
    }
}