package com.cosine.swamp.service

import com.cosine.swamp.registry.WebServerRegistry
import io.vertx.core.Vertx
import io.vertx.core.http.HttpServer
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.zeroturnaround.zip.ZipUtil
import java.io.File
import java.security.MessageDigest

class ResourcePackService(
    plugin: Plugin,
    private val webServerRegistry: WebServerRegistry
) {

    private val messageDigest = MessageDigest.getInstance("SHA-1")

    private val dataFolder = plugin.dataFolder
    private val server = plugin.server

    private val inputFolder = File(dataFolder, "input")
    private val outputFolder = File(dataFolder, "output")

    private var resourcePackUrl = ""

    private var httpServer: HttpServer? = null

    private fun startHttpServer() {
        val ip = webServerRegistry.getIp()
        val port = webServerRegistry.getPort()

        resourcePackUrl = "http://$ip:$port"

        httpServer = Vertx.vertx().createHttpServer()
        httpServer?.requestHandler { request ->
            val path = getResourcePackFile().path
            request.response().sendFile(path)
        }
        httpServer?.listen(port)
    }

    fun stopHttpServer() {
        httpServer?.close()
        httpServer = null
    }

    fun loadResourcePack() {
        generateResourcePack()
        startHttpServer()
        applyResourcePack()
    }

    fun reloadResourcePack() {
        generateResourcePack()
        applyResourcePack()
    }

    fun applyResourcePack(player: Player) {
        val url = "$resourcePackUrl#${getResourcePackSha1()}"
        player.setResourcePack(url)
    }

    private fun applyResourcePack() {
        server.onlinePlayers.forEach { player ->
            applyResourcePack(player)
        }
    }

    private fun generateResourcePack() {
        val generateFile = getResourcePackFile()
        ZipUtil.pack(inputFolder, generateFile)
    }

    private fun getResourcePackFile(): File = File(outputFolder, "generate.zip")

    private fun getResourcePackSha1(): ByteArray {
        return messageDigest.digest(getResourcePackFile().readBytes())
    }
}