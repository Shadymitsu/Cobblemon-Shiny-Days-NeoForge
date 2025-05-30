package me.shadymitsu.cobblemonshinydays.broadcast

import com.cobblemon.mod.common.util.server
import me.shadymitsu.cobblemonshinydays.config.ConfigLoader
import net.minecraft.network.chat.Component
import java.time.LocalDateTime
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object BroadcastManager {

    private val scheduler = Executors.newSingleThreadScheduledExecutor()

    private val labelDisplayNames = mapOf(
        "legendary" to "Legendary",
        "mythical" to "Mythical",
        "ultra_beast" to "Ultra Beast",
        "fossil" to "Fossil",
        "powerhouse" to "Powerhouse",
        "baby" to "Baby",
        "regional" to "Regional",
        "alolan_form" to "Alolan Form",
        "galarian_form" to "Galarian Form",
        "hisuian_form" to "Hisuian Form",
        "paldean_form" to "Paldean Form",
        "mega" to "Mega",
        "primal" to "Primal",
        "gmax" to "G-Max",
        "totem" to "Totem",
        "paradox" to "Paradox",
        "gen1" to "Gen 1",
        "gen2" to "Gen 2",
        "gen3" to "Gen 3",
        "gen4" to "Gen 4",
        "gen5" to "Gen 5",
        "gen6" to "Gen 6",
        "gen7" to "Gen 7",
        "gen8" to "Gen 8",
        "gen9" to "Gen 9",
        "customized_official" to "Customized Official",
        "custom" to "Custom"
    )

    fun startBroadcasting() {
        val config = ConfigLoader.loadConfig()

        val groupedByInterval = config
            .filter { it.broadcastInterval != null }
            .groupBy { it.broadcastInterval }

        for ((intervalSeconds, entries) in groupedByInterval) {
            scheduler.scheduleAtFixedRate({
                val now = LocalDateTime.now()
                val currentDay = now.dayOfWeek.name

                entries.forEach { entry ->
                    val isActiveDay = entry.days.any { it.equals(currentDay, ignoreCase = true) }

                    if (isActiveDay) {
                        if (entry.species.map { it.uppercase() }.contains("ALL")) {
                            val message = "§eToday is a §6Shiny Day! §eIf you're lucky you may encounter a shiny Pokémon!"
                            broadcastToServer(message)
                        } else {
                            val speciesPart = entry.species.joinToString(", ") { "§d$it" }

                            val labelsPart = entry.labels.mapNotNull {
                                labelDisplayNames[it.lowercase()]
                            }.joinToString(", ") { "§b$it" }

                            val typesPart = entry.types.joinToString(", ") { "§a$it" }

                            val fullList = listOfNotNull(
                                speciesPart.takeIf { it.isNotEmpty() },
                                labelsPart.takeIf { it.isNotEmpty() },
                                typesPart.takeIf { it.isNotEmpty() }
                            )

                            if (fullList.isNotEmpty()) {
                                val parts = fullList.flatMap { it.split(", ") }
                                val combined = when (parts.size) {
                                    1 -> parts[0]
                                    2 -> "${parts[0]} §7or ${parts[1]}"
                                    else -> parts.dropLast(1).joinToString("§7, ") + " §7or ${parts.last()}"
                                }

                                val message = "§eToday is a §6Shiny Day! §eIf you're lucky you may encounter a shiny $combined §ePokémon!"
                                broadcastToServer(message)
                            }
                        }
                    }
                }
            }, 0, intervalSeconds!!.toLong(), TimeUnit.SECONDS)
        }
    }

    private fun broadcastToServer(message: String) {
        val component = Component.literal(message)
        server()?.playerList?.players?.forEach { player ->
            player.sendSystemMessage(component)
        }
    }

    fun shutdown() {
        scheduler.shutdownNow()
        println("Cobblemon Shiny Days: Broadcast scheduler shut down successfully.")
    }
}
