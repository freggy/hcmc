package dev.freggy.hcmc.hcloud

import dev.freggy.hcmc.hcloud.model.Server
import dev.freggy.hcmc.hcloud.model.ServerPage
import java.net.URI

class ServerClient(private val hcloud: HetznerCloud) {

    fun getAllServers(): Array<Server> {
        // TODO: get all servers
        val req = hcloud.baseRequest.GET().uri(URI.create("${hcloud.basePath}/servers")).build()
        val resp = hcloud.queue.submit(req)
        return hcloud.gson.fromJson(resp.body(), ServerPage::class.java).servers
    }

    fun getServer(id: Int): Server {
        val req = hcloud.baseRequest.GET().uri(URI.create("${hcloud.basePath}/servers/$id")).build()
        val resp = hcloud.queue.submit(req)
        return hcloud.gson.fromJson(resp.body(), Server::class.java)
    }
}
