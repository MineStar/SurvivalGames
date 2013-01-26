package de.minestar.craftz.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import de.minestar.craftz.Core;

public class CreateGame_Command {

    public void execute(Player sender, String[] args) {
        // check the argumentcount
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Wrong syntax!");
            sender.sendMessage(ChatColor.GRAY + "/game create <GameName>");
            return;
        }

        if (Core.gameManager.gameExists(args[1])) {
            sender.sendMessage(ChatColor.RED + "A game with that name already exists!");
            return;
        } else {
            Core.gameManager.createGame(args[1]);
            sender.sendMessage(ChatColor.GREEN + "Game created!");
            return;
        }
    }
}
