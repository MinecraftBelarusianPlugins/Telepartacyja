package by.siarhiejbahdaniec.telepartacyja.utils

import org.bukkit.command.CommandSender

fun CommandSender.setMessageWithColors(message: String) {
    this.sendMessage(message.applyColors())
}