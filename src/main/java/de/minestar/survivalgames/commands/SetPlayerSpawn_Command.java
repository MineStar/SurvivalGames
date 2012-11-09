package de.minestar.survivalgames.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import de.minestar.survivalgames.data.PlayerSpawn;
import de.minestar.survivalgames.data.Settings;

public class SetPlayerSpawn_Command {

    public void execute(Player sender, String[] args) {
        Settings.addPlayerSpawn(new PlayerSpawn(-1, sender.getLocation()));
        sender.sendMessage(ChatColor.GREEN + "Player added!");
    }
}
