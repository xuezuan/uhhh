package org.guihai.wr.wallrun.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.guihai.wr.wallrun.Scoreboard;
import org.guihai.wr.wallrun.WallRun;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;


public class onWin implements Listener {
    public ArrayList<Player> jWin = new ArrayList<>();
    public ArrayList<Player> att = new ArrayList<>();
    Map<Player, Integer> blockPlaced = new HashMap<>();

    BlocksPlacedByPlayers blocksManager;
    WallRun main;
    Scoreboard b;
    ArrayList<Player> inTimer = new ArrayList<>();
    Map<Player, Double> time = new HashMap<>();
    Map<Player, Double> lb = new HashMap<>();
    DecimalFormat df = new DecimalFormat("0.000");
    boolean started = false;
    public double x = 0.5;
    public int player = 0;

    public onWin(BlocksPlacedByPlayers blocksPlacedByPlayers, WallRun main, Scoreboard b) {
        this.blocksManager = blocksPlacedByPlayers;
        this.main = main;
        this.b = b;
    }

    @EventHandler
    public void join(PlayerJoinEvent e) {
        if (!started) {
            kTimer();
            System.out.println("[Main] WallRun started!");
        }
        double bl = 0.000;
        int blo = 0;
        if (!e.getPlayer().hasPlayedBefore()) {
            main.addAtt(e.getPlayer().getUniqueId());
            main.addDia(e.getPlayer().getUniqueId());
        }

        Location location = new Location(main.getServer().getWorld("world"), x, 21, -0.5, 180, 0);
        Location location2 = new Location(main.getServer().getWorld("world"), x, 100, -0.5, 180, 0);
        e.getPlayer().setBedSpawnLocation(location2, true);
        e.getPlayer().teleport(location);
        e.setJoinMessage(null);

        x = x + 14;
        player = player + 1;
        Double block = 0.000;
        Integer blocks = 0;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (att.contains(e.getPlayer())) {
            att.add(e.getPlayer());
        }
        if (!jWin.contains(e.getPlayer())) {
            if (!inTimer.contains(e.getPlayer())) {
                inTimer.add(e.getPlayer());
            }
            if (blockPlaced.containsKey(e.getPlayer())) {
                blockPlaced.put(e.getPlayer(), blockPlaced.get(e.getPlayer()) + 1);
            } else {
                blockPlaced.put(e.getPlayer(), 1);
            }
        }
    }

    public void kTimer() {
        started = true;
        int timer = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, () -> {
            if (inTimer == null || inTimer.isEmpty()) return;
            inTimer.forEach(p -> {
                if (time.containsKey(p)){
                    time.put(p, time.get(p) + 0.05);
                }else {
                    time.put(p, 0.05);
                }
            });
        }, 0, 1);
    }


    @EventHandler
    public void OnWin(PlayerMoveEvent e) {
        if (this.jWin.contains(e.getPlayer())) return;
        Player p = e.getPlayer();
        if (!(p.getLocation().getY() > 13.0D)) {
            this.fail(p);
        }

        if (!p.hasPermission("access.op")) {
            if (p.getLocation().getZ() >= 2.0D) {
                this.fail(p);
                return;
            }

            if (p.getLocation().getX() >= p.getBedSpawnLocation().getX() + 5.0D) {
                this.fail(p);
                return;
            }

            if (p.getLocation().getX() <= p.getBedSpawnLocation().getX() - 5.0D) {
                this.fail(p);
                return;
            }

            if (p.getLocation().getY() >= 36.0D) {
                this.fail(p);
                return;
            }

            if (p.getLocation().getZ() <= -42.0D) {
                this.fail(p);
                return;
            }
        }

        if (!(p.getLocation().getY() > 13.0D)) {
            this.fail(p);
            return;
        }


        if (!(p.getLocation().getBlock().getType().equals(Material.GOLD_PLATE))) return;
        if (!(p.getLocation().getX() <= p.getBedSpawnLocation().getX() + 3)) return;
        if (!(p.getLocation().getX() >= p.getBedSpawnLocation().getX() - 3)) return;
        inTimer.remove(p);
        if (lb.containsKey(p)) {
            if (lb.get(p) > time.get(p)) {
                lb.put(p, time.get(p));
            }
        }else {
            lb.put(p, time.get(p));
        }
        PacketPlayOutTitle length = new PacketPlayOutTitle(5, 45, 15);
        time.putIfAbsent(p, 0.000);
        PacketPlayOutTitle ttl = new PacketPlayOutTitle(EnumTitleAction.TITLE, ChatSerializer.a("{\"text\":\"§aTime§f: §e" + df.format(time.get(p)) + "\"}"));
        PacketPlayOutTitle ttl1 = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, ChatSerializer.a("{\"text\":\"§eYou have completed the wallrun!\"}"));
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(length);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(ttl);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(ttl1);
        this.jWin.add(p);
        Bukkit.getScheduler().runTaskLater(this.main, () -> {
            win(p);
        }, 35);
    }

    public void win(Player p) {
        inTimer.remove(p);
        time.remove(p);
        blockPlaced.remove(p);
        this.blocksManager.rm_normal(p);
        p.teleport(Objects.requireNonNull(new Location(main.getServer().getWorld("world"), p.getBedSpawnLocation().getX() + 0.5, 21, -0.5, 180, 0)));
        jWin.remove(p);
        if (att.contains(p)) {
            main.addAtt(p.getUniqueId());
        }
    }

    public void fail(Player p) {
        if (att.contains(p)) {
            main.addAtt(p.getUniqueId());
        }
        inTimer.remove(p);
        time.remove(p);
        blockPlaced.remove(p);
        this.blocksManager.rm_normal(p);
        p.teleport(Objects.requireNonNull(new Location(main.getServer().getWorld("world"), p.getBedSpawnLocation().getX() + 0.5, 21, -0.5, 180, 0)));
    }
}
