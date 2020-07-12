package dev.freggy.hcmc.plugin

import dev.freggy.hcmc.hcloud.HetznerCloud
import dev.freggy.hcmc.hcloud.model.Action
import dev.freggy.hcmc.hcloud.model.ActionStatus
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.ConcurrentHashMap

class ActionMonitor(private val hcloud: HetznerCloud) {

    private val monitored = ConcurrentHashMap<Int, Action>()
    val updateChannel = Channel<Action>()

    fun startMonitoring() {
        GlobalScope.launch {
            while (true) {
                monitored.forEach {
                    delay(2000)
                    val new = hcloud.actions.getAction(it.key)
                    // if hashcode differs we know that the state of the action has changed
                    if (it.hashCode() != new.hashCode()) {
                        monitored[it.key] = new
                        updateChannel.send(new)
                        if (new.status == ActionStatus.SUCCESS || new.status == ActionStatus.ERROR) {
                            monitored.remove(it.key)
                        }
                    }
                }
            }
        }
    }

    fun monitor(action: Action) {
        monitored[action.id] = action
        // send initial action
        runBlocking {
            updateChannel.send(action)
        }
    }

    fun isMonitored(id: Int): Boolean {
        return monitored.containsKey(id)
    }
}
