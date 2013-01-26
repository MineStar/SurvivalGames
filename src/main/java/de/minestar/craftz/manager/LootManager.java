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
            String fileName = file.getName().trim();
            String cleanFileName = fileName.replace(".dat", "").replace("_", " , ");
            try {
                if (fileName.endsWith(".dat")) {
                    // get the location
                    Location location = LocationUtils.fromString(fileName.replace(".dat", ""));
                    if (location == null) {
                        Chat.printMessage(ChatColor.RED, "Unable to load lootspot @ " + cleanFileName);
                        continue;
                    }

                    // get the material
                    String split[] = fileName.replace(".dat", "").split("_");
                    int typeID = Integer.valueOf(split[6]);
                    byte subID = Byte.valueOf(split[7]);

                    // is the lootspot valid?
                    if (location.getBlock().getTypeId() != typeID || location.getBlock().getData() != subID) {
                        location.getBlock().setTypeIdAndData(typeID, subID, false);
                        location.getBlock().setData(subID, false);
                        Chat.printMessage(ChatColor.RED, "Recreating lootspot @ " + cleanFileName);
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
                    AbstractLootspot lootSpot = this.addLootSpot(location);
                    if (lootSpot != null) {
                        this.lootspots.add(lootSpot);
                    } else {
                        Chat.printMessage(ChatColor.RED, "Unable to load lootspot @ " + cleanFileName);
                    }
                }
            } catch (Exception e) {
                Chat.printMessage(ChatColor.RED, "Unable to load lootspot @ " + cleanFileName);
                e.printStackTrace();
            }
        }
        Chat.printMessage(ChatColor.GREEN, "Loaded " + this.lootspots.size() + " lootspots with " + lootItemCount + " items!");
    }

    public AbstractLootspot addLootSpot(Location location) {
        if (this.getLootspot(location) != null) {
            return null;
        }

        final int typeID = location.getBlock().getTypeId();
        switch (typeID) {
            case 54 : { // CHEST
                return this.addChest(location);
            }
            case 23 : { // DISPENSER
                return this.addDispenser(location);
            }
            default : {
                return null;
            }
        }
    }

    private AbstractLootspot addChest(Location location) {
        AbstractLootspot lootSpot = new LootChest(this.dataFolder, location, new ArrayList<Loot>(), location.getBlock().getData());
        this.lootspots.add(lootSpot);
        return lootSpot;
    }

    private AbstractLootspot addDispenser(Location location) {
        AbstractLootspot lootSpot = new LootDispenser(this.dataFolder, location, new ArrayList<Loot>(), location.getBlock().getData());
        this.lootspots.add(lootSpot);
        return lootSpot;
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
