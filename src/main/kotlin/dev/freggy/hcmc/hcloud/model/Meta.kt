package dev.freggy.hcmc.hcloud.model

import com.google.gson.annotations.SerializedName

data class Meta(val pagination: Pagination)

data class Pagination(
    val page: Int,
    @SerializedName("last_page") val lastPage: Int,
    @SerializedName("next_page") val nextPage: Int
)
