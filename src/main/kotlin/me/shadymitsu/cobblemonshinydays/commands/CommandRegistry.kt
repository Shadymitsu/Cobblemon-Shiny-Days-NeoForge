package me.shadymitsu.cobblemonshinydays.commands

import me.shadymitsu.cobblemonshinydays.commands.CheckCommand
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.RegisterCommandsEvent

@EventBusSubscriber(modid = "cobblemonshinydays")
object CommandRegistry {

    @SubscribeEvent
    fun onCommandRegister(event: RegisterCommandsEvent) {
        event.dispatcher.register(CheckCommand.register())
    }
}
