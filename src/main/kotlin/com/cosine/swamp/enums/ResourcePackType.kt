package com.cosine.swamp.enums

enum class ResourcePackType(val defaultPort: Int) {
    EMPTY(25564),
    GENERATE(25563);

    companion object {
        fun getResourcePackType(key: String): ResourcePackType? {
            return ResourcePackType.values().find { it.name == key.uppercase() }
        }
    }
}