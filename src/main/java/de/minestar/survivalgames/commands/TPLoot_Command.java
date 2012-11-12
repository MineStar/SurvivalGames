package de.minestar.survivalgames.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import de.minestar.survivalgames.Core;
import de.minestar.survivalgames.data.SurvivalPlayer;

public class TPLoot_Command {

    public void execute(Player sender, String[] args) {
        // check the argumentcount
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Wrong syntax!");
            sender.sendMessage(ChatColor.GRAY + "/game tpLoot <NUMBER>");
            return;
        }

        int ID = 0;
        try {
            ID = Integer.valueOf(args[1]);
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Wrong syntax!");
            sender.sendMessage(ChatColor.GRAY + "/game tpLoot <NUMBER>");
            return;
        }

        // get the player
        SurvivalPlayer sPlayer = Core.gameManager.getPlayer(sender.getName());
        if (sPlayer == null) {
            sender.sendMessage(ChatColor.RED + "You are currently not in a survivalgame!");
            return;
        }

        if (!sPlayer.getCurrentGame().isGameInLobby()) {
            sender.sendMessage(ChatColor.RED + "Game is currently running!");
            return;
        }

        // send info
        sPlayer.getCurrentGame().getLootManager().teleportToChest(ID, sPlayer);
    }

}
