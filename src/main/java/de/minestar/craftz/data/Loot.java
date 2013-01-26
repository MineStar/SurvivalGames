package de.minestar.craftz.data;

import org.bukkit.inventory.ItemStack;

public class Loot {

    private transient boolean used = false;
    private ItemStack itemStack;

    public Loot(ItemStack stack) {
        this.itemStack = stack.clone();
        this.used = false;
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
        return itemStack.getTypeId();
    }

    /**
     * @return the subID
     */
    public short getSubID() {
        return itemStack.getDurability();
    }

    /**
     * @return the amount
     */
    public int getAmount() {
        return itemStack.getAmount();
    }

    /**
     * @return the itemstack
     */
    public ItemStack getItemStack() {
        return this.itemStack.clone();
    }
}
