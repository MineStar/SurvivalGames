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

public class GameSettings {

    private HashSet<PlayerSpawn> playerSpawns;
    private PlayerSpawn spectatorSpawn = null, lobbySpawn = null;
    private File dataFolder, configFile, playerSpawnFile;

    private int preGameTime = 30;
    private int prePVPTime = 2 * 60;
    private int preDeathmatchTime = 28 * 60;
    private int afterMatchTime = 2 * 60;

    private HashSet<Integer> breakableBlocks = new HashSet<Integer>(Arrays.asList(Material.VINE.getId(), Material.MELON.getId(), Material.WHEAT.getId(), Material.BROWN_MUSHROOM.getId(), Material.RED_MUSHROOM.getId(), Material.SUGAR_CANE_BLOCK.getId(), Material.SAPLING.getId()));
    private HashSet<Integer> placeableBlocks = new HashSet<Integer>(Arrays.asList(Material.VINE.getId(), Material.CAKE_BLOCK.getId(), Material.CAKE.getId(), Material.SAPLING.getId()));
    private HashSet<Integer> nonUseableBlocks = new HashSet<Integer>(Arrays.asList(Material.DISPENSER.getId()));
    private ArrayList<Integer> lootRefillTimes = new ArrayList<Integer>(Arrays.asList(15));

    private int currentRefill = -1;

    public GameSettings(String gameName) {
        this.dataFolder = new File(Core.INSTANCE.getDataFolder() + System.getProperty("file.separator") + gameName);
        this.dataFolder.mkdir();
        this.configFile = new File(dataFolder, "config.yml");
        this.playerSpawnFile = new File(this.dataFolder, "playerSpawns.yml");
        this.loadConfig();
        this.loadPlayerSpawns();
        this.reset();
    }

    public void reset() {
        this.currentRefill = this.lootRefillTimes.size() - 1;
    }

