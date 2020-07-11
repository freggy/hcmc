package dev.freggy.hcmc.hcloud.model

import com.google.gson.annotations.SerializedName

data class ActionPage(val actions: Array<Action>, val meta: Meta)

data class Action(
    val id: Int,
    val command: ActionCommand,
    val status: ActionStatus,
    val progress: Int,
    val started: String,
    val finished: String,
    val resources: Array<Resource>
)

data class Resource(val id: Int, val type: String)

enum class ActionStatus {
    @SerializedName("success")
    SUCCESS,

    @SerializedName("running")
    RUNNING,

    @SerializedName("error")
    ERROR
}

enum class ActionCommand(val isServerStatus: Boolean, val isServerPresence: Boolean) {
    @SerializedName("stop_server")
    STOP_SERVER(true, false),

    @SerializedName("delete_server")
    DELETE_SERVER(false, true),

    @SerializedName("start_server")
    START_SERVER(true, false),

    @SerializedName("create_server")
    CREATE_SERVER(false, true),

    @SerializedName("change_server_type")
    CHANGE_SERVER_TYPE(true, false)

}
