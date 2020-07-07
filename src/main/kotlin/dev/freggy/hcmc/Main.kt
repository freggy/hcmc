package dev.freggy.hcmc

import dev.freggy.hcmc.hcloud.HetznerCloud
import kotlinx.coroutines.runBlocking

// for testing purposes only

fun main(args: Array<String>) {

    val h = HetznerCloud("")

    runBlocking {
        println(h.servers().getServers()[0].status)
    }
}