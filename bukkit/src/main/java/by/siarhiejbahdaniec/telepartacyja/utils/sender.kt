package by.siarhiejbahdaniec.telepartacyja.utils

import org.bukkit.command.CommandSender

fun CommandSender.sendMessageWithColors(message: String) {
    this.sendMessage(message.applyColors())
}