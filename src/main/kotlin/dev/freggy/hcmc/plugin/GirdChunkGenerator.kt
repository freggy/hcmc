package dev.freggy.hcmc.plugin

import org.bukkit.Material
import org.bukkit.World
import org.bukkit.generator.ChunkGenerator
import java.util.*

class GirdChunkGenerator : ChunkGenerator() {
    override fun generateChunkData(world: World, random: Random, x: Int, z: Int, biome: BiomeGrid): ChunkData {
        val chunk = createChunkData(world)

        // draw borders
        for (i in 0..15) {
            chunk.setBlock(i, 3, 0, Material.BEDROCK)
            chunk.setBlock(15, 3, i, Material.BEDROCK)
            chunk.setBlock(0, 3, i, Material.BEDROCK)
            chunk.setBlock(i, 3, 15, Material.BEDROCK)
        }

        // fill rest of chunk
        for (xx in 0..15) {
            for (zz in 0..15) {
                if (chunk.getType(xx, 3, zz) != Material.BEDROCK) {
                    chunk.setBlock(xx, 3, zz, Material.GRASS_BLOCK)
                }
            }
        }

        return chunk
    }
}
