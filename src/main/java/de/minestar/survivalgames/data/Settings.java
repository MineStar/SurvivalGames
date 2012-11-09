package de.minestar.survivalgames.data;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import de.minestar.survivalgames.Core;
import de.minestar.survivalgames.utils.Chat;
import de.minestar.survivalgames.utils.LocationUtils;

public class Settings {

    private static boolean blockDispenserInteraction = true;
    private static boolean blockDoorInteraction = false;
    private static boolean blockStoneButtonInteraction = false;
    private static boolean blockWoodButtonInteraction = false;
    private static boolean blockLeverInteraction = false;
    private static boolean blockFurnaceInteraction = false;
    private static boolean blockWorkbenchInteraction = false;

    private static HashSet<PlayerSpawn> playerSpawns;
    private static PlayerSpawn spectatorSpawn = null, lobbySpawn = null;
    private static File dataFolder, configFile, playerSpawnFile;

    private static int preGameTime = 1;
    private static int prePVPTime = 2;
    private static int preDeathmatchTime = 28;

    public static void init() {
        Settings.dataFolder = Core.INSTANCE.getDataFolder();
        Settings.configFile = new File(dataFolder, "config.yml");
        Settings.playerSpawnFile = new File(Settings.dataFolder, "playerSpawns.yml");
        Settings.loadSettings();
        Settings.loadPlayerSpawns();
    }

    private static void loadPlayerSpawns() {
        try {
            playerSpawns = new HashSet<PlayerSpawn>();
            YamlConfiguration config = new YamlConfiguration();
            if (!Settings.playerSpawnFile.exists()) {
                Settings.playerSpawnFile.createNewFile();
            }
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

            config.save(Settings.playerSpawnFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadSettings() {
        try {
            YamlConfiguration config = new YamlConfiguration();
            if (!Settings.configFile.exists()) {
                Settings.saveSettings();
            }
            config.load(Settings.configFile);
            Settings.blockDispenserInteraction = config.getBoolean("block.interaction.dispenser", true);
            Settings.blockDoorInteraction = config.getBoolean("block.interaction.door", false);
            Settings.blockStoneButtonInteraction = config.getBoolean("block.interaction.stonebutton", false);
            Settings.blockWoodButtonInteraction = config.getBoolean("block.interaction.woodbutton", false);
            Settings.blockLeverInteraction = config.getBoolean("block.interaction.lever", false);
            Settings.blockFurnaceInteraction = config.getBoolean("block.interaction.furnace", false);
            Settings.blockWorkbenchInteraction = config.getBoolean("block.interaction.workbench", false);

            Settings.preGameTime = config.getInt("timings.pre.game", Settings.preGameTime);
            Settings.prePVPTime = config.getInt("timings.pre.pvp", Settings.prePVPTime);
            Settings.preDeathmatchTime = config.getInt("timings.pre.deathmatch", Settings.preDeathmatchTime);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveSettings() {
        try {
            YamlConfiguration config = new YamlConfiguration();
            if (Settings.configFile.exists()) {
                Settings.configFile.delete();
            }
            config.set("block.interaction.dispenser", Settings.blockDispenserInteraction);
            config.set("block.interaction.door", Settings.blockDoorInteraction);
            config.set("block.interaction.stonebutton", Settings.blockStoneButtonInteraction);
            config.set("block.interaction.woodbutton", Settings.blockWoodButtonInteraction);
            config.set("block.interaction.lever", Settings.blockLeverInteraction);
            config.set("block.interaction.furnace", Settings.blockFurnaceInteraction);
            config.set("block.interaction.workbench", Settings.blockWorkbenchInteraction);

            config.set("timings.pre.game", Settings.preGameTime);
            config.set("timings.pre.pvp", Settings.prePVPTime);
            config.set("timings.pre.deathmatch", Settings.preDeathmatchTime);

            config.save(Settings.configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the blockDispenserInteraction
     */
    public static boolean isBlockDispenserInteraction() {
        return blockDispenserInteraction;
    }

    /**
     * @return the blockDoorInteraction
     */
    public static boolean isBlockDoorInteraction() {
        return blockDoorInteraction;
    }

    /**
     * @return the blockStoneButtonInteraction
     */
    public static boolean isBlockStoneButtonInteraction() {
        return blockStoneButtonInteraction;
    }

    /**
     * @return the blockWoodButtonInteraction
     */
    public static boolean isBlockWoodButtonInteraction() {
        return blockWoodButtonInteraction;
    }

    /**
     * @return the blockLeverInteraction
     */
    public static boolean isBlockLeverInteraction() {
        return blockLeverInteraction;
    }

    /**
     * @return the blockFurnaceInteraction
     */
    public static boolean isBlockFurnaceInteraction() {
        return blockFurnaceInteraction;
    }

    /**
     * @return the blockWorkbenchInteraction
     */
    public static boolean isBlockWorkbenchInteraction() {
        return blockWorkbenchInteraction;
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
     * @param blockDispenserInteraction
     *            the blockDispenserInteraction to set
     */
    public static void setBlockDispenserInteraction(boolean blockDispenserInteraction) {
        Settings.blockDispenserInteraction = blockDispenserInteraction;
    }

    /**
     * @param blockDoorInteraction
     *            the blockDoorInteraction to set
     */
    public static void setBlockDoorInteraction(boolean blockDoorInteraction) {
        Settings.blockDoorInteraction = blockDoorInteraction;
    }

    /**
     * @param blockStoneButtonInteraction
     *            the blockStoneButtonInteraction to set
     */
    public static void setBlockStoneButtonInteraction(boolean blockStoneButtonInteraction) {
        Settings.blockStoneButtonInteraction = blockStoneButtonInteraction;
    }

    /**
     * @param blockWoodButtonInteraction
     *            the blockWoodButtonInteraction to set
     */
    public static void setBlockWoodButtonInteraction(boolean blockWoodButtonInteraction) {
        Settings.blockWoodButtonInteraction = blockWoodButtonInteraction;
    }

    /**
     * @param blockLeverInteraction
     *            the blockLeverInteraction to set
     */
    public static void setBlockLeverInteraction(boolean blockLeverInteraction) {
        Settings.blockLeverInteraction = blockLeverInteraction;
    }

    /**
     * @param blockFurnaceInteraction
     *            the blockFurnaceInteraction to set
     */
    public static void setBlockFurnaceInteraction(boolean blockFurnaceInteraction) {
        Settings.blockFurnaceInteraction = blockFurnaceInteraction;
    }

    /**
     * @param blockWorkbenchInteraction
     *            the blockWorkbenchInteraction to set
     */
    public static void setBlockWorkbenchInteraction(boolean blockWorkbenchInteraction) {
        Settings.blockWorkbenchInteraction = blockWorkbenchInteraction;
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
}
