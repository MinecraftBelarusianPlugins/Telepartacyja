package by.siarhiejbahdaniec.telepartacyja.logic

import by.siarhiejbahdaniec.telepartacyja.config.ConfigHolder
import by.siarhiejbahdaniec.telepartacyja.config.ConfigKeys
import by.siarhiejbahdaniec.telepartacyja.utils.sendMessageWithColors
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

internal class SpawnCommandExecutor(
    private val configHolder: ConfigHolder,
    private val teleportExecutor: TeleportExecutor,
) : TabExecutor {

    companion object {
        private const val PARAM_SET = "set"
        private const val PARAM_FIRST_SET = "firstset"
        private const val PARAM_RELOAD = "reload"
    }

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (args.isEmpty()) {
            if (sender is Player) {
                val location = configHolder.getLocation(ConfigKeys.spawn)
                if (location != null) {
                    teleportExecutor.execute(
                        player = sender,
                        location = location,
                        teleportMessage = configHolder.getString(ConfigKeys.Messages.teleport),
                    )
                } else {
                    sender.sendMessageWithColors(
                        configHolder.getString(ConfigKeys.Messages.noSpawn)
                    )
                }
            } else {
                sender.sendMessageWithColors(
                    configHolder.getString(ConfigKeys.Messages.noPlayer)
                )
            }
            return true
        }

        if (args.size == 1 && sender.isOp) {
            when (args[0]) {
                PARAM_SET -> handleSetLocation(sender, isFirstSpawn = false)
                PARAM_FIRST_SET -> handleSetLocation(sender, isFirstSpawn = true)
                PARAM_RELOAD -> handleReload(sender)
                else -> return false
            }
            return true
        }

        return false
    }

    private fun handleSetLocation(sender: CommandSender, isFirstSpawn: Boolean) {
        require(sender.isOp)

        if (sender !is Player) {
            sender.sendMessageWithColors(configHolder.getString(ConfigKeys.Messages.noPlayer))
            return
        }

        configHolder.setLocation(
            key = if (isFirstSpawn) {
                ConfigKeys.firstSpawn
            } else {
                ConfigKeys.spawn
            },
            location = sender.location
        )
        sender.sendMessageWithColors(
            message = configHolder.getString(
                key = if (isFirstSpawn) {
                    ConfigKeys.Messages.firstSpawnSet
                } else {
                    ConfigKeys.Messages.spawnSet
                }
            )
        )
    }

    private fun handleReload(sender: CommandSender) {
        require(sender.isOp)

        configHolder.reloadConfigFromDisk()
        sender.sendMessageWithColors(configHolder.getString(ConfigKeys.Messages.configReload))
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): List<String>? {
        if (args.size == 1 && sender.isOp) {
            return listOf(PARAM_SET, PARAM_FIRST_SET, PARAM_RELOAD)
        }

        return null
    }
}