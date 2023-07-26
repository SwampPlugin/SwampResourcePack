package com.cosine.swamp.command

import com.cosine.library.kommand.Kommand
import com.cosine.library.kommand.KommandBuilder
import com.cosine.library.kommand.SubKommand
import com.cosine.swamp.SwampResourcePack
import com.cosine.swamp.service.ResourcePackService
import org.bukkit.command.CommandSender

@Kommand("리소스팩")
class ResourcePackAdminCommand(
    private val resourcePackService: ResourcePackService
) : KommandBuilder(SwampResourcePack.getInstance(), SwampResourcePack.prefix) {

    @SubKommand("리로드", "리소스팩을 리로드합니다.", helpPriority = 1)
    fun applyReouscePack(sender: CommandSender) {
        val failReason = resourcePackService.reload()
        if (failReason == null) {
            sender.sendMessage("$prefix 리소스팩을 리로드하였습니다.")
        } else {
            sender.sendMessage("$prefix 파일을 불러오지 못했습니다.")
        }
    }
}