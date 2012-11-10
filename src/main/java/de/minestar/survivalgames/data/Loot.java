package de.minestar.survivalgames.data;

import org.bukkit.inventory.ItemStack;

public class Loot {
    private final int typeID;
    private final short subID;
    private final int amount;
    private boolean used = false;

    public Loot(int typeID, short subID, int amount) {
        this.typeID = typeID;
        this.subID = subID;
        this.amount = amount;
    }

    /**
     * @return the used
     */
    public boolean isUsed() {
        return used;
    }

    /**
     * @param used
     *            the used to set
     */
    public void setUsed(boolean used) {
        this.used = used;
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
