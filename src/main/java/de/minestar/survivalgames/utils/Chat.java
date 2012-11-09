package de.minestar.survivalgames.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Chat {

    private static Logger LOGGER;

    static {
        LOGGER = Logger.getLogger("Minecraft");
    }

    // //////////////////////////
    //
    // Console
    //
    // //////////////////////////

    public static void printMessage(String message) {
        Chat.printMessage(ChatColor.WHITE, message);
    }

    public static void printMessage(ChatColor color, String message) {
        LOGGER.log(Level.INFO, message);
    }

    // //////////////////////////
    //
    // Broadcasts
    //
    // //////////////////////////

    public static void broadcast(ChatColor color, String message) {
        Bukkit.broadcastMessage(color + message);
    }

    public static void broadcast(String message) {
        Bukkit.broadcastMessage(message);
    }
}
