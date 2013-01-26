package de.minestar.craftz.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import de.minestar.craftz.Core;
import de.minestar.craftz.data.SurvivalGame;
import de.minestar.craftz.data.SurvivalPlayer;

public class JoinGame_Command {

    public void execute(Player sender, String[] args) {
        // check the argumentcount
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Wrong syntax!");
            sender.sendMessage(ChatColor.GRAY + "/game join <GameName>");
            return;
        }

        // get the player
        SurvivalPlayer sPlayer = Core.gameManager.getPlayer(sender.getName());
        if (sPlayer != null) {
            sPlayer.broadcast(ChatColor.RED + "You are already in a survivalgame!");
            sPlayer.broadcast(ChatColor.GRAY + "Exit it via /game quit...");
            return;
        }

        if (Core.gameManager.gameExists(args[1])) {
            if (Core.gameManager.getGame(args[1]).isGameFull() && !sender.isOp()) {
                sender.sendMessage(ChatColor.RED + "Sorry! There are no free slots left...");
                sender.sendMessage(ChatColor.GRAY + "Try again later!");
                return;
            }
            Core.gameManager.playerJoinGame(args[1], sender.getName());
        } else {
            sender.sendMessage(ChatColor.RED + SurvivalGame.LIMITER);
            sender.sendMessage(ChatColor.RED + "A game named '" + args[1] + "' does not exist!");
            sender.sendMessage(ChatColor.RED + "List of games:");
            Core.gameManager.listGames(sender);
            sender.sendMessage(ChatColor.RED + SurvivalGame.LIMITER);
        }
    }
}
