package com.farimarwat.krossmapdemo

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform