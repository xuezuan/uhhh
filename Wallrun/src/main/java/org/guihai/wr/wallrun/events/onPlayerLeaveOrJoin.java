package org.guihai.wr.wallrun.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.guihai.wr.wallrun.Scoreboard;
import org.guihai.wr.wallrun.WallRun;


import org.bukkit.Location;

import java.util.*;

public class onPlayerLeaveOrJoin implements Listener {

    BlocksPlacedByPlayers blocksPlacedByPlayers;

    public double x = 0.5;
    public int player = 0;



    public onPlayerLeaveOrJoin(BlocksPlacedByPlayers blocksPlacedByPlayers) {
        this.blocksPlacedByPlayers = blocksPlacedByPlayers;
    }


    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        e.setQuitMessage(null);
        this.blocksPlacedByPlayers.rm_normal(e.getPlayer());
        if (player != 0) {
            x = x - 14;
            player = player - 1;
        }
    }
}
