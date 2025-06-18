package me.shadymitsu.cobblemonshinydays

import com.cobblemon.mod.common.api.events.pokemon.ShinyChanceCalculationEvent
import com.cobblemon.mod.common.api.events.CobblemonEvents
import me.shadymitsu.cobblemonshinydays.broadcast.BroadcastManager
import me.shadymitsu.cobblemonshinydays.config.ConfigLoader
import net.neoforged.fml.common.Mod
import java.time.LocalDateTime

@Mod("cobblemonshinydays")
class CobblemonShinyDays {

    init {
        println("Cobblemon Shiny Days loaded!")
        val config = ConfigLoader.loadConfig()
        println("Config loaded with ${config.size} time block(s).")

        CobblemonEvents.SHINY_CHANCE_CALCULATION.subscribe { event ->
            handleShinyChanceCalculation(event)
        }

        BroadcastManager.startBroadcasting()
    }

    private fun handleShinyChanceCalculation(event: ShinyChanceCalculationEvent) {
        val day = LocalDateTime.now().dayOfWeek.name
        val speciesName = event.pokemon.species.name
        val pokemonTypes = event.pokemon.types.map { it.name.lowercase() }

        val config = ConfigLoader.loadConfig()

        val multiplier = config.firstOrNull { entry ->
            val dayMatch = entry.days.any { it.equals(day, ignoreCase = true) }
            if (!dayMatch) return@firstOrNull false

            val speciesMatch = entry.species.map { it.uppercase() }.contains("ALL") ||
                    entry.species.any { it.equals(speciesName, ignoreCase = true) }

            val labelMatch = entry.labels.any { label -> event.pokemon.hasLabels(label) }

            val typeMatch = entry.types.any { type -> pokemonTypes.contains(type.lowercase()) }

            speciesMatch || labelMatch || typeMatch
        }?.multiplier

        if (multiplier != null) {
            event.addModificationFunction { base, _, _ -> base / multiplier }
        }
    }
}
