package de.minestar.survivalgames.data;

import org.bukkit.inventory.ItemStack;

public class Loot {
    private final int typeID;
    private final short subID;
    private final int amount;

    public Loot(int typeID, short subID, int amount) {
        this.typeID = typeID;
        this.subID = subID;
        this.amount = amount;
    }

    /**
     * @return the typeID
     */
    public int getTypeID() {
        return typeID;
    }

    /**
     * @return the subID
     */
    public short getSubID() {
        return subID;
    }

    /**
     * @return the amount
     */
    public int getAmount() {
        return amount;
    }

    /**
     * @return the itemstack
     */
    public ItemStack toItemStack() {
        ItemStack itemStack = new ItemStack(typeID);
        itemStack.setAmount(amount);
        itemStack.setDurability(subID);
        return itemStack;
    }

    @Override
    public String toString() {
        return this.typeID + "_" + this.subID + "_" + this.amount;
    }
}
