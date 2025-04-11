package me.shadymitsu.cobblemonshinydays.util

import me.shadymitsu.cobblemonshinydays.broadcast.BroadcastManager
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.server.ServerStoppingEvent

@EventBusSubscriber(modid = "cobblemonshinydays")  // Make sure modid matches your mod's ID
object ShutdownHandler {

    @SubscribeEvent
    fun onServerStopping(event: ServerStoppingEvent) {
        println("Cobblemon Shiny Days: Server is stopping, shutting down scheduler...")
        BroadcastManager.shutdown()
    }
}
