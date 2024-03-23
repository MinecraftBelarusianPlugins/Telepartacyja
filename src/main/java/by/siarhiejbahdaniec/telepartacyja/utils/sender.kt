package by.siarhiejbahdaniec.telepartacyja.utils

import org.bukkit.command.CommandSender

internal fun CommandSender.sendMessageWithColors(message: String) {
    this.sendMessage(message.applyColors())
}