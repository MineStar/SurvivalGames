package de.minestar.survivalgames.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import de.minestar.survivalgames.Core;
import de.minestar.survivalgames.data.Settings;

public class StartGame_Command {

    public void execute(Player sender) {
        if (Settings.getLobbySpawn() == null) {
            sender.sendMessage(ChatColor.RED + "Lobbyspawn not set!");
            return;
        }

        if (Settings.getSpectatorSpawn() == null) {
            sender.sendMessage(ChatColor.RED + "Spectatorspawn not set!");
            return;
        }

        if (Settings.getPlayerSpawns().size() < 2) {
            sender.sendMessage(ChatColor.RED + "No playerspawns set!");
            return;
        }

        if (Core.gameManager.isInGame()) {
            sender.sendMessage(ChatColor.RED + "Game is already running!");
            return;
        }

        sender.sendMessage(ChatColor.GREEN + "Executing command \"Start Game\"...");
        Core.lootManager.endGame();
        Core.gameManager.preGame();
    }
}
