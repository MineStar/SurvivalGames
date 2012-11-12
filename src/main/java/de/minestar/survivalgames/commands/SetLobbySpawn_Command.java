package de.minestar.survivalgames.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import de.minestar.survivalgames.Core;
import de.minestar.survivalgames.data.PlayerSpawn;
import de.minestar.survivalgames.data.SurvivalPlayer;

public class SetLobbySpawn_Command {

    public void execute(Player sender, String[] args) {
        // check the argumentcount
        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Wrong syntax!");
            sender.sendMessage(ChatColor.GRAY + "/game setLobby");
            return;
        }

        // get the player
        SurvivalPlayer sPlayer = Core.gameManager.getPlayer(sender.getName());
        if (sPlayer == null) {
            sender.sendMessage(ChatColor.RED + "You are currently not in a survivalgame!");
            return;
        }

        sPlayer.getCurrentGame().getSettings().setLobbySpawn(new PlayerSpawn(-1, sender.getLocation()));
        sPlayer.getCurrentGame().getSettings().savePlayerSpawns();
        sender.sendMessage(ChatColor.GREEN + "Lobbyspawn set!");
    }
}
