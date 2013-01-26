package de.minestar.craftz.data.loot;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_4_6.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.minestar.craftz.data.Loot;

public class LootChest extends AbstractLootspot {

    public LootChest(File dataFolder, Location location, ArrayList<Loot> lootList) {
        super(dataFolder, location, lootList, 27);
    }

    public Chest getChest() {
        if (!location.getBlock().getType().equals(Material.CHEST)) {
            return null;
        }
        return (Chest) (location.getBlock().getState());
    }

    @Override
    public void showLoot() {
        Chest chest = this.getChest();
        if (chest == null) {
            return;
        }

        this.clearLootspot();
        int index = 0;
        for (Loot loot : this.lootList) {
            chest.getBlockInventory().setItem(index, loot.getItemStack());
            index++;
        }
    }

    @Override
    public void clearLootspot() {
        Chest chest = this.getChest();
        if (chest == null) {
            return;
        }
        for (int itemIndex = 0; itemIndex < chest.getBlockInventory().getSize(); itemIndex++) {
            chest.getBlockInventory().setItem(itemIndex, null);
        }
    }

    @Override
    protected void fillWithRandomItems() {
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
            int itemSlot = (int) (this.random.nextDouble() * INVENTORY_SIZE);
            if (chest.getBlockInventory().getItem(itemSlot) == null || chest.getBlockInventory().getItem(itemSlot).getType().equals(Material.AIR)) {
                chest.getBlockInventory().setItem(itemSlot, loot.getItemStack());
                done++;
            }
        }
        for (Loot loot : this.lootList) {
            loot.setUsed(false);
        }
    }

    @Override
    public void updateLootContentsFromInventory() {
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
            this.lootList.add(new Loot(stack));
        }
    }

    @Override
    protected CraftInventory getCraftInventory() {
        Chest chest = this.getChest();
        if (chest == null) {
            return null;
        }
        Inventory inventory = chest.getBlockInventory();
        return (CraftInventory) inventory;
    }

}
