package com.cosine.swamp

import com.cosine.library.kommand.KommandManager
import com.cosine.swamp.command.ResourcePackAdminCommand
import com.cosine.swamp.config.WebConfig
import com.cosine.swamp.listener.ResourcePackListener
import com.cosine.swamp.registry.WebServerRegistry
import com.cosine.swamp.service.ResourcePackService
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class SwampResourcePack : JavaPlugin() {

    companion object {
        private lateinit var instance: SwampResourcePack

        fun getInstance(): SwampResourcePack = instance

        internal const val prefix = "§b[ SwampResourcePack ]§f"
    }

    private lateinit var resourcePackService: ResourcePackService
    private lateinit var webServerRegistry: WebServerRegistry
    private lateinit var webConfig: WebConfig

    override fun onLoad() {
        instance = this
        if (!dataFolder.exists()) {
            dataFolder.mkdirs()
        }
        createFolder("empty")
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
        webServerRegistry = WebServerRegistry()

        webConfig = WebConfig(this, webServerRegistry)
        webConfig.load()

        resourcePackService = ResourcePackService(this, webServerRegistry)
        resourcePackService.loadResourcePack()

        server.pluginManager.registerEvents(ResourcePackListener(resourcePackService), this)

        KommandManager.registerCommand(
            ResourcePackAdminCommand(resourcePackService)
        )
    }

    override fun onDisable() {
        resourcePackService.stopHttpServer()
    }

    fun getResourcePackService(): ResourcePackService = resourcePackService
}