package winlyps.wetFarmlandForever

import org.bukkit.Material
import org.bukkit.block.data.type.Farmland
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockFadeEvent
import org.bukkit.event.block.BlockPhysicsEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.block.MoistureChangeEvent
import org.bukkit.event.entity.EntityInteractEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.plugin.java.JavaPlugin

class WetFarmlandForever : JavaPlugin(), Listener {

    override fun onEnable() {
        // Register event listeners
        server.pluginManager.registerEvents(this, this)
        logger.info("WetFarmlandForever plugin has been enabled!")
    }

    override fun onDisable() {
        logger.info("WetFarmlandForever plugin has been disabled!")
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        // Make farmland instantly wet when placed
        if (event.block.type == Material.FARMLAND) {
            makeFarmlandWet(event.block)
        }
    }

    @EventHandler
    fun onBlockPhysics(event: BlockPhysicsEvent) {
        // Prevent farmland from turning into dirt due to lack of water
        if (event.block.type == Material.FARMLAND) {
            event.isCancelled = true
            // Ensure farmland stays wet
            makeFarmlandWet(event.block)
        }
    }

    @EventHandler
    fun onBlockFade(event: BlockFadeEvent) {
        // Prevent farmland from fading/changing state over time
        if (event.block.type == Material.FARMLAND) {
            event.isCancelled = true
            // Ensure farmland stays wet
            makeFarmlandWet(event.block)
        }
    }

    @EventHandler
    fun onMoistureChange(event: MoistureChangeEvent) {
        // Prevent farmland moisture level changes that could lead to it turning to dirt
        event.isCancelled = true
        // Keep farmland wet
        makeFarmlandWet(event.block)
    }

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        // Prevent players from trampling farmland
        val clickedBlock = event.clickedBlock ?: return
        if (clickedBlock.type == Material.FARMLAND && event.action == org.bukkit.event.block.Action.PHYSICAL) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onEntityInteract(event: EntityInteractEvent) {
        // Prevent entities (like mobs) from trampling farmland
        if (event.block.type == Material.FARMLAND) {
            event.isCancelled = true
        }
    }

    private fun makeFarmlandWet(block: org.bukkit.block.Block) {
        if (block.type == Material.FARMLAND) {
            val farmlandData = block.blockData as Farmland
            farmlandData.moisture = farmlandData.maximumMoisture
            block.blockData = farmlandData
        }
    }
}