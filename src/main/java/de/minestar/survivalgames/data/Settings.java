package de.minestar.survivalgames.data;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.collect.Lists;

import de.minestar.survivalgames.Core;
import de.minestar.survivalgames.utils.Chat;
import de.minestar.survivalgames.utils.LocationUtils;

public class Settings {

    private static HashSet<PlayerSpawn> playerSpawns;
    private static PlayerSpawn spectatorSpawn = null, lobbySpawn = null;
    private static File dataFolder, configFile, playerSpawnFile;

    private static int preGameTime = 30;
    private static int prePVPTime = 2 * 60;
    private static int preDeathmatchTime = 28 * 60;

    private static HashSet<Integer> breakableBlocks = new HashSet<Integer>(Arrays.asList(Material.VINE.getId(), Material.MELON.getId(), Material.WHEAT.getId(), Material.BROWN_MUSHROOM.getId(), Material.RED_MUSHROOM.getId(), Material.SUGAR_CANE_BLOCK.getId(), Material.SAPLING.getId()));
    private static HashSet<Integer> placeableBlocks = new HashSet<Integer>(Arrays.asList(Material.VINE.getId(), Material.CAKE_BLOCK.getId(), Material.CAKE.getId(), Material.SAPLING.getId()));
    private static HashSet<Integer> nonUseableBlocks = new HashSet<Integer>(Arrays.asList(Material.DISPENSER.getId()));
    private static ArrayList<Integer> lootRefillTimes = new ArrayList<Integer>(Arrays.asList(15));

    private static int currentRefill = -1;

    public static void init() {
        Settings.dataFolder = Core.INSTANCE.getDataFolder();
        Settings.configFile = new File(dataFolder, "config.yml");
        Settings.playerSpawnFile = new File(Settings.dataFolder, "playerSpawns.yml");
        Settings.loadConfig();
        Settings.loadPlayerSpawns();
    }

