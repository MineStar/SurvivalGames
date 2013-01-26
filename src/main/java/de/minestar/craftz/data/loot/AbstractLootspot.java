package de.minestar.craftz.data.loot;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_4_6.inventory.CraftInventory;

import de.minestar.craftz.data.Loot;
import de.minestar.craftz.utils.Chat;
import de.minestar.craftz.utils.InventoryHandler;
import de.minestar.craftz.utils.LocationUtils;

public abstract class AbstractLootspot {

    protected final File dataFolder;
    protected final Location location;
    protected ArrayList<Loot> lootList = new ArrayList<Loot>();
    protected final Random random;

    protected final int INVENTORY_SIZE;
    protected final int BLOCK_ID;
    protected final byte SUB_ID;

    public AbstractLootspot(File dataFolder, Location location, ArrayList<Loot> lootList, int blockID, byte subID, int invSize) {
        this.dataFolder = dataFolder;
        this.location = location;
        this.lootList = lootList;
        this.BLOCK_ID = blockID;
        this.SUB_ID = subID;
        this.INVENTORY_SIZE = invSize;
        this.random = new Random();
    }

    protected abstract CraftInventory getCraftInventory();

    protected abstract void fillWithRandomItems();

    public abstract void showLoot();

    public abstract void clearLootspot();

    public abstract void updateLootContentsFromInventory();

    public final Location getLocation() {
        return location;
    }

    public final ArrayList<Loot> getLoot() {
        return lootList;
    }

    public final void respawnLoot() {
        this.clearLootspot();
        this.fillWithRandomItems();
    }

    public final void saveLoot() {
        File file = new File(this.dataFolder, LocationUtils.toString(this.location, this.BLOCK_ID + "_" + this.SUB_ID) + ".dat");
        file.mkdir();
        if (file.exists()) {
            file.delete();
        }

        try {
            CraftInventory inventory = this.getCraftInventory();
            if (inventory != null) {
                InventoryHandler.saveInventory(file, inventory);
            } else {
                Chat.printMessage(ChatColor.RED, "Could not get lootinventory!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
