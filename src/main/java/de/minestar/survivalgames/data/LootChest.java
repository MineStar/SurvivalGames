package de.minestar.survivalgames.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;

import de.minestar.survivalgames.Core;
import de.minestar.survivalgames.utils.LocationUtils;

public class LootChest {

    private final Location location;
    private ArrayList<Loot> lootList = new ArrayList<Loot>();
    private Random random = new Random();

    public LootChest(Location location, ArrayList<Loot> lootList) {
        this.location = location;
        this.lootList = lootList;
    }

    public Chest getChest() {
        if (!location.getBlock().getType().equals(Material.CHEST)) {
            return null;
        }
        return (Chest) (location.getBlock().getState());
    }

    public void clear() {
        Chest chest = this.getChest();
        if (chest == null) {
            return;
        }
        chest.getBlockInventory().clear();
    }

    private void fillWithRandomItems() {
        Chest chest = this.getChest();
        if (chest == null) {
            return;
        }

        int itemAmount = (int) Math.max(2, this.random.nextDouble() * 5 + 1);
        for (int count = 0; count < itemAmount; itemAmount++) {
            Loot loot = this.lootList.get((int) (this.random.nextDouble() * this.lootList.size()));
            int itemSlot = (int) (this.random.nextDouble() * 27);
            if (chest.getBlockInventory().getItem(itemSlot) == null || chest.getBlockInventory().getItem(itemSlot).getType().equals(Material.AIR)) {
                chest.getBlockInventory().setItem(itemSlot, loot.toItemStack());
            } else {
                count--;
                continue;
            }
        }
    }

    public void refill() {
        this.clear();
        this.fillWithRandomItems();
    }

    public ArrayList<Loot> getLoot() {
        return lootList;
    }

    public Location getLocation() {
        return location;
    }

    public void saveChest() {
        File file = new File(Core.INSTANCE.getDataFolder() + System.getProperty("file.separator") + "loot", LocationUtils.toString(this.location) + ".txt");
        file.mkdir();
        if (file.exists()) {
            file.delete();
        }

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            for (Loot loot : this.lootList) {
                bw.write(loot.toString());
                bw.newLine();
            }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