    private void loadConfig() {
        try {
            YamlConfiguration config = new YamlConfiguration();
            if (!this.configFile.exists()) {
                this.saveConfig();
            }

            // init YAML
            config.load(this.configFile);

            // load timinigs
            this.preGameTime = config.getInt("timings.game.preGame", this.preGameTime);
            this.prePVPTime = config.getInt("timings.game.prePVP", this.prePVPTime);
            this.preDeathmatchTime = config.getInt("timings.game.preDeathmatch", this.preDeathmatchTime);
            this.afterMatchTime = config.getInt("timings.game.afterMatch", this.afterMatchTime);

            // load refilltimes
            this.lootRefillTimes = new ArrayList<Integer>();
            List<Integer> refillList = config.getIntegerList("timings.lootRefill");
            if (refillList != null) {
                for (int ID : refillList) {
                    this.lootRefillTimes.add(ID);
                }
            }

            // load breakable blocks
            this.breakableBlocks = new HashSet<Integer>();
            List<String> breakList = config.getStringList("blocks.breakable");
            if (breakList != null) {
                for (String name : breakList) {
                    int ID = this.StringToMaterialID(name);
                    if (ID != Material.AIR.getId()) {
                        this.breakableBlocks.add(ID);
                    }
                }
            }

            // load placeable blocks
            this.placeableBlocks = new HashSet<Integer>();
            List<String> placeList = config.getStringList("blocks.placeable");
            if (placeList != null) {
                for (String name : placeList) {
                    int ID = this.StringToMaterialID(name);
                    if (ID != Material.AIR.getId()) {
                        this.placeableBlocks.add(ID);
                    }
                }
            }

            // load non useable blocks
            this.nonUseableBlocks = new HashSet<Integer>();
            List<String> nonUseList = config.getStringList("blocks.nonUseable");
            if (nonUseList != null) {
                for (String name : nonUseList) {
                    int ID = this.StringToMaterialID(name);
                    if (ID != Material.AIR.getId()) {
                        this.nonUseableBlocks.add(ID);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveConfig() {
        try {
            YamlConfiguration config = new YamlConfiguration();

            // save timings
            config.set("timings.game.preGame", this.preGameTime);
            config.set("timings.game.prePVP", this.prePVPTime);
            config.set("timings.game.preDeathmatch", this.preDeathmatchTime);
            config.set("timings.game.afterMatch", this.afterMatchTime);

            // save refilltimes
            config.set("timings.lootRefill", this.lootRefillTimes);

            // save breakable blocks
            ArrayList<String> data = new ArrayList<String>();
            for (int ID : this.breakableBlocks) {
                String name = this.IDToMaterialName(ID);
                if (!name.equalsIgnoreCase("AIR")) {
                    data.add(name);
                }
            }
            config.set("blocks.breakable", Lists.newArrayList(data));

            // save placeable blocks
            data = new ArrayList<String>();
            for (int ID : this.placeableBlocks) {
                String name = this.IDToMaterialName(ID);
                if (!name.equalsIgnoreCase("AIR")) {
                    data.add(name);
                }
            }
            config.set("blocks.placeable", Lists.newArrayList(data));

            // save non useable blocks
            data = new ArrayList<String>();
            for (int ID : this.nonUseableBlocks) {
                String name = this.IDToMaterialName(ID);
                if (!name.equalsIgnoreCase("AIR")) {
                    data.add(name);
                }
            }
            config.set("blocks.nonUseable", Lists.newArrayList(data));

            // save to file
            config.save(this.configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadPlayerSpawns() {
        try {
            playerSpawns = new HashSet<PlayerSpawn>();
            YamlConfiguration config = new YamlConfiguration();
            if (!this.playerSpawnFile.exists()) {
                this.playerSpawnFile.createNewFile();
            }

            // load playerspawns
            config.load(this.playerSpawnFile);
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
                    this.spectatorSpawn = new PlayerSpawn(-1, location);
                }
            }

            // load lobbyspawn
            text = config.getString("game.lobbySpawn", "NULL");
            if (!text.equalsIgnoreCase("NULL")) {
                Location location = LocationUtils.fromString(text);
                if (location != null) {
                    this.lobbySpawn = new PlayerSpawn(-1, location);
                }
            }

            Chat.printMessage(ChatColor.GREEN, "Loaded spawns: " + playerSpawns.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void savePlayerSpawns() {
        try {
            YamlConfiguration config = new YamlConfiguration();
            if (this.configFile.exists()) {
                this.configFile.delete();
            }

            // save playerspawns
            ArrayList<String> locList = new ArrayList<String>();
            for (PlayerSpawn spawn : this.playerSpawns) {
                locList.add(LocationUtils.toString(spawn.getLocation()));
            }
            config.set("game.playerSpawns", locList);

            // save spectatorspawn
            if (this.spectatorSpawn != null) {
                config.set("game.spectatorSpawn", LocationUtils.toString(this.spectatorSpawn.getLocation()));
            }

            // save lobbyspawn
            if (this.lobbySpawn != null) {
                config.set("game.lobbySpawn", LocationUtils.toString(this.lobbySpawn.getLocation()));
            }

            // save to file
            config.save(this.playerSpawnFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int StringToMaterialID(String text) {
        for (Material material : Material.values()) {
            if (material.name().equalsIgnoreCase(text) || material.name().replace("_", "").equalsIgnoreCase(text)) {
                return material.getId();
            }
        }
        return Material.AIR.getId();
    }

    private String IDToMaterialName(int ID) {
        Material material = Material.getMaterial(ID);
        if (material != null) {
            return material.name();
        }
        return "AIR";
    }

    public int getNextRefillTime() {
        if (this.currentRefill < 0) {
            return 0;
        }
        int nextTime = this.lootRefillTimes.get(this.currentRefill);
        this.currentRefill--;
        return nextTime;
    }

    /**
     * @return the playerSpawns
     */
    public HashSet<PlayerSpawn> getPlayerSpawns() {
        return playerSpawns;
    }

    /**
     * @return the preGameTime
     */
    public long getPreGameTime() {
        return preGameTime;
    }

    /**
     * @return the prePVPTime
     */
    public long getPrePVPTime() {
        return prePVPTime;
    }

    /**
     * @param playerSpawns
     *            the playerSpawns to set
     */
    public void setPlayerSpawns(HashSet<PlayerSpawn> playerSpawns) {
        this.playerSpawns = playerSpawns;
    }

    /**
     * @param spectatorSpawn
     *            the spectatorSpawn to set
     */
    public void setSpectatorSpawn(PlayerSpawn spectatorSpawn) {
        this.spectatorSpawn = spectatorSpawn;
    }

    /**
     * @param lobbySpawn
     *            the lobbySpawn to set
     */
    public void setLobbySpawn(PlayerSpawn lobbySpawn) {
        this.lobbySpawn = lobbySpawn;
    }

    /**
     * @param preGameTime
     *            the preGameTime to set
     */
    public void setPreGameTime(int preGameTime) {
        this.preGameTime = preGameTime;
    }

    /**
     * @param prePVPTime
     *            the prePVPTime to set
     */
    public void setPrePVPTime(int prePVPTime) {
        this.prePVPTime = prePVPTime;
    }

    /**
     * @param preDeathmatchTime
     *            the preDeathmatchTime to set
     */
    public void setPreDeathmatchTime(int preDeathmatchTime) {
        this.preDeathmatchTime = preDeathmatchTime;
    }

    /**
     * @return the preDeathmatchTime
     */
    public long getPreDeathmatchTime() {
        return preDeathmatchTime;
    }

    /**
     * @return the afterMatchTime
     */
    public int getAfterMatchTime() {
        return afterMatchTime;
    }

    /**
     * @param afterMatchTime
     *            the afterMatchTime to set
     */
    public void setAfterMatchTime(int afterMatchTime) {
        this.afterMatchTime = afterMatchTime;
    }

    public PlayerSpawn getSpectatorSpawn() {
        return spectatorSpawn;
    }

    public PlayerSpawn getLobbySpawn() {
        return lobbySpawn;
    }

    public PlayerSpawn getPlayerSpawnByID(int ID) {
        for (PlayerSpawn spawn : this.playerSpawns) {
            if (spawn.getID() == ID) {
                return spawn;
            }
        }
        return null;
    }

    public boolean addPlayerSpawn(PlayerSpawn spawn) {
        if (this.playerSpawns.contains(spawn)) {
            return false;
        }
        this.playerSpawns.add(spawn);
        this.savePlayerSpawns();
        this.loadPlayerSpawns();
        return true;
    }

    public boolean removePlayerSpawn(int ID) {
        PlayerSpawn spawn = this.getPlayerSpawnByID(ID);
        if (spawn == null) {
            return false;
        }
        return this.removePlayerSpawn(spawn);
    }

    public boolean removePlayerSpawn(PlayerSpawn spawn) {
        if (!this.playerSpawns.contains(spawn)) {
            return false;
        }
        this.playerSpawns.remove(spawn);
        this.savePlayerSpawns();
        return true;
    }

    public boolean isBreakable(Material material) {
        return this.breakableBlocks.contains(material.getId());
    }

    public boolean isPlaceable(Material material) {
        return this.placeableBlocks.contains(material.getId());
    }

    public boolean isNonUseable(Material material) {
        return this.nonUseableBlocks.contains(material.getId());
    }
}
