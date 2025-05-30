package me.shadymitsu.cobblemonshinydays.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import me.shadymitsu.cobblemonshinydays.config.ConfigLoader
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.network.chat.Component
import java.time.LocalDateTime

object CheckCommand {

    fun register(): LiteralArgumentBuilder<CommandSourceStack> {
        return Commands.literal("shinyday")
            .requires { it.hasPermission(0) }
            .executes { context ->
                val sender = context.source
                val day = LocalDateTime.now().dayOfWeek.name
                val config = ConfigLoader.loadConfig()

                val relevantEntries = config.filter {
                    it.days.any { d -> d.equals(day, ignoreCase = true) }
                }

                if (relevantEntries.isEmpty()) {
                    sender.sendSystemMessage(Component.literal("§eThere are no shiny boosts for today."))
                    return@executes Command.SINGLE_SUCCESS
                }

                relevantEntries.forEach { entry ->
                    val parts = mutableListOf<String>()

                    if (entry.species.isNotEmpty()) {
                        parts.add("§dSpecies: ${entry.species.joinToString(", ")}")
                    }
                    if (entry.labels.isNotEmpty()) {
                        parts.add("§bLabels: ${entry.labels.joinToString(", ")}")
                    }
                    if (entry.types.isNotEmpty()) {
                        parts.add("§aTypes: ${entry.types.joinToString(", ")}")
                    }

                    val message = "§6Shiny Boosts: §fMultiplier §e${entry.multiplier}x§f — ${parts.joinToString(" §7| ")}"
                    sender.sendSystemMessage(Component.literal(message))
                }

                Command.SINGLE_SUCCESS
            }
    }
}