    private static void loadConfig() {
        try {
            YamlConfiguration config = new YamlConfiguration();
            if (!Settings.configFile.exists()) {
                Settings.saveConfig();
            }

            config.load(Settings.configFile);

            // load timinigs
            Settings.preGameTime = config.getInt("timings.game.preGame", Settings.preGameTime);
            Settings.prePVPTime = config.getInt("timings.game.prePVP", Settings.prePVPTime);
            Settings.preDeathmatchTime = config.getInt("timings.game.preDeathmatch", Settings.preDeathmatchTime);

            // load refilltimes
            Settings.lootRefillTimes = new ArrayList<Integer>();
            List<Integer> refillList = config.getIntegerList("timings.lootRefill");
            if (refillList != null) {
                for (int ID : refillList) {
                    Settings.lootRefillTimes.add(ID);
                }
            }
            Settings.currentRefill = Settings.lootRefillTimes.size() - 1;

            // load breakable blocks
            Settings.breakableBlocks = new HashSet<Integer>();
            List<String> breakList = config.getStringList("blocks.breakable");
            if (breakList != null) {
                for (String name : breakList) {
                    int ID = Settings.StringToMaterialID(name);
                    if (ID != Material.AIR.getId()) {
                        Settings.breakableBlocks.add(ID);
                    }
                }
            }

            // load placeable blocks
            Settings.placeableBlocks = new HashSet<Integer>();
            List<String> placeList = config.getStringList("blocks.placeable");
            if (placeList != null) {
                for (String name : placeList) {
                    int ID = Settings.StringToMaterialID(name);
                    if (ID != Material.AIR.getId()) {
                        Settings.placeableBlocks.add(ID);
                    }
                }
            }

            // load non useable blocks
            Settings.nonUseableBlocks = new HashSet<Integer>();
            List<String> nonUseList = config.getStringList("blocks.nonUseable");
            if (nonUseList != null) {
                for (String name : nonUseList) {
                    int ID = Settings.StringToMaterialID(name);
                    if (ID != Material.AIR.getId()) {
                        Settings.nonUseableBlocks.add(ID);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveConfig() {
        try {
            YamlConfiguration config = new YamlConfiguration();
            if (Settings.configFile.exists()) {
                Settings.configFile.delete();
            }

            // save timings
            config.set("timings.game.preGame", Settings.preGameTime);
            config.set("timings.game.prePVP", Settings.prePVPTime);
            config.set("timings.game.preDeathmatch", Settings.preDeathmatchTime);

            // save refilltimes
            config.set("timings.lootRefill", Settings.lootRefillTimes);

            // save breakable blocks
            ArrayList<String> data = new ArrayList<String>();
            for (int ID : Settings.breakableBlocks) {
                String name = Settings.IDToMaterialName(ID);
                if (!name.equalsIgnoreCase("AIR")) {
                    data.add(name);
                }
            }
            config.set("blocks.breakable", Lists.newArrayList(data));

            // save placeable blocks
            data = new ArrayList<String>();
            for (int ID : Settings.placeableBlocks) {
                String name = Settings.IDToMaterialName(ID);
                if (!name.equalsIgnoreCase("AIR")) {
                    data.add(name);
                }
            }
            config.set("blocks.placeable", Lists.newArrayList(data));

            // save non useable blocks
            data = new ArrayList<String>();
            for (int ID : Settings.nonUseableBlocks) {
                String name = Settings.IDToMaterialName(ID);
                if (!name.equalsIgnoreCase("AIR")) {
                    data.add(name);
                }
            }
            config.set("blocks.nonUseable", Lists.newArrayList(data));

            // save to file
            config.save(Settings.configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadPlayerSpawns() {
        try {
            playerSpawns = new HashSet<PlayerSpawn>();
            YamlConfiguration config = new YamlConfiguration();
            if (!Settings.playerSpawnFile.exists()) {
                Settings.playerSpawnFile.createNewFile();
            }

            // load playerspawns
            config.load(Settings.playerSpawnFile);
            List<String> locList = config.getStringList("game.playerSpawns");
            int IDCount = 0;
            if (locList != null) {
                for (String locText : locList) {
                    Location location = LocationUtils.fromString(locText);
                    if (location == null) {
                        Chat.printMessage(ChatColor.RED, "Spawn @ >> " + locText + " << could not be loaded!");
                        continue;
                    }
                    playerSpawns.add(new PlayerSpawn(IDCount, location));
                    IDCount++;
                }
            }

            // load spectatorspawn
            String text = config.getString("game.spectatorSpawn", "NULL");
            if (!text.equalsIgnoreCase("NULL")) {
                Location location = LocationUtils.fromString(text);
                if (location != null) {
                    Settings.spectatorSpawn = new PlayerSpawn(-1, location);
                }
            }

            // load lobbyspawn
            text = config.getString("game.lobbySpawn", "NULL");
            if (!text.equalsIgnoreCase("NULL")) {
                Location location = LocationUtils.fromString(text);
                if (location != null) {
                    Settings.lobbySpawn = new PlayerSpawn(-1, location);
                }
            }

            Chat.printMessage(ChatColor.GREEN, "Loaded spawns: " + playerSpawns.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void savePlayerSpawns() {
        try {
            YamlConfiguration config = new YamlConfiguration();
            if (Settings.configFile.exists()) {
                Settings.configFile.delete();
            }

            // save playerspawns
            ArrayList<String> locList = new ArrayList<String>();
            for (PlayerSpawn spawn : Settings.playerSpawns) {
                locList.add(LocationUtils.toString(spawn.getLocation()));
            }
            config.set("game.playerSpawns", locList);

            // save spectatorspawn
            if (Settings.spectatorSpawn != null) {
                config.set("game.spectatorSpawn", LocationUtils.toString(Settings.spectatorSpawn.getLocation()));
            }

            // save lobbyspawn
            if (Settings.lobbySpawn != null) {
                config.set("game.lobbySpawn", LocationUtils.toString(Settings.lobbySpawn.getLocation()));
            }

            // save to file
            config.save(Settings.playerSpawnFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int StringToMaterialID(String text) {
        for (Material material : Material.values()) {
            if (material.name().equalsIgnoreCase(text) || material.name().replace("_", "").equalsIgnoreCase(text)) {
                return material.getId();
            }
        }
        return Material.AIR.getId();
    }

    private static String IDToMaterialName(int ID) {
        Material material = Material.getMaterial(ID);
        if (material != null) {
            return material.name();
        }
        return "AIR";
    }

    public static int getNextRefillTime() {
        if (Settings.currentRefill < 0) {
            return 0;
        }
        int nextTime = Settings.lootRefillTimes.get(Settings.currentRefill);
        Settings.currentRefill--;
        return nextTime;
    }

    /**
     * @return the playerSpawns
     */
    public static HashSet<PlayerSpawn> getPlayerSpawns() {
        return playerSpawns;
    }

    /**
     * @return the preGameTime
     */
    public static long getPreGameTime() {
        return preGameTime;
    }

    /**
     * @return the prePVPTime
     */
    public static long getPrePVPTime() {
        return prePVPTime;
    }

    /**
     * @param playerSpawns
     *            the playerSpawns to set
     */
    public static void setPlayerSpawns(HashSet<PlayerSpawn> playerSpawns) {
        Settings.playerSpawns = playerSpawns;
    }

    /**
     * @param spectatorSpawn
     *            the spectatorSpawn to set
     */
    public static void setSpectatorSpawn(PlayerSpawn spectatorSpawn) {
        Settings.spectatorSpawn = spectatorSpawn;
    }

    /**
     * @param lobbySpawn
     *            the lobbySpawn to set
     */
    public static void setLobbySpawn(PlayerSpawn lobbySpawn) {
        Settings.lobbySpawn = lobbySpawn;
    }

    /**
     * @param preGameTime
     *            the preGameTime to set
     */
    public static void setPreGameTime(int preGameTime) {
        Settings.preGameTime = preGameTime;
    }

    /**
     * @param prePVPTime
     *            the prePVPTime to set
     */
    public static void setPrePVPTime(int prePVPTime) {
        Settings.prePVPTime = prePVPTime;
    }

    /**
     * @param preDeathmatchTime
     *            the preDeathmatchTime to set
     */
    public static void setPreDeathmatchTime(int preDeathmatchTime) {
        Settings.preDeathmatchTime = preDeathmatchTime;
    }

    /**
     * @return the preDeathmatchTime
     */
    public static long getPreDeathmatchTime() {
        return preDeathmatchTime;
    }

    public static PlayerSpawn getSpectatorSpawn() {
        return spectatorSpawn;
    }

    public static PlayerSpawn getLobbySpawn() {
        return lobbySpawn;
    }

    public static PlayerSpawn getPlayerSpawnByID(int ID) {
        for (PlayerSpawn spawn : Settings.playerSpawns) {
            if (spawn.getID() == ID) {
                return spawn;
            }
        }
        return null;
    }

    public static boolean addPlayerSpawn(PlayerSpawn spawn) {
        if (Settings.playerSpawns.contains(spawn)) {
            return false;
        }
        Settings.playerSpawns.add(spawn);
        Settings.savePlayerSpawns();
        Settings.loadPlayerSpawns();
        return true;
    }

    public static boolean removePlayerSpawn(int ID) {
        PlayerSpawn spawn = Settings.getPlayerSpawnByID(ID);
        if (spawn == null) {
            return false;
        }
        return Settings.removePlayerSpawn(spawn);
    }

    public static boolean removePlayerSpawn(PlayerSpawn spawn) {
        if (!Settings.playerSpawns.contains(spawn)) {
            return false;
        }
        Settings.playerSpawns.remove(spawn);
        Settings.savePlayerSpawns();
        return true;
    }

    public static boolean isBreakable(Material material) {
        return Settings.breakableBlocks.contains(material.getId());
    }

    public static boolean isPlaceable(Material material) {
        return Settings.placeableBlocks.contains(material.getId());
    }

    public static boolean isNonUseable(Material material) {
        return Settings.nonUseableBlocks.contains(material.getId());
    }
}
