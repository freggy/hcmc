package dev.freggy.hcmc.hcloud

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class RequestQueue(private val client: HttpClient) {

    private val resps = Channel<HttpResponse<String>>()
    private val reqs = Channel<HttpRequest>()

    fun start() {
        GlobalScope.launch {
            while (true) {
                delay(1000)
                try {
                    resps.send(client.send(reqs.receive(), HttpResponse.BodyHandlers.ofString()))
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
    }

    fun submit(request: HttpRequest): HttpResponse<String> {
        return runBlocking {
            reqs.send(request)
            return@runBlocking resps.receive()
        }
    }
}
