package dev.freggy.hcmc

import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

class PluginContext(val plugin: HcmcPlugin) : AbstractCoroutineContextElement(PluginContext) {
    companion object Key : CoroutineContext.Key<PluginContext>
}
