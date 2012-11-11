package de.minestar.survivalgames.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import de.minestar.survivalgames.Core;
import de.minestar.survivalgames.utils.Chat;

public class StopGame_Command {

    public void execute(Player sender) {
        if (!Core.gameManager.isInGame()) {
            sender.sendMessage(ChatColor.RED + "Game is not running!");
            return;
        }

        sender.sendMessage(ChatColor.GREEN + "Executing command \"End Game\"...");
        Core.gameManager.endGame();
        Core.playerManager.teleportAllToLobbySpawn();
        Chat.broadcast(ChatColor.RED, "GAME HAS BEEN STOPPED BY AN ADMIN!");
    }
}
