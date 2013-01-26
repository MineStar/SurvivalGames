package de.minestar.craftz.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import de.minestar.craftz.Core;
import de.minestar.craftz.data.SurvivalPlayer;

public class QuitGame_Command {

    public void execute(Player sender, String[] args) {
        // check the argumentcount
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Wrong syntax!");
            sender.sendMessage(ChatColor.GRAY + "/game quit");
            return;
        }

        // get the player
        SurvivalPlayer sPlayer = Core.gameManager.getPlayer(sender.getName());
        if (sPlayer == null) {
            sender.sendMessage(ChatColor.RED + "You are currently not in a survivalgame!");
            return;
        }

        // quit the game
        Core.gameManager.playerQuitGame(sPlayer.getPlayerName());
        sender.sendMessage(ChatColor.GREEN + "You have left the game.");
    }
}
