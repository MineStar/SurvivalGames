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

import org.bukkit.plugin.java.JavaPlugin;

import de.minestar.survivalgames.manager.GameManager;
import de.minestar.survivalgames.manager.LootManager;
import de.minestar.survivalgames.manager.PlayerManager;
import de.minestar.survivalgames.utils.Chat;

public class Core extends JavaPlugin {

    public static Core INSTANCE;

    public static final String NAME = "SurvivalGames";
    public static final String VERSION = "0.1alpha";

    public static LootManager lootManager;
    public static GameManager gameManager;
    public static PlayerManager playerManager;

    @Override
    public void onEnable() {
        // create managers
        Core.gameManager = new GameManager();
        Core.lootManager = new LootManager();
        Core.playerManager = new PlayerManager();

        // enable managers
        Core.gameManager.onEnable();
        Core.lootManager.onEnable();
        Core.playerManager.onEnable();

        // print info
        Chat.printMessage(NAME + " version " + VERSION + " enabled!");
    }

    @Override
    public void onDisable() {
        // disable managers
        Core.playerManager.onDisable();
        Core.lootManager.onDisable();
        Core.gameManager.onDisable();

        // print info
        Chat.printMessage(NAME + " version " + VERSION + " disabled!");
    }

}
