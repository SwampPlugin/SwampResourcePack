package com.cosine.swamp

import com.cosine.library.kommand.KommandManager
import com.cosine.swamp.command.ResourcePackAdminCommand
import com.cosine.swamp.config.WebConfig
import com.cosine.swamp.listener.ResourcePackListener
import com.cosine.swamp.registry.WebServerRegistry
import com.cosine.swamp.service.ResourcePackService
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

@Suppress("MemberVisibilityCanBePrivate")
class SwampResourcePack : JavaPlugin() {

    companion object {
        internal lateinit var plugin: SwampResourcePack
            private set

        val instance: SwampResourcePack get() = plugin

        internal const val prefix = "§b[ SwampFarmingCrate ]§f"
    }

    lateinit var resourcePackService: ResourcePackService
        private set

    lateinit var webServerRegistry: WebServerRegistry
        private set

    lateinit var webConfig: WebConfig
        private set

    override fun onLoad() {
        plugin = this
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
}