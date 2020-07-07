package dev.freggy.hcmc.hcloud

import dev.freggy.hcmc.hcloud.model.Server
import dev.freggy.hcmc.hcloud.model.ServerPage
import io.ktor.client.HttpClient
import io.ktor.client.request.*

class ServerClient(private val client: HttpClient) {

    suspend fun getServers(): Array<Server> {
        return this.client.get<ServerPage> {
            url {
                path("servers")
            }
        }.servers
    }

    suspend fun getServer(id: Int): Server {
        return this.client.get {
            url {
                path("server", "$id")
            }
        }
    }
}