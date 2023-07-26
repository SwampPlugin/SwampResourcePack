package com.cosine.swamp.service

import com.cosine.swamp.enums.FailReason
import org.bukkit.Server
import org.bukkit.entity.Player
import org.zeroturnaround.zip.ZipUtil
import java.io.File


class ResourcePackService(
    dataFolder: File,
    private val server: Server
) {

    private val inputFolder = File(dataFolder, "input")
    private val outputFolder = File(dataFolder, "output")

    private val resourcePackFile get() = outputFolder.listFiles()?.firstOrNull()
    private val resourcePackUri get() = resourcePackFile?.toURI().toString()

    init {
        reload()
    }

    fun reload(): FailReason? {
        generate()
        if (resourcePackFile == null) {
            return FailReason.FAILED_LOAD_FILE
        }
        applyResourcePack()
        return null
    }

    private fun applyResourcePack() {
        server.onlinePlayers.forEach { player ->
            player.setResourcePack(resourcePackUri)
        }
    }

    fun applyResourcePack(player: Player) {
        if (resourcePackFile == null) return
        player.setResourcePack(resourcePackUri)
    }

    private fun generate() {
        val newFile = File(outputFolder, "generate.zip")
        ZipUtil.pack(inputFolder, newFile)
    }
}