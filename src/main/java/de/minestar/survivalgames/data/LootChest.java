package de.minestar.survivalgames.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import de.minestar.survivalgames.utils.LocationUtils;

public class LootChest {

    private final Location location;
    private ArrayList<Loot> lootList = new ArrayList<Loot>();
    private final File dataFolder;
    private Random random = new Random();

    public LootChest(File dataFolder, Location location, ArrayList<Loot> lootList) {
        this.dataFolder = dataFolder;
        this.location = location;
        this.lootList = lootList;
    }

    public Chest getChest() {
        if (!location.getBlock().getType().equals(Material.CHEST)) {
            return null;
        }
        return (Chest) (location.getBlock().getState());
    }

    public void showLoot() {
        Chest chest = this.getChest();
        if (chest == null) {
            return;
        }

        this.clear();
        int index = 0;
        for (Loot loot : this.lootList) {
            chest.getBlockInventory().setItem(index, loot.toItemStack());
            index++;
        }
    }

    public void clear() {
        Chest chest = this.getChest();
        if (chest == null) {
            return;
        }
        for (int itemIndex = 0; itemIndex < chest.getBlockInventory().getSize(); itemIndex++) {
            chest.getBlockInventory().setItem(itemIndex, null);
        }
    }

    private void fillWithRandomItems() {
        Chest chest = this.getChest();
        if (chest == null) {
            return;
        }

        int itemAmount = 0;
        double randomNumber = this.random.nextDouble();
        if (randomNumber < 0.15d) {
            itemAmount = 1;
        } else if (randomNumber < 0.52d) {
            itemAmount = 2;
        } else if (randomNumber < 0.72d) {
            itemAmount = 3;
        } else if (randomNumber < 0.83d) {
            itemAmount = 4;
        } else if (randomNumber <= 0.91d) {
            itemAmount = 5;
        } else if (randomNumber <= 0.95d) {
            itemAmount = 6;
        } else {
            itemAmount = 2;
        }

        int done = 0;
        int count = 0;
        while (done < itemAmount && done < this.lootList.size() && count < 20) {
            count++;
            Loot loot = this.lootList.get((int) (this.random.nextDouble() * this.lootList.size()));
            if (loot.isUsed()) {
                continue;
            }
            loot.setUsed(true);
            int itemSlot = (int) (this.random.nextDouble() * 27);
            if (chest.getBlockInventory().getItem(itemSlot) == null || chest.getBlockInventory().getItem(itemSlot).getType().equals(Material.AIR)) {
                chest.getBlockInventory().setItem(itemSlot, loot.toItemStack());
                done++;
            }
        }
        for (Loot loot : this.lootList) {
            loot.setUsed(false);
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

    public void updateLootContents() {
        Chest chest = this.getChest();
        if (chest == null) {
            return;
        }

        this.lootList.clear();
        ItemStack[] contents = chest.getBlockInventory().getContents();
        for (ItemStack stack : contents) {
            if (stack == null || stack.getType().equals(Material.AIR)) {
                continue;
            }
            this.lootList.add(new Loot(stack.getTypeId(), stack.getDurability(), stack.getAmount()));
        }
    }

    public void saveChest() {
        File file = new File(this.dataFolder, LocationUtils.toString(this.location) + ".txt");
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
