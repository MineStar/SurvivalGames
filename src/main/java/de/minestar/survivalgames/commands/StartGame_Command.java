package de.minestar.survivalgames.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import de.minestar.survivalgames.Core;
import de.minestar.survivalgames.data.SurvivalPlayer;

public class StartGame_Command {

    public void execute(Player sender, String[] args) {
        // check the argumentcount
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Wrong syntax!");
            sender.sendMessage(ChatColor.GRAY + "/game start");
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

        sender.sendMessage(ChatColor.GREEN + "Executing command \"Start Game\"...");

        if (!sPlayer.getCurrentGame().isSetupComplete()) {
            sender.sendMessage(ChatColor.RED + "Gamesetup is incomplete!");
            return;
        }

        Core.gameManager.startGame(sPlayer.getCurrentGame().getGameName());
    }
}
