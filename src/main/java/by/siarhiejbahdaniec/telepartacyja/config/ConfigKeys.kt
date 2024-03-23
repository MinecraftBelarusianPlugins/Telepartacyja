package by.siarhiejbahdaniec.telepartacyja.config

internal object ConfigKeys {

    internal object Messages {
        private const val PREFIX = "messages."

        const val teleport = PREFIX + "teleport"
        const val cooldownLeft = PREFIX + "cooldown-left"

        const val delayLeft = PREFIX + "delay-left"
        const val teleportCancelled = PREFIX + "teleport-cancelled"

        const val spawnSet = PREFIX + "spawn-set"
        const val firstSpawnSet = PREFIX + "first-spawn-set"

        const val configReload = PREFIX + "config-reload"

        const val noSpawn = PREFIX + "no-spawn"
        const val noPlayer = PREFIX + "no-player"
    }

    const val spawn = "spawn"
    const val firstSpawn = "first-spawn"

    internal object Teleport {
        private const val PREFIX = "teleport."

        const val delay = PREFIX + "delay"
        const val cooldown = PREFIX + "cooldown"
    }

    internal object Server {
        private const val PREFIX = "server."

        const val tps = PREFIX + "tps"
    }
}