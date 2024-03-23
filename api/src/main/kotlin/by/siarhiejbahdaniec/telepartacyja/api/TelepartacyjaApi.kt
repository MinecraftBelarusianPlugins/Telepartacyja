package by.siarhiejbahdaniec.telepartacyja.api

import org.bukkit.Location
import org.bukkit.entity.Player

interface TelepartacyjaApi {

    fun teleport(
        player: Player,
        location: Location,
        teleportMessage: String? = null,
        onComplete: () -> Unit = {},
    )
}