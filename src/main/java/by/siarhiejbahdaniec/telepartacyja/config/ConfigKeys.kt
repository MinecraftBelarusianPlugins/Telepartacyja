package by.siarhiejbahdaniec.telepartacyja.config

object ConfigKeys {

    object Messages {
        private const val PREFIX = "messages."

        const val teleport = PREFIX + "teleport"
        const val cooldownLeft = PREFIX + "cooldown-left"

        const val delayLeft = PREFIX + "delay-left"
        const val teleportCancelled = PREFIX + "teleport-cancelled"

        const val spawnSet = PREFIX + "spawn-set"
        const val firstSpawnSet = PREFIX + "first-spawn-set"

        const val configReload = PREFIX + "config-reload"

        const val noSpawn = PREFIX + "no-spawn"
        const val noPermission = PREFIX + "no-permission"
        const val noPlayer = PREFIX + "no-player"
    }

    const val spawn = "spawn"
    const val firstSpawn = "first-spawn"
}