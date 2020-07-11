package dev.freggy.hcmc

import dev.freggy.hcmc.hcloud.model.Server
import org.bukkit.Location

data class PlacedServer(val inner: Server, val location: Location)
