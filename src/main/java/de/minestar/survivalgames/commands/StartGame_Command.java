package de.minestar.survivalgames.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import de.minestar.survivalgames.Core;

public class StartGame_Command {
    public void execute(Player sender) {
        sender.sendMessage(ChatColor.GREEN + "Executing command \"Start Game\"...");
        Core.gameManager.preGame();
    }
}
