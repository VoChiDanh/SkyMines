package com.github.colingrime.config;

import com.github.colingrime.storage.StorageType;
import com.github.colingrime.utils.Utils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Settings {

	private final JavaPlugin plugin;
	private FileConfiguration config;

	// database stuff
	private StorageType storageType;
	private String host;
	private int port;
	private String database;
	private String username;
	private String password;

	// token stuff
	private Material tokenType;
	private String tokenName;
	private List<String> tokenLore;

	// upgrade: block variety
	private Map<Integer, BlockVariety> upgradesBlockVariety;
	private Map<Integer, Double> costsBlockVariety;

	// upgrade: reset cooldown
	private Map<Integer, Double> upgradesResetCooldown;
	private Map<Integer, Double> costsResetCooldown;

	// max levels
	private int maxBlockVariety;
	private int maxResetCooldown;

	public Settings(JavaPlugin plugin) {
		this.plugin = plugin;
		this.plugin.saveDefaultConfig();
	}

	public void reload() {
		plugin.reloadConfig();
		config = plugin.getConfig();

		// database stuff
		storageType = _getStorageType();
		host = _getHost();
		port = _getPort();
		database = _getDatabase();
		username = _getUsername();
		password = _getPassword();

		// token stuff
		tokenType = _getTokenType();
		tokenName = _getTokenName();
		tokenLore = _getTokenLore();

		// upgrade: block variety
		upgradesBlockVariety = _getUpgradesBlockVariety();
		costsBlockVariety = _getCostsBlockVariety();

		// upgrade: reset cooldown
		upgradesResetCooldown = _getUpgradesResetCooldown();
		costsResetCooldown = _getCostsResetCooldown();

		// max levels
		maxBlockVariety = Collections.max(upgradesBlockVariety.keySet());
		maxResetCooldown = Collections.max(upgradesResetCooldown.keySet());
	}

	private StorageType _getStorageType() {
		String type = config.getString("skymines-save.storage-type");
		for (StorageType storageType : StorageType.values()) {
			if (storageType.name().equalsIgnoreCase(type)) {
				return storageType;
			}
		}

		return StorageType.None;
	}

	public StorageType getStorageType() {
		return storageType;
	}

	private String _getHost() {
		return config.getString("skymines-save.host");
	}

	public String getHost() {
		return host;
	}

	private int _getPort() {
		return config.getInt("skymines-save.port");
	}

	public int getPort() {
		return port;
	}

	private String _getDatabase() {
		return config.getString("skymines-save.database");
	}

	public String getDatabase() {
		return database;
	}

	private String _getUsername() {
		return config.getString("skymines-save.username");
	}

	public String getUsername() {
		return username;
	}

	private String _getPassword() {
		return config.getString("skymines-save.password");
	}

	public String getPassword() {
		return password;
	}

	private Material _getTokenType() {
		String materialName = config.getString("token.type");
		if (materialName == null || Material.matchMaterial(materialName) == null) {
			return Material.TRIPWIRE_HOOK;
		}
		return Material.matchMaterial(materialName);
	}

	public Material getTokenType() {
		return tokenType;
	}

	private String _getTokenName() {
		return Utils.color(config.getString("token.name"));
	}

	public String getTokenName() {
		return tokenName;
	}

	private List<String> _getTokenLore() {
		return Utils.color(config.getStringList("token.lore"));
	}

	public List<String> getTokenLore() {
		return tokenLore;
	}

	private Map<Integer, BlockVariety> _getUpgradesBlockVariety() {
		Map<Integer, BlockVariety> upgradesBlockVariety = new HashMap<>();
		ConfigurationSection sec = config.getConfigurationSection("upgrades.block-variety");
		if (sec == null) {
			return upgradesBlockVariety;
		}

		// goes over each level in the block variety section
		for (String level : sec.getKeys(false)) {
			if (!level.matches("\\d+")) {
				continue;
			}

			// sets up collection of types with chances
			BlockVariety blockVariety = new BlockVariety();
			for (String types : sec.getStringList(level + ".upgrade")) {
				String[] type = types.split(" ");
				blockVariety.addType(type[0], type[1]);
			}

			upgradesBlockVariety.put(Integer.parseInt(level), blockVariety);
		}

		return upgradesBlockVariety;
	}

	public Map<Integer, BlockVariety> getUpgradesBlockVariety() {
		return upgradesBlockVariety;
	}

	private Map<Integer, Double> _getCostsBlockVariety() {
		Map<Integer, Double> costsBlockVariety = new HashMap<>();
		ConfigurationSection sec = config.getConfigurationSection("upgrades.block-variety");
		if (sec == null) {
			return costsBlockVariety;
		}

		// goes over each level in the block variety section
		for (String level : sec.getKeys(false)) {
			if (level.matches("\\d+")) {
				costsBlockVariety.put(Integer.parseInt(level), sec.getDouble(level + ".cost"));
			}
		}

		return costsBlockVariety;
	}

	public Map<Integer, Double> getCostsBlockVariety() {
		return costsBlockVariety;
	}

	private Map<Integer, Double> _getUpgradesResetCooldown() {
		Map<Integer, Double> upgradesResetCooldown = new HashMap<>();
		ConfigurationSection sec = config.getConfigurationSection("upgrades.reset-cooldown");
		if (sec == null) {
			return upgradesResetCooldown;
		}

		// goes over each level in the reset cooldown section
		for (String level : sec.getKeys(false)) {
			if (!level.matches("\\d+")) {
				continue;
			}

			// checks to make sure time is valid
			String cooldownString = sec.getString(level + ".upgrade.");
			if (cooldownString == null || !cooldownString.split(" ")[0].matches("\\d+(\\.\\d+)?")) {
				continue;
			}
			String[] cooldownArray = cooldownString.split(" ");

			// checks for minute units
			double cooldown = Double.parseDouble(cooldownArray[0]);
			if (cooldownArray.length > 1 && cooldownArray[1].contains("minute")) {
				cooldown *= 60;
			}

			upgradesResetCooldown.put(Integer.parseInt(level), cooldown);
		}

		return upgradesResetCooldown;
	}

	public Map<Integer, Double> getUpgradesResetCooldown() {
		return upgradesResetCooldown;
	}

	private Map<Integer, Double> _getCostsResetCooldown() {
		Map<Integer, Double> costsResetCooldown = new HashMap<>();
		ConfigurationSection sec = config.getConfigurationSection("upgrades.reset-cooldown");
		if (sec == null) {
			return costsResetCooldown;
		}

		// goes over each level in the block variety section
		for (String level : sec.getKeys(false)) {
			if (level.matches("\\d+")) {
				costsResetCooldown.put(Integer.parseInt(level), sec.getDouble(level + ".cost"));
			}
		}

		return costsResetCooldown;
	}

	public Map<Integer, Double> getCostsResetCooldown() {
		return costsResetCooldown;
	}

	public int getMaxBlockVariety() {
		return maxBlockVariety;
	}

	public int getMaxResetCooldown() {
		return maxResetCooldown;
	}
}