package de.minestar.craftz.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import de.minestar.craftz.Core;
import de.minestar.craftz.data.SurvivalPlayer;

public class StopGame_Command {

    public void execute(Player sender, String[] args) {
        // check the argumentcount
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Wrong syntax!");
            sender.sendMessage(ChatColor.GRAY + "/game stop");
            return;
        }

        // get the player
        SurvivalPlayer sPlayer = Core.gameManager.getPlayer(sender.getName());
        if (sPlayer == null) {
            sender.sendMessage(ChatColor.RED + "You are currently not in a survivalgame!");
            return;
        }

        if (sPlayer.getCurrentGame().isGameInLobby()) {
            sender.sendMessage(ChatColor.RED + "Game is not running!");
            return;
        }

        sender.sendMessage(ChatColor.GREEN + "Executing command \"End Game\"...");
        Core.gameManager.stopGame(sPlayer.getCurrentGame().getGameName());
    }
}
