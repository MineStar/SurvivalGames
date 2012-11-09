package de.minestar.survivalgames.manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;

import de.minestar.survivalgames.Core;
import de.minestar.survivalgames.data.Loot;
import de.minestar.survivalgames.data.LootChest;
import de.minestar.survivalgames.utils.Chat;
import de.minestar.survivalgames.utils.LocationUtils;

public class LootManager {

    private ArrayList<LootChest> chestList;

    public void onEnable() {
        this.chestList = new ArrayList<LootChest>();
        this.loadChests();
    }

    private void loadChests() {
        File[] files = new File(Core.INSTANCE.getDataFolder() + System.getProperty("path.separator") + "loot").listFiles();
        int itemCount = 0;
        for (File f : files) {
            try {
                String fileName = f.getName().trim();
                // FILESYNTAX: config_%worldname%.yml
                if (fileName.endsWith(".txt")) {
                    // get the location
                    Location location = LocationUtils.fromString(fileName.replace(".txt", ""));
                    if (location == null) {
                        Chat.printMessage(ChatColor.RED, "Unable to load chest @ " + fileName.replace(".txt", ""));
                        continue;
                    }

                    // read file
                    BufferedReader reader = new BufferedReader(new FileReader(f));
                    String zeile = "";
                    ArrayList<Loot> lootList = new ArrayList<Loot>();
                    while ((zeile = reader.readLine()) != null) {
                        String[] split = zeile.split("_");
                        if (split.length != 3) {
                            Chat.printMessage(ChatColor.RED, "Unable to load loot " + zeile + " @ " + fileName.replace(".txt", ""));
                            continue;
                        }

                        // get loot-data
                        int typeID = Integer.valueOf(split[0]);
                        short subID = Short.valueOf(split[1]);
                        int amount = Integer.valueOf(split[2]);

                        lootList.add(new Loot(typeID, subID, amount));
                        itemCount++;
                    }
                    this.chestList.add(new LootChest(location, lootList));
                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Chat.printMessage(ChatColor.GREEN, "Loaded " + this.chestList.size() + " chests with " + itemCount + " items!");
    }

    public void onDisable() {

    }

    public void startGame() {

    }

    public void endGame() {
    }
}
