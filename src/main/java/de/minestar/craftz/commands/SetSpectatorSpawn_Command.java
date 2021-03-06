package de.minestar.craftz.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import de.minestar.craftz.Core;
import de.minestar.craftz.data.PlayerSpawn;
import de.minestar.craftz.data.SurvivalPlayer;

public class SetSpectatorSpawn_Command {

    public void execute(Player sender, String[] args) {
        // check the argumentcount
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Wrong syntax!");
            sender.sendMessage(ChatColor.GRAY + "/game setSpectator");
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

        sPlayer.getCurrentGame().getSettings().setSpectatorSpawn(new PlayerSpawn(-1, sender.getLocation()));
        sPlayer.getCurrentGame().getSettings().savePlayerSpawns();
        sender.sendMessage(ChatColor.GREEN + "Spectatorspawn set!");
    }
}
