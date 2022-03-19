package org.guihai.wr.wallrun;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

import org.guihai.wr.wallrun.events.*;

import java.io.File;
import java.io.IOException;
import java.util.UUID;


public final class WallRun extends JavaPlugin implements Listener {

    File dataLib = new File(getDataFolder()+"/player.yml");
    FileConfiguration data = YamlConfiguration.loadConfiguration(dataLib);
    onWin ow;
    public WallRun(onWin ow) {
        this.ow = ow;
    }


    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("M-1 loaded");

        BlocksPlacedByPlayers blocksPlacedByPlayers = new BlocksPlacedByPlayers();
        Scoreboard b = new Scoreboard();

        getServer().getPluginManager().registerEvents(new onPlayerLeaveOrJoin(blocksPlacedByPlayers), this);
        getServer().getPluginManager().registerEvents(new Blocks(blocksPlacedByPlayers, this), this);
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new onWin(blocksPlacedByPlayers,this, b), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        savedata(data, dataLib);
    }
    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        e.setCancelled(true);
    }


    public void savedata(FileConfiguration ymlConfig, File ymlFile) {
        try {
            ymlConfig.save(ymlFile);
        } catch (IOException e) {
            getLogger().severe("ERROR! Unable to save Wallrun data file! Error:");
            e.printStackTrace();
        }
    }
    public void addAtt(UUID uuid) {
        String path = uuid + ".attempts";
        if (data.get(path) == null) {
            data.set(path, 0);
        }else {
            data.set(path, data.getInt(path) + 1);
        }
    }
    public void addPll(UUID uuid, Player player) {
        String path = uuid + ".name";
        data.set(path, player.getName());
    }
    public void addDia(UUID uuid) {
        String path = uuid + ".diamonds";
        if (data.get(path) == null) {
            data.set(path, 0);
        }else {
            data.set(path, data.getInt(path) + 1);
        }
    }
}
