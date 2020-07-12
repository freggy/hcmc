package dev.freggy.hcmc.plugin

import dev.freggy.hcmc.hcloud.HetznerCloud
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.joda.time.DateTime

class ActionFetcher(private val hcloud: HetznerCloud, private val monitor: ActionMonitor) {

    fun startFetching() {
        GlobalScope.launch {
            var lastCheck = System.currentTimeMillis()
            while (true) {
                delay(2000)
                val page = hcloud.actions.getActions(listOf(Pair("sort", "id:desc")))
                page.actions
                    .filter { DateTime(it.started).isAfter(lastCheck) && !monitor.isMonitored(it.id) }
                    .forEach { monitor.monitor(it) }
                lastCheck = System.currentTimeMillis()
            }
        }
    }
}
