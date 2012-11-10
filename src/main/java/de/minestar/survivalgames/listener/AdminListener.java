package de.minestar.survivalgames.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import de.minestar.survivalgames.Core;
import de.minestar.survivalgames.data.LootChest;
import de.minestar.survivalgames.manager.GameManager;
import de.minestar.survivalgames.manager.LootManager;

public class AdminListener implements Listener {

    private LootManager lootManager;
    private GameManager gameManager;

    public AdminListener() {
        this.lootManager = Core.lootManager;
        this.gameManager = Core.gameManager;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // only if there is no game
        if (this.gameManager.isInGame()) {
            return;
        }

        // only ops are allowed
        if (!event.getPlayer().isOp()) {
            return;
        }

        // only right clicks on a block
        if (!event.getAction().equals(Action.LEFT_CLICK_BLOCK) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        // get the block
        Block block = event.getClickedBlock();

        // only chest interaction
        if (!block.getType().equals(Material.CHEST)) {
            return;
        }

        // we need a bedrock in the hand
        ItemStack stack = event.getPlayer().getItemInHand();
        if (stack == null || !stack.getType().equals(Material.BEDROCK)) {
            if (stack.getType().equals(Material.BOOKSHELF)) {
                LootChest chest = this.lootManager.getChest(block.getLocation());
                if (chest != null) {
                    chest.refill();
                    event.getPlayer().sendMessage(ChatColor.GREEN + "Chest refilled!");
                    event.getPlayer().sendMessage(ChatColor.GRAY + "NOTE: Every chest of a doublechest needs to be refilled!");
                }
            }
            return;
        }

        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            // save/update the chest
            LootChest chest = this.lootManager.getChest(block.getLocation());
            if (chest != null) {
                chest.updateLootContents();
                chest.saveChest();
                event.getPlayer().sendMessage(ChatColor.GREEN + "Chest updated!");
                event.getPlayer().sendMessage(ChatColor.GRAY + "NOTE: Every chest of a doublechest needs to be updated!");
                return;
            } else {
                this.lootManager.addChest(block.getLocation());
                chest = this.lootManager.getChest(block.getLocation());
                chest.updateLootContents();
                chest.saveChest();
                event.getPlayer().sendMessage(ChatColor.GREEN + "Chest created!");
                event.getPlayer().sendMessage(ChatColor.GRAY + "NOTE: Every chest of a doublechest needs to be saved!");
                return;
            }
        } else {
            LootChest chest = this.lootManager.getChest(block.getLocation());
            if (chest != null) {
                chest.showLoot();
                event.getPlayer().sendMessage(ChatColor.GREEN + "Chest is now showing lootcontents!");
                event.getPlayer().sendMessage(ChatColor.GRAY + "NOTE: Every chest of a doublechest needs to be restored manually!");
            }
            event.setCancelled(true);
        }
    }
}
