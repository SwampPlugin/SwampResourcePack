package com.cosine.swamp.listener

import com.cosine.swamp.service.ResourcePackService
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class ResourcePackListener(
    private val resourcePackService: ResourcePackService
) : Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        resourcePackService.applyResourcePack(player)
    }
}