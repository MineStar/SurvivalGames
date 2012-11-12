package de.minestar.survivalgames.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;

public class Chat {

    private static Logger LOGGER;

    static {
        LOGGER = Logger.getLogger("Minecraft");
    }

    // //////////////////////////
    //
    // Date
    //
    // //////////////////////////

    public static String secondsToMinutes(long seconds) {
        long mins = seconds / 60;
        long secs = seconds % 60;
        String text = "";
        if (mins > 0) {
            text += mins + " minute";
            if (mins != 1) {
                text += "s";
            }

        }
        if (secs > 0) {
            if (mins > 0) {
                text += " and ";
            }
            text += secs + " second";
            if (secs != 1) {
                text += "s";
            }
        }
        return text;
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
}
