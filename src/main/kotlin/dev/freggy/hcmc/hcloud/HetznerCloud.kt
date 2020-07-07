package dev.freggy.hcmc.hcloud

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.*
import io.ktor.http.URLBuilder
import io.ktor.http.takeFrom

class HetznerCloud(private val token: String) {
    private val basePath = "https://api.hetzner.cloud/v1"
    val client = HttpClient(OkHttp) {
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
        defaultRequest {
            header("Authorization", "Bearer $token")
            url.takeFrom(URLBuilder().takeFrom(basePath).apply {
                encodedPath += url.encodedPath
            })
        }
    }

    fun servers(): ServerClient = ServerClient(this.client)
    fun serverActions(): ServerActionClient = ServerActionClient(this.client)

}