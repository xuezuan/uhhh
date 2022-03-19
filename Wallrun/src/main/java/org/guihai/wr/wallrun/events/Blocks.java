package org.guihai.wr.wallrun.events;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.guihai.wr.wallrun.WallRun;

import java.util.HashSet;
import java.util.Set;

public class Blocks implements Listener {
    BlocksPlacedByPlayers blocksPlacedByPlayers;
    WallRun plugin;

    public Blocks(BlocksPlacedByPlayers blocksPlacedByPlayers, WallRun plugin) {
        this.blocksPlacedByPlayers = blocksPlacedByPlayers;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent e) {//register block in data set.
        Block b = e.getBlock();
        this.blocksPlacedByPlayers.register(e.getPlayer(), b);
}

    @EventHandler
    public void onDestroyBlock(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (p.hasPermission("access.op")) return;
        Set<Block> playerBlocks = this.blocksPlacedByPlayers.blocks.computeIfAbsent(p, b -> new HashSet<>());
        if (playerBlocks.contains(e.getBlock())) return;
            e.setCancelled(true);
    }


}
