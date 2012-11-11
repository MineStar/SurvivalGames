package de.minestar.survivalgames.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import de.minestar.survivalgames.data.PlayerSpawn;
import de.minestar.survivalgames.data.Settings;

public class SetSpectatorSpawn_Command {

    public void execute(Player sender, String[] args) {
        Settings.setSpectatorSpawn(new PlayerSpawn(-1, sender.getLocation()));
        Settings.saveConfig();
        sender.sendMessage(ChatColor.GREEN + "Spectatorspawn set!");
    }
}
