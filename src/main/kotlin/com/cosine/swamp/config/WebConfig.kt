package com.cosine.swamp.config

import com.cosine.swamp.enums.ResourcePackType
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
            webServerRegistry.setIp(ip)
            getConfigurationSection("port")?.apply {
                getKeys(false).forEach { key ->
                    val resourcePackType = ResourcePackType.getResourcePackType(key) ?: run {
                        logger.warning("port 섹션의 $key(은)는 존재하지 않는 타입입니다.")
                        return@forEach
                    }
                    val port = getInt(key)
                    webServerRegistry.setPort(resourcePackType, port)
                }
            }
        }
    }

    fun reload() {
        config.load(file)
        load()
    }
}