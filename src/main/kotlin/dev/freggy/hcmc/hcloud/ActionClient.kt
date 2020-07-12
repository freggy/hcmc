package dev.freggy.hcmc.hcloud

import dev.freggy.hcmc.hcloud.model.Action
import dev.freggy.hcmc.hcloud.model.ActionObject
import dev.freggy.hcmc.hcloud.model.ActionPage
import java.net.URI

class ActionClient(private val hcloud: HetznerCloud) {

    fun getActions(params: List<Pair<String, String>>): ActionPage {
        val builder = hcloud.baseRequest.GET()
        val url = StringBuilder("${hcloud.basePath}/actions")
        if (params.isNotEmpty()) {
            params.forEach {
                if (it == params.first()) {
                    url.append("?")
                }
                url.append("${it.first}=${it.second}")
                if (it != params.last()) {
                    url.append("&")
                }
            }
        }
        val req = builder.uri(URI.create(url.toString())).build()
        val resp = hcloud.queue.submit(req)
        return hcloud.gson.fromJson(resp.body(), ActionPage::class.java)
    }

    fun getAction(id: Int): Action {
        val req = hcloud.baseRequest.GET().uri(URI.create("${hcloud.basePath}/actions/$id")).build()
        val resp = hcloud.queue.submit(req)
        return hcloud.gson.fromJson(resp.body(), ActionObject::class.java).action
    }
}
