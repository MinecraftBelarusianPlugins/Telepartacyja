package by.siarhiejbahdaniec.telepartacyja.config

object ConfigKeys {

    object Messages {
        private const val PREFIX = "messages."

        const val teleport = PREFIX + "teleport"
        const val cooldownLeft = PREFIX + "cooldown-left"

        const val delayLeft = PREFIX + "delay-left"
        const val teleportCancelled = PREFIX + "teleport-cancelled"
        const val teleportCancelledUnknown = PREFIX + "teleport-cancelled-unknown"

        const val spawnSet = PREFIX + "spawn-set"
        const val firstSpawnSet = PREFIX + "first-spawn-set"

        const val configReload = PREFIX + "config-reload"

        const val noSpawn = PREFIX + "no-spawn"
        const val noPlayer = PREFIX + "no-player"
    }

    const val spawn = "spawn"
    const val firstSpawn = "first-spawn"

    object Teleport {
        private const val PREFIX = "teleport."

        const val delay = PREFIX + "delay"
        const val cooldown = PREFIX + "cooldown"
    }

    object Server {
        private const val PREFIX = "server."

        const val tps = PREFIX + "tps"
    }
}