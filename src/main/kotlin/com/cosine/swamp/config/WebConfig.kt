package com.cosine.swamp.config

import com.cosine.swamp.registry.WebServerRegistry
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.Plugin
import java.io.File

class WebConfig(
    plugin: Plugin,
    private val webServerRegistry: WebServerRegistry
) {

    private companion object {
        const val path = "config.yml"
    }

    private val logger by lazy { plugin.logger }

    private var file: File
    private var config: YamlConfiguration

    init {
        val newFile = File(plugin.dataFolder, path)
        if (!newFile.exists() && plugin.getResource(path) != null) {
            plugin.saveResource(path, false)
        }
        file = newFile
        config = YamlConfiguration.loadConfiguration(newFile)
    }

    fun load() {
        config.getConfigurationSection("web-server")?.apply {
            val ip = getString("ip") ?: run {
                logger.warning("ip를 불러오지 못했습니다.")
                return@apply
            }
            val port = getInt("port")
            webServerRegistry.setIp(ip)
            webServerRegistry.setPort(port)
        }
    }

    fun reload() {
        config.load(file)
        load()
    }
}