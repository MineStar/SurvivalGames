package de.minestar.craftz.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationUtils {

    public static String toString(Location location) {
        return toString(location, null);
    }

    public static String toString(Location location, String appendix) {
        if (appendix != null && appendix.length() > 0) {
            return location.getWorld().getName() + "_" + location.getX() + "_" + location.getY() + "_" + location.getZ() + "_" + location.getYaw() + "_" + location.getPitch() + "_" + appendix;
        } else {
            return location.getWorld().getName() + "_" + location.getX() + "_" + location.getY() + "_" + location.getZ() + "_" + location.getYaw() + "_" + location.getPitch();
        }
    }

    public static Location fromString(String text) {
        String[] split = text.split("_");
        if (split.length < 6) {
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

    public static boolean equalsXZ(Location location, Location other) {
        if (location.getBlockX() != other.getBlockX()) {
            return false;
        }

        if (location.getBlockZ() != other.getBlockZ()) {
            return false;
        }

        if (!location.getWorld().getName().equalsIgnoreCase(other.getWorld().getName())) {
            return false;
        }

        return true;
    }

    public static boolean equals(Location location, Location other) {

        if (location.getBlockX() != other.getBlockX()) {
            return false;
        }

        if (location.getBlockY() != other.getBlockY()) {
            return false;
        }

        if (location.getBlockZ() != other.getBlockZ()) {
            return false;
        }

        if (!location.getWorld().getName().equalsIgnoreCase(other.getWorld().getName())) {
            return false;
        }

        return true;
    }
}
