package me.shadymitsu.cobblemonshinydays.config

import com.google.gson.GsonBuilder
import java.io.File
import java.io.FileWriter

object ConfigCreator {

    private val configFile = File("config/cobblemonshinydays/config.json")

    fun createDefaultConfig() {
        val defaultConfig = getDefaultConfig()
        saveConfig(defaultConfig)
    }

    private fun saveConfig(config: List<ConfigFields>) {
        val gson = GsonBuilder().setPrettyPrinting().create()
        configFile.parentFile.mkdirs()
        FileWriter(configFile).use { writer ->
            gson.toJson(config, writer)
        }
    }

    fun getDefaultConfig(): List<ConfigFields> {
        return listOf(
            ConfigFields(
                species = listOf("Pikachu", "Bulbasaur", "Charmander", "Squirtle"),
                labels = listOf("legendary", "mythical"),
                types = listOf("normal"),
                days = listOf("Saturday", "Sunday"),
                multiplier = 5.0f,
                broadcastInterval = 300
            ),
            ConfigFields(
                species = emptyList(),
                labels = listOf("gen1"),
                types = listOf("fire"),
                days = listOf("Monday"),
                multiplier = 2.0f,
                broadcastInterval = 300
            ),
            ConfigFields(
                species = emptyList(),
                labels = listOf("gen2"),
                types = listOf("water"),
                days = listOf("Tuesday"),
                multiplier = 2.0f,
                broadcastInterval = 300
            ),
            ConfigFields(
                species = emptyList(),
                labels = listOf("gen3"),
                types = listOf("psychic"),
                days = listOf("Wednesday"),
                multiplier = 2.0f,
                broadcastInterval = 300
            ),
            ConfigFields(
                species = emptyList(),
                labels = listOf("gen4"),
                types = listOf("grass"),
                days = listOf("Thursday"),
                multiplier = 2.0f,
                broadcastInterval = 300
            ),
            ConfigFields(
                species = emptyList(),
                labels = listOf("gen5"),
                types = listOf("lightning"),
                days = listOf("Friday"),
                multiplier = 2.0f,
                broadcastInterval = 300
            )
        )
    }
}