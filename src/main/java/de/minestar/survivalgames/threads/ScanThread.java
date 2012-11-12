package de.minestar.survivalgames.threads;

import java.util.ArrayList;
import java.util.TimerTask;

import org.bukkit.ChatColor;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Material;

import de.minestar.survivalgames.data.LootChest;
import de.minestar.survivalgames.data.SurvivalGame;

public class ScanThread extends TimerTask {

    private final SurvivalGame game;
    private final ArrayList<ChunkSnapshot> snapList;

    public ScanThread(SurvivalGame game, ArrayList<ChunkSnapshot> snapList) {
        this.game = game;
        this.snapList = snapList;
    }

    @Override
    public void run() {
        int createCount = 0, updateCount = 0;
        int chunkCount = this.snapList.size();
        int currentNumber = 1;
        for (ChunkSnapshot snapshot : this.snapList) {
            for (int bX = 0; bX < 16; bX++) {
                for (int bZ = 0; bZ < 16; bZ++) {
                    for (int bY = 0; bY < 256; bY++) {
                        if (snapshot.getBlockTypeId(bX, bY, bZ) == Material.CHEST.getId()) {
                            // save/update the chest
                            LootChest chest = game.getLootManager().getChest(game.getSettings().getSpectatorSpawn().getLocation().getWorld().getBlockAt(snapshot.getX() * 16 + bX, bY, snapshot.getZ() * 16 + bZ).getLocation());
                            if (chest != null) {
                                chest.updateLootContents();
                                chest.saveChest();
                                updateCount++;
                            } else {
                                game.getLootManager().addChest(game.getSettings().getSpectatorSpawn().getLocation().getWorld().getBlockAt(snapshot.getX() * 16 + bX, bY, snapshot.getZ() * 16 + bZ).getLocation());
                                chest = game.getLootManager().getChest(game.getSettings().getSpectatorSpawn().getLocation().getWorld().getBlockAt(snapshot.getX() * 16 + bX, bY, snapshot.getZ() * 16 + bZ).getLocation());
                                chest.updateLootContents();
                                chest.saveChest();
                                createCount++;
                            }
                        }
                    }
                }
            }
            if (currentNumber % 200 == 0) {
                game.broadcast(ChatColor.GRAY + "Status (2/2): Chunk " + currentNumber + " of " + chunkCount);
            }
            currentNumber++;
        }

        // send info
        game.broadcast(ChatColor.GREEN + "New chests: " + createCount);
        game.broadcast(ChatColor.GREEN + "Updated chests: " + updateCount);
    }

}
