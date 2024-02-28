package by.siarhiejbahdaniec.telepartacyja.command

import by.siarhiejbahdaniec.telepartacyja.config.ConfigHolder
import by.siarhiejbahdaniec.telepartacyja.config.ConfigKeys
import by.siarhiejbahdaniec.telepartacyja.utils.setMessageWithColors
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player

class SpawnCommandExecutor(
    private val configHolder: ConfigHolder,
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
            sender.setMessageWithColors(configHolder.getString(ConfigKeys.Messages.noPlayer))
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
        sender.setMessageWithColors(
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
        sender.setMessageWithColors(configHolder.getString(ConfigKeys.Messages.configReload))
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): List<String>? {
        if (args.isEmpty()) {
            return listOf(PARAM_SET, PARAM_FIRST_SET, PARAM_RELOAD)
        }

        return null
    }
}