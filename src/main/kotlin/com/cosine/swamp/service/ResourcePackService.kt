package com.cosine.swamp.service

import com.cosine.library.extension.later
import com.cosine.swamp.config.WebConfig
import com.cosine.swamp.enums.ResourcePackType
import com.cosine.swamp.registry.WebServerRegistry
import io.vertx.core.Vertx
import io.vertx.core.http.HttpServer
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.zeroturnaround.zip.ZipUtil
import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest

class ResourcePackService(
    plugin: Plugin,
    private val webConfig: WebConfig,
    private val webServerRegistry: WebServerRegistry
) {

    private val dataFolder = plugin.dataFolder
    private val server = plugin.server

    private val defaultFolder = File(dataFolder, "empty")
    private val inputFolder = File(dataFolder, "input")
    private val outputFolder = File(dataFolder, "output")

    private val outputFolderFiles get() = outputFolder.listFiles()
    private val emptyResourcePackFile get() = outputFolderFiles?.firstOrNull { it.name == "default.zip" }
    private val generateResourcePackFile get() = outputFolderFiles?.firstOrNull { it.name == "generate.zip" }
    private val sha1 get() = generateResourcePackFile?.getSHA1()

    private var emptyResourcePackUrl = ""
    private var generateResourcePackUrl = ""

    private val httpServers = mutableListOf<HttpServer>()

    private fun startHttpServer() {
        val ip = webServerRegistry.getIp()
        val emptyPort = webServerRegistry.getPort(ResourcePackType.EMPTY)
        val generatePort = webServerRegistry.getPort(ResourcePackType.GENERATE)

        emptyResourcePackUrl = "http://$ip:$emptyPort"
        generateResourcePackUrl = "http://$ip:$generatePort"

        // createHttpServer(emptyPort, emptyResourcePackFile)
        createHttpServer(generatePort, generateResourcePackFile)
    }

    private fun createHttpServer(port: Int, resourcePackFile: File?) {
        val httpServer = Vertx.vertx().createHttpServer()
        httpServer.requestHandler { request ->
            val response = request.response()
            response.sendFile(resourcePackFile?.path)
        }
        httpServer.listen(port)
    }

    fun stopHttpServer() {
        httpServers.forEach(HttpServer::close)
        httpServers.clear()
    }

    fun loadResourcePack() {
        generateResourcePack()
        startHttpServer()
        applyResourcePack()
    }

    fun reloadResourcePack() {
        webConfig.reload()
        //stopHttpServer()
        generateResourcePack()
        //startHttpServer()
        applyResourcePack()
    }

    fun applyResourcePack(player: Player) {
        player.sendMessage("emptyResourcePackUrl: $emptyResourcePackUrl")
        player.sendMessage("generateResourcePackUrl: $generateResourcePackUrl")
        // player.setResourcePack(emptyResourcePackUrl, emptyResourcePackFile?.getSHA1())
        player.setResourcePack(generateResourcePackUrl, sha1)
    }

    private fun applyResourcePack() {
        later(20) {
            server.onlinePlayers.forEach { player ->
                applyResourcePack(player)
            }
        }
    }

    private fun generateResourcePack() {
        val defaultFile = File(outputFolder, "default.zip")
        val generateFile = File(outputFolder, "generate.zip")
        ZipUtil.pack(inputFolder, generateFile)
        ZipUtil.pack(defaultFolder, defaultFile)
    }

    private fun File.getSHA1(): ByteArray {
        return MessageDigest.getInstance("SHA-1").digest(readBytes())
    }
}