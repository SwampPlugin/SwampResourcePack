package com.cosine.swamp.registry

class WebServerRegistry {

    private var ip = "localhost"
    private var port = 25564

    fun getIp(): String = ip

    fun setIp(ip: String) {
        this.ip = ip
    }

    fun getPort(): Int = port

    fun setPort(port: Int) {
        this.port = port
    }
}