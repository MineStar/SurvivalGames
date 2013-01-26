package de.minestar.craftz.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import de.minestar.craftz.Core;
import de.minestar.craftz.data.SurvivalPlayer;
import de.minestar.craftz.data.loot.AbstractLootspot;
import de.minestar.craftz.manager.GameManager;

public class AdminListener implements Listener {

    private GameManager gameManager;

    public AdminListener() {
        this.gameManager = Core.gameManager;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // only check OPs
        if (!event.getPlayer().isOp()) {
            return;
        }

        // get the player
        SurvivalPlayer sPlayer = this.gameManager.getPlayer(event.getPlayer().getName());
        if (sPlayer == null) {
            return;
        }

        // only right clicks on a block
        if (!event.getAction().equals(Action.LEFT_CLICK_BLOCK) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        // get the block
        Block block = event.getClickedBlock();

        // handle chest interaction
        boolean isChest = block.getType().equals(Material.CHEST);
        boolean isDispenser = block.getType().equals(Material.DISPENSER);
        boolean isFurnace = block.getType().equals(Material.FURNACE);

        boolean isLoot = isChest || isDispenser || isFurnace;
        if (!isLoot) {
            return;
        }

        // we need a bedrock in the hand
        ItemStack stack = event.getPlayer().getItemInHand();

        boolean isBookshelfInHand = (stack != null && stack.getType().equals(Material.BOOKSHELF));
        boolean isBedrockInHand = (stack != null && stack.getType().equals(Material.BEDROCK));

        // BOOKSHELF ==>> RESPAWN LOOT
        if (isBookshelfInHand) {
            AbstractLootspot lootSpot = sPlayer.getCurrentGame().getLootManager().getLootspot(block.getLocation());
            if (lootSpot != null) {
                lootSpot.respawnLoot();
                event.getPlayer().sendMessage(ChatColor.GREEN + "Lootspot refilled!");
                if (isChest) {
                    event.getPlayer().sendMessage(ChatColor.GRAY + "NOTE: Every chest of a doublechest needs to be refilled!");
                }
                // cancel the event
                event.setCancelled(true);
                return;
            }
        }

        if (isBedrockInHand) {
            // cancel the event
            event.setCancelled(true);
            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                // save/update the chest
                AbstractLootspot lootSpot = sPlayer.getCurrentGame().getLootManager().getLootspot(block.getLocation());
                if (lootSpot != null) {
                    // UPDATE LOOTSPOT
                    lootSpot.updateLootContentsFromInventory();
                    lootSpot.saveLoot();
                    event.getPlayer().sendMessage(ChatColor.GREEN + "Lootspot updated!");
                    if (isChest) {
                        event.getPlayer().sendMessage(ChatColor.GRAY + "NOTE: Every chest of a doublechest needs to be updated!");
                    }
                    return;
                } else {
                    // CREATE LOOTSPOT
                    lootSpot = sPlayer.getCurrentGame().getLootManager().addLootSpot(block.getLocation());
                    if (lootSpot != null) {
                        lootSpot = sPlayer.getCurrentGame().getLootManager().getLootspot(block.getLocation());
                        lootSpot.updateLootContentsFromInventory();
                        lootSpot.saveLoot();
                        event.getPlayer().sendMessage(ChatColor.GREEN + "Lootspot created!");
                        if (isChest) {
                            event.getPlayer().sendMessage(ChatColor.GRAY + "NOTE: Every chest of a doublechest needs to be saved!");
                        }
                    } else {
                        event.getPlayer().sendMessage(ChatColor.RED + "Error creating lootspot!");
                    }
                    return;
                }
            } else {
                // SHOW ALL LOOTCONTENTS
                AbstractLootspot lootSpot = sPlayer.getCurrentGame().getLootManager().getLootspot(block.getLocation());
                if (lootSpot != null) {
                    lootSpot.showLoot();
                    event.getPlayer().sendMessage(ChatColor.GREEN + "Lootspot is now showing ALL lootcontents!");
                    if (isChest) {
                        event.getPlayer().sendMessage(ChatColor.GRAY + "NOTE: Every chest of a doublechest needs to be restored manually!");
                    }
                }
            }
        }

    }
}
