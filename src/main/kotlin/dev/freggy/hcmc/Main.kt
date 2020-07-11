package dev.freggy.hcmc

import dev.freggy.hcmc.hcloud.HetznerCloud

// for testing purposes only

fun main(args: Array<String>) {

    val h = HetznerCloud("")
    val f = h.servers().getAllServers()

    println(f)
}
