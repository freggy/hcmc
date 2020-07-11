package dev.freggy.hcmc

import dev.freggy.hcmc.hcloud.HetznerCloud
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.joda.time.DateTime

// for testing purposes only

fun main(args: Array<String>) {
    val h = HetznerCloud("")

    val x = GlobalScope.launch {
        var lastCheck = DateTime(System.currentTimeMillis())
        while (true) {
            // 2 second delay here so we can have 1 different api request per second
            delay(500)
            val page = h.actions.getActions(listOf(Pair("sort", "id:desc")))

            page.actions
                .filter { DateTime(it.started).isAfter(lastCheck) }
                .forEach { println(it) }

            lastCheck = DateTime(page.actions.first().started)
        }
    }

    while (true) {
    }
}
