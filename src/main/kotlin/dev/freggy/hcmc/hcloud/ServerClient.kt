package dev.freggy.hcmc.hcloud

import dev.freggy.hcmc.hcloud.model.Server
import dev.freggy.hcmc.hcloud.model.ServerPage
import java.net.URI

class ServerClient(private val hcloud: HetznerCloud) {

    fun getAllServers(): ArrayList<Server> {
        val servers = arrayListOf<Server>()
        var lastPage = 0
        var currentPage = 0

        do {
            currentPage++
            val req = hcloud.baseRequest.GET()
                .uri(URI.create("${hcloud.basePath}/servers?per_page=50&page=$currentPage"))
                .build()

            val resp = hcloud.queue.submit(req)
            val page = hcloud.gson.fromJson(resp.body(), ServerPage::class.java)

            servers.addAll(page.servers)
            lastPage = page.meta.pagination.lastPage
        } while (currentPage != lastPage)

        return servers
    }

    fun getServer(id: Int): Server {
        val req = hcloud.baseRequest.GET().uri(URI.create("${hcloud.basePath}/servers/$id")).build()
        val resp = hcloud.queue.submit(req)
        return hcloud.gson.fromJson(resp.body(), Server::class.java)
    }
}
