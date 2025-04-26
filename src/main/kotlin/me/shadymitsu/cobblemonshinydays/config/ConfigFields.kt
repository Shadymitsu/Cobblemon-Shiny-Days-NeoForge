package me.shadymitsu.cobblemonshinydays.config

data class ConfigFields(
    val species: List<String>,
    val labels: List<String>,
    val days: List<String>,
    val multiplier: Float,
    val broadcastInterval: Int? = null
)
