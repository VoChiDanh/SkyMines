package com.github.colingrime.updater;

import com.github.colingrime.SkyMines;
import com.github.colingrime.utils.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class SpigotUpdater {

    private static final String UPDATE_URL = "https://api.spigotmc.org/legacy/update.php?resource=101373";
    private static final String RESOURCE_URL = "https://www.spigotmc.org/resources/101373/";

    private final SkyMines plugin;

    public SpigotUpdater(SkyMines plugin) {
        this.plugin = plugin;
    }

    public void checkForUpdate() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(UPDATE_URL).openStream()))) {
            String latestVersion = reader.readLine();
            String pluginVersion = plugin.getDescription().getVersion();
            String latestNumber = latestVersion.replace(".", "");
            String pluginNumber = pluginVersion.replace(".", "");
            if (!pluginVersion.contains("-SNAPSHOT")) {
                if (Integer.parseInt(latestNumber) > Integer.parseInt(pluginNumber)) {
                    Logger.warn("A new update has been found (v" + latestVersion + ")");
                    Logger.warn("Download it here: " + RESOURCE_URL);
                }
            } else {
                Logger.warn("You are running dev build version, be careful!");
                Logger.warn("Your version: " + pluginVersion + " (#" + pluginNumber.replace("-SNAPSHOT", "") + ")");
                Logger.warn("Latest version: " + latestVersion + " (#" + latestNumber + ")");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}