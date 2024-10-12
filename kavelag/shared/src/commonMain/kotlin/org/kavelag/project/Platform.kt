package org.kavelag.project

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform