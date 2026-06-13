package io.github.sophon.quexplorer

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform