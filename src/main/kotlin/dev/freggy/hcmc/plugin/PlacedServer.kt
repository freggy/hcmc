package dev.freggy.hcmc.plugin

import dev.freggy.hcmc.hcloud.HetznerCloud
import dev.freggy.hcmc.hcloud.model.ActionStatus
import dev.freggy.hcmc.hcloud.model.Server
import org.bukkit.Location
import org.bukkit.Material
import java.util.concurrent.locks.ReentrantLock

class PlacedServer(inner: Server, location: Location) {
    private val innerLock = ReentrantLock()

    var location: Location = location
        private set

    var inner: Server = inner
        private set
        get() {
            innerLock.lock()
            try {
                return field
            } finally {
                innerLock.unlock()
            }
        }

    /**
     * Updates [inner] server object. Should be called when updates to the
     * PlacedServer are made to always have an up-to-date pointer
     * to the cloud server representing this placed server.
     */
    fun updateInner(hcloud: HetznerCloud) {
        innerLock.lock()
        try {
            inner = hcloud.servers.getServer(inner.id)
        } finally {
            innerLock.unlock()
        }
    }

    fun remove(status: ActionStatus) {
        this.location.block.type = Material.AIR
    }

    fun start(status: ActionStatus) {
        this.location.block.type = Material.EMERALD_BLOCK
    }

    fun stop(status: ActionStatus) {
        if (status == ActionStatus.SUCCESS) {
            this.location.block.type = Material.REDSTONE_BLOCK
            return
        }

        if (status == ActionStatus.RUNNING) {
            this.location.block.type = Material.OAK_WOOD
        }
    }

    fun migrate(status: ActionStatus) {
        this.location.block.type = Material.REDSTONE_LAMP
    }

    fun off(status: ActionStatus) {
        this.location.block.type = Material.BEDROCK
    }
}
