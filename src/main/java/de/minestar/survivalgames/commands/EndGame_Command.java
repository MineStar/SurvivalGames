package de.minestar.survivalgames.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import de.minestar.survivalgames.Core;
import de.minestar.survivalgames.utils.Chat;

public class EndGame_Command {

    public void execute(Player sender) {
        sender.sendMessage(ChatColor.GREEN + "Executing command \"End Game\"...");
        Core.gameManager.endGame();
        Chat.broadcast(ChatColor.RED, "GAME HAS BEEN STOPPED BY AN ADMIN!");
    }
}
