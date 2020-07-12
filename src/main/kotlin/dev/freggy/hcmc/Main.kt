package dev.freggy.hcmc

import dev.freggy.hcmc.hcloud.HetznerCloud
import dev.freggy.hcmc.plugin.ActionFetcher
import dev.freggy.hcmc.plugin.ActionMonitor
import kotlinx.coroutines.runBlocking

// for testing purposes only

fun main(args: Array<String>) {
    val h = HetznerCloud("")


    //println(h.actions.getAction())

    val monitor = ActionMonitor(h)
    monitor.startMonitoring()

    val fetcher = ActionFetcher(h, monitor)
    fetcher.startFetching()

    while (true) {
        runBlocking {
            val event = monitor.updateChannel.receive()
            println(event)
        }
    }
}
