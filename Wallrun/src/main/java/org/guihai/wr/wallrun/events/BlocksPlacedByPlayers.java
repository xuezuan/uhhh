package org.guihai.wr.wallrun.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BlocksPlacedByPlayers {
    public  Map<Player, Set<Block>> blocks;

    public BlocksPlacedByPlayers() {
        this.blocks = new HashMap<>();
    }

    public void register(Player player, Block block) {
        this.blocks.computeIfAbsent(player, b -> new HashSet<>()).add(block);
    }

    public void rm_normal(Player p) {
        Set<Block> blocks = this.blocks.get(p);
        if(blocks == null || blocks.isEmpty()) return;
        blocks.forEach(b -> b.setType(Material.AIR));
    }
}
