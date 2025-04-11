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

        // Load once at startup
        val config = ConfigLoader.loadConfig()
        println("Config loaded with ${config.size} entry(s).")

        // Register event
        CobblemonEvents.SHINY_CHANCE_CALCULATION.subscribe { event ->
            handleShinyChanceCalculation(event)
        }

        // Start broadcasting
        BroadcastManager.startBroadcasting()
    }

    private fun handleShinyChanceCalculation(event: ShinyChanceCalculationEvent) {
        val day = LocalDateTime.now().dayOfWeek.name
        val speciesName = event.pokemon.species.name

        val config = ConfigLoader.loadConfig()
        val multiplier = config.firstOrNull {
            (it.species.contains("ALL") ||
                    it.species.any { s -> s.equals(speciesName, ignoreCase = true) }) &&
                    it.days.any { configDay -> configDay.equals(day, ignoreCase = true) }
        }?.multiplier

        if (multiplier != null) {
            event.addModificationFunction { base, _, _ ->
                base / multiplier
            }
        }
    }
}
