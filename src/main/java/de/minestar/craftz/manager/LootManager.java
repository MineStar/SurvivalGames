package de.minestar.craftz.manager;

import java.io.File;
import java.util.ArrayList;

import net.minecraft.server.v1_4_6.ItemStack;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_4_6.inventory.CraftItemStack;

import de.minestar.craftz.Core;
import de.minestar.craftz.data.Loot;
import de.minestar.craftz.data.SurvivalPlayer;
import de.minestar.craftz.data.loot.AbstractLootspot;
import de.minestar.craftz.data.loot.LootChest;
import de.minestar.craftz.data.loot.LootDispenser;
import de.minestar.craftz.utils.Chat;
import de.minestar.craftz.utils.InventoryHandler;
import de.minestar.craftz.utils.LocationUtils;

public class LootManager {

    private ArrayList<AbstractLootspot> lootspots;
    private File dataFolder;

    public LootManager(String gameName) {
        this.dataFolder = new File(Core.INSTANCE.getDataFolder() + System.getProperty("file.separator") + gameName);
        this.dataFolder.mkdir();

        this.dataFolder = new File(this.dataFolder + System.getProperty("file.separator") + "loot");
        this.dataFolder.mkdir();

        this.lootspots = new ArrayList<AbstractLootspot>();
        this.loadLootspots();
    }

    private void loadLootspots() {
        File[] files = this.dataFolder.listFiles();
        int lootItemCount = 0;
        for (File file : files) {
            try {
                String fileName = file.getName().trim();
                if (fileName.endsWith(".dat")) {
                    // get the location
                    Location location = LocationUtils.fromString(fileName.replace(".dat", ""));
                    if (location == null) {
                        Chat.printMessage(ChatColor.RED, "Unable to load lootspot @ " + fileName.replace(".dat", ""));
                        continue;
                    }

                    // is the lootspot valid?
                    boolean isChest = location.getBlock().getType().equals(Material.CHEST);
                    boolean isDispenser = location.getBlock().getType().equals(Material.DISPENSER);
                    if (!isChest && !isDispenser) {
                        Chat.printMessage(ChatColor.RED, "Lootspot @ " + fileName.replace(".dat", "") + " is invalid!");
                        continue;
                    }

                    // load loot
                    ArrayList<Loot> lootList = new ArrayList<Loot>();
                    ItemStack[] itemList = InventoryHandler.loadInventory(file);
                    for (ItemStack stack : itemList) {
                        if (stack != null) {
                            lootList.add(new Loot(CraftItemStack.asCraftMirror(stack)));
                            lootItemCount++;
                        }
                    }

                    // add to lootlist
                    if (isChest) {
                        this.lootspots.add(new LootChest(this.dataFolder, location, lootList));
                    } else if (isDispenser) {
                        this.lootspots.add(new LootDispenser(this.dataFolder, location, lootList));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Chat.printMessage(ChatColor.GREEN, "Loaded " + this.lootspots.size() + " chests with " + lootItemCount + " items!");
    }

    public boolean addChest(Location location) {
        if (this.getLootspot(location) != null) {
            return false;
        }
        this.lootspots.add(new LootChest(this.dataFolder, location, new ArrayList<Loot>()));
        return true;
    }

    public boolean addDispenser(Location location) {
        if (this.getLootspot(location) != null) {
            return false;
        }
        this.lootspots.add(new LootDispenser(this.dataFolder, location, new ArrayList<Loot>()));
        return true;
    }

    public AbstractLootspot getLootspot(Location location) {
        for (AbstractLootspot lootSpot : this.lootspots) {
            if (LocationUtils.equals(lootSpot.getLocation(), location)) {
                return lootSpot;
            }
        }
        return null;
    }

    public void teleportToLootspot(int ID, SurvivalPlayer player) {
        int c = 0;
        for (AbstractLootspot lootSpot : this.lootspots) {
            if (ID == c) {
                player.teleport(lootSpot.getLocation());
                player.broadcast(ChatColor.GREEN + "Teleport to lootspot #" + ID + " of " + this.lootspots.size());
                return;
            }
            c++;
        }
        player.broadcast(ChatColor.GREEN + "Total lootspots: " + this.lootspots.size());
    }

    public void refillLootspots() {
        for (AbstractLootspot lootSpot : this.lootspots) {
            lootSpot.respawnLoot();
        }
    }

    public void clearAllLootSpots() {
        for (AbstractLootspot lootSpot : this.lootspots) {
            lootSpot.clearLootspot();
        }
    }
}
