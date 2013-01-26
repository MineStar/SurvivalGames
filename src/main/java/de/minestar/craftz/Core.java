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

package de.minestar.craftz;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import de.minestar.craftz.commands.CreateGame_Command;
import de.minestar.craftz.commands.JoinGame_Command;
import de.minestar.craftz.commands.QuitGame_Command;
import de.minestar.craftz.commands.Ready_Command;
import de.minestar.craftz.commands.SetLobbySpawn_Command;
import de.minestar.craftz.commands.SetPlayerSpawn_Command;
import de.minestar.craftz.commands.SetSpectatorSpawn_Command;
import de.minestar.craftz.commands.StartGame_Command;
import de.minestar.craftz.commands.StopGame_Command;
import de.minestar.craftz.commands.TPLoot_Command;
import de.minestar.craftz.listener.AdminListener;
import de.minestar.craftz.listener.BlockListener;
import de.minestar.craftz.listener.PlayerListener;
import de.minestar.craftz.manager.GameManager;
import de.minestar.craftz.utils.Chat;

public class Core extends JavaPlugin {

    public static Core INSTANCE;

    public static final String NAME = "CraftZ";
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

        Player[] playerList = Bukkit.getOnlinePlayers();
        for (Player player : playerList) {
            Core.gameManager.playerJoinGame(Core.gameManager.getMainGame().getGameName(), player.getName());
        }

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
        if (label.contains("game") && !sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "You are not allowed to use this command!");
            return true;
        }

        // get the player
        if (label.contains("ready")) {
            new Ready_Command().execute((Player) sender, args);
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Wrong syntax!");
            return true;
        }

        if (sender instanceof Player) {
            if (args[0].equalsIgnoreCase("create")) {
                if (sender.isOp()) {
                    new CreateGame_Command().execute((Player) sender, args);
                } else {
                    sender.sendMessage(ChatColor.RED + "Command disabled!");
                }
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
                if (sender.isOp()) {
                    new JoinGame_Command().execute((Player) sender, args);
                } else {
                    sender.sendMessage(ChatColor.RED + "Command disabled!");
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("quit")) {
                if (sender.isOp()) {
                    new QuitGame_Command().execute((Player) sender, args);
                } else {
                    sender.sendMessage(ChatColor.RED + "Command disabled!");
                }
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
            if (args[0].equalsIgnoreCase("tpLoot")) {
                new TPLoot_Command().execute((Player) sender, args);
                return true;
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Only ingame!");
            return true;
        }

        return true;
    }

}
