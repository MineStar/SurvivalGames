package de.minestar.survivalgames.data;

import org.bukkit.Location;

public class PlayerSpawn implements Comparable<PlayerSpawn> {

    private final int ID;
    private Location location;

    public PlayerSpawn(int ID, Location location) {
        this.ID = ID;
        this.location = location;
    }

    public int getID() {
        return ID;
    }

    public Location getLocation() {
        return this.location;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PlayerSpawn)) {
            return false;
        }

        PlayerSpawn spawn = (PlayerSpawn) obj;
        return this.ID == spawn.ID;
    }

    @Override
    public int compareTo(PlayerSpawn other) {
        return this.ID - other.ID;
    }

    @Override
    public int hashCode() {
        return this.ID;
    }
}
