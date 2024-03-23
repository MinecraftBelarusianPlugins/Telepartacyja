package by.siarhiejbahdaniec.telepartacyja.utils

import net.md_5.bungee.api.ChatColor
import java.util.regex.Pattern

private val pattern: Pattern = Pattern.compile("&#[a-fA-F0-9]{6}")

internal fun String.applyColors(): String {
    var text = this
    var match = pattern.matcher(text)
    while (match.find()) {
        val color = text.substring(match.start(), match.end())
        text = text.replace(color, ChatColor.of(color.replace("&#", "#")).toString() + "")
        match = pattern.matcher(text)
    }

    return ChatColor.translateAlternateColorCodes('&', text)
}
