/*
 * Copyright (C) 2011 MineStar.de 
 * 
 * This file is part of TheRock.
 * 
 * TheRock is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * TheRock is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with TheRock.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.minestar.survivalgames;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import de.minestar.survivalgames.commands.CreateGame_Command;
import de.minestar.survivalgames.commands.JoinGame_Command;
import de.minestar.survivalgames.commands.QuitGame_Command;
import de.minestar.survivalgames.commands.SetLobbySpawn_Command;
import de.minestar.survivalgames.commands.SetPlayerSpawn_Command;
import de.minestar.survivalgames.commands.SetSpectatorSpawn_Command;
import de.minestar.survivalgames.commands.StartGame_Command;
import de.minestar.survivalgames.commands.StopGame_Command;
import de.minestar.survivalgames.listener.AdminListener;
import de.minestar.survivalgames.listener.BlockListener;
import de.minestar.survivalgames.listener.PlayerListener;
import de.minestar.survivalgames.manager.GameManager;
import de.minestar.survivalgames.utils.Chat;

public class Core extends JavaPlugin {

    public static Core INSTANCE;

    public static final String NAME = "SurvivalGames";
    public static final String VERSION = "0.1alpha";

    public static GameManager gameManager;

    @Override
    public void onEnable() {
        Core.INSTANCE = this;

        // create dirs
        Core.INSTANCE.getDataFolder().mkdir();

        // create managers
        Core.gameManager = new GameManager();

        // enable managers
        Core.gameManager.onEnable();

        // create listeners
        Bukkit.getPluginManager().registerEvents(new AdminListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);

        // print info
        Chat.printMessage(NAME + " version " + VERSION + " enabled!");
    }

    @Override
    public void onDisable() {
        // disable managers
        Core.gameManager.onDisable();

        // print info
        Chat.printMessage(NAME + " version " + VERSION + " disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "You are not allowed to use this command!");
            return true;
        }

        if (sender instanceof Player) {
            if (args[0].equalsIgnoreCase("create")) {
                new CreateGame_Command().execute((Player) sender, args);
                return true;
            }
            if (args[0].equalsIgnoreCase("start")) {
                new StartGame_Command().execute((Player) sender, args);
                return true;
            }
            if (args[0].equalsIgnoreCase("stop")) {
                new StopGame_Command().execute((Player) sender, args);
                return true;
            }
            if (args[0].equalsIgnoreCase("join")) {
                new JoinGame_Command().execute((Player) sender, args);
                return true;
            }
            if (args[0].equalsIgnoreCase("quit")) {
                new QuitGame_Command().execute((Player) sender, args);
                return true;
            }
            if (args[0].equalsIgnoreCase("setLobby")) {
                new SetLobbySpawn_Command().execute((Player) sender, args);
                return true;
            }
            if (args[0].equalsIgnoreCase("setSpectator")) {
                new SetSpectatorSpawn_Command().execute((Player) sender, args);
                return true;
            }
            if (args[0].equalsIgnoreCase("addPlayer")) {
                new SetPlayerSpawn_Command().execute((Player) sender, args);
                return true;
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Only ingame!");
            return true;
        }

        return true;
    }

}
