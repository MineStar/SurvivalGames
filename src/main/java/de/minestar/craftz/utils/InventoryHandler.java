package de.minestar.craftz.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import net.minecraft.server.v1_4_6.ItemStack;
import net.minecraft.server.v1_4_6.NBTTagCompound;
import net.minecraft.server.v1_4_6.NBTTagList;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_4_6.inventory.CraftInventory;

public class InventoryHandler {

    public static void saveInventory(File file, CraftInventory inventory) {
        try {
            if (file.exists()) {
                file.delete();
            }

            NBTTagCompound compound = new NBTTagCompound();
            NBTTagList stackTagList = new NBTTagList();

            ItemStack[] contents = inventory.getInventory().getContents();
            for (ItemStack stack : contents) {
                if (stack == null || stack.id == Material.AIR.getId()) {
                    continue;
                }
                NBTTagCompound itemCompound = new NBTTagCompound();
                stack.save(itemCompound);
                stackTagList.add(itemCompound);
            }
            compound.set("Items", stackTagList);
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file));
            CompressedStreamTools.writeGzippedCompoundToOutputStream(compound, stream);
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ItemStack[] loadInventory(File file) {
        try {
            if (!file.exists()) {
                return new ItemStack[0];
            }
            NBTTagCompound tagCompound = CompressedStreamTools.loadGzippedCompoundFromOutputStream(new FileInputStream(file));

            NBTTagList itemList = tagCompound.getList("Items");
            ItemStack[] items = new ItemStack[itemList.size()];
            for (int i = 0; i < itemList.size(); i++) {
                NBTTagCompound nbttagcompound1 = (NBTTagCompound) itemList.get(i);
                items[i] = ItemStack.a(nbttagcompound1);
            }
            return items;
        } catch (Exception e) {
            e.printStackTrace();
            return new ItemStack[0];
        }
    }
}
