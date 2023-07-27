package com.cosine.swamp.registry

import com.cosine.swamp.enums.ResourcePackType

class WebServerRegistry {

    private var ip = "localhost"
    private var portMap = mutableMapOf<ResourcePackType, Int>()

    fun getIp(): String = ip

    fun setIp(ip: String) {
        this.ip = ip
    }

    fun getPort(resourcePackType: ResourcePackType): Int = portMap[resourcePackType] ?: resourcePackType.defaultPort

    fun setPort(resourcePackType: ResourcePackType, port: Int) {
        portMap[resourcePackType] = port
    }
}