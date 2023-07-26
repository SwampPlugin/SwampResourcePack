package com.cosine.swamp

import com.cosine.library.kommand.KommandManager
import com.cosine.swamp.command.ResourcePackAdminCommand
import com.cosine.swamp.listener.ResourcePackListener
import com.cosine.swamp.service.ResourcePackService
import com.cosine.swamp.service.VerticleService
import io.vertx.core.Vertx
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class SwampResourcePack : JavaPlugin() {

    companion object {
        private lateinit var instance: SwampResourcePack

        fun getInstance(): SwampResourcePack = instance

        internal const val prefix = "§b[ SwampResourcePack ]§f"
    }

    private lateinit var resourcePackService: ResourcePackService

    override fun onLoad() {
        instance = this
        if (!dataFolder.exists()) {
            dataFolder.mkdirs()
        }
        createFolder("input")
        createFolder("output")
    }

    private fun createFolder(child: String) {
        val folder = File(dataFolder, child)
        if (!folder.exists()) {
            folder.mkdirs()
        }
    }

    override fun onEnable() {
        val vertx = Vertx.vertx()
        vertx.deployVerticle(VerticleService())

        resourcePackService = ResourcePackService(dataFolder, server)

        server.pluginManager.registerEvents(ResourcePackListener(resourcePackService), this)

        KommandManager.registerCommand(
            ResourcePackAdminCommand(resourcePackService)
        )
    }

    override fun onDisable() {

    }

    fun getResourcePackService(): ResourcePackService = resourcePackService
}