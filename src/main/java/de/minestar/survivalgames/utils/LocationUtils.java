package de.minestar.survivalgames.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationUtils {

    public static String toString(Location location) {
        return location.getWorld().getName() + ";" + location.getX() + ";" + location.getY() + ";" + location.getZ() + ";" + location.getYaw() + ";" + location.getPitch();
    }

    public static Location fromString(String text) {
        String[] split = text.split(";");
        if (split.length != 6) {
            return null;
        }

        World world = Bukkit.getWorld(split[0]);
        if (world == null) {
            return null;
        }

        try {
            return new Location(world, Float.valueOf(split[1]), Float.valueOf(split[2]), Float.valueOf(split[3]), Float.valueOf(split[4]), Float.valueOf(split[5]));
        } catch (Exception e) {
            return null;
        }
    }
}
