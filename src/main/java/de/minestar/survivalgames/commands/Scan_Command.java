package de.minestar.survivalgames.commands;

import java.util.ArrayList;
import java.util.Timer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.entity.Player;

import de.minestar.survivalgames.Core;
import de.minestar.survivalgames.data.SurvivalPlayer;
import de.minestar.survivalgames.threads.ScanThread;

public class Scan_Command implements Runnable {

    private int currentX, currentZ;
    private ArrayList<ChunkSnapshot> snapList = new ArrayList<ChunkSnapshot>();
    private Chunk currentChunk;
    private Player sender;
    private SurvivalPlayer sPlayer;
    private int radius, taskID;

    private int totalCount = 0;
    private int current = 1;

    public void execute(Player sender, String[] args) {
        this.sender = sender;
        // check the argumentcount
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Wrong syntax!");
            sender.sendMessage(ChatColor.GRAY + "/game scan <NUMBER>");
            return;
        }

        radius = 100;
        try {
            radius = Integer.valueOf(args[1]);
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Wrong syntax!");
            sender.sendMessage(ChatColor.GRAY + "/game scan <NUMBER>");
            return;
        }

        // get the player
        sPlayer = Core.gameManager.getPlayer(sender.getName());
        if (sPlayer == null) {
            sender.sendMessage(ChatColor.RED + "You are currently not in a survivalgame!");
            return;
        }

        if (!sPlayer.getCurrentGame().isGameInLobby()) {
            sender.sendMessage(ChatColor.RED + "Game is currently running!");
            return;
        }

        // send info
        sender.sendMessage(ChatColor.GREEN + "Scanning world...");
        currentChunk = sender.getLocation().getChunk();

        currentX = -radius;
        currentZ = -radius;

        totalCount = radius * 2 + 1;
        totalCount *= totalCount;
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.INSTANCE, this, 10, 10);
    }

    @Override
    public void run() {
        Chunk chunk;

        currentZ = -radius;
        for (int z = currentZ; z <= radius; z++) {
            if (sender.getWorld().loadChunk(currentChunk.getX() + currentX, currentChunk.getZ() + z, false)) {
                chunk = sender.getWorld().getChunkAt(currentChunk.getX() + currentX, currentChunk.getZ() + z);
                snapList.add(chunk.getChunkSnapshot());
            }
            if (current % 200 == 0) {
                sPlayer.broadcast(ChatColor.GRAY + "Status (1/2): " + current + "/" + totalCount);
            }
            current++;
        }
        if (currentX > radius) {
            Bukkit.getScheduler().cancelTask(this.taskID);
            Timer timer = new Timer();
            timer.schedule(new ScanThread(sPlayer.getCurrentGame(), snapList), 50);
        }
        currentX++;
    }
}
