package com.github.colingrime.locale;

import com.github.colingrime.utils.Logger;
import com.github.colingrime.utils.Replacer;
import com.github.colingrime.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Messages {

	// general messages
	LIST_SKYMINES("general.list-skymines", "&7[&a%id%&7] &eClick to teleport."),

	// success messages
	SUCCESS_GIVE("success.give", "&7You've given &a%amount%x %token% &7to &a%player%&7!"),
	SUCCESS_RECEIVE("success.receive", "&7You've received &a%amount%x %token%&7!"),
	SUCCESS_UPGRADE("success.upgrade", "&aYou've upgraded &e%upgrade% &ato level &e%level%&a!"),

	// failure messages
	FAILURE_TOO_SMALL("failure.too-small", "&cThe SkyMine you are trying to create is too small!"),
	FAILURE_NO_SPACE("failure.no-space", "&cThere is no space for the SkyMine to be placed there."),
	FAILURE_NO_FUNDS("failure.no-funds", "&cYou don't have enough money to buy this upgrade."),
	FAILURE_NO_PERMISSION("failure.no-permission", "&cYou do not have permission to perform this command."),
	FAILURE_INVALID_SENDER("failure.invalid-sender", "&cYou must be a player to perform this command."),

	// usage messages
	USAGE_SKYMINES_COMMAND("usage.skymines-command", "&7SkyMines Command:",
			"&a/skymines &7- &eOpens up the main panel for accessing your skymines.",
			"&a/skymines [id] &7- &eOpens up the panel of the specified skymine.",
			"&a/skymines list &7- &eLists the IDs of your skymines, and gives you a fast way to teleport to them.",
			"&a/skymines home [id] &7- &eTeleports to the specified skymine.",
			"&a/skymines sethome [id] &7- &eSets the home of the specified skymine.",
			"&a/skymines reset [id] &7- &eResets the specified skymine."),
	USAGE_SKYMINES_PANEL("usage.skymines-panel", "&a/skymines [id] &7- &eOpens up the panel of the specified skymine."),
	USAGE_SKYMINES_LIST("usage.skymines-list", "&a/skymines list &7- &eLists the IDs of your skymines, and gives you a fast way to teleport to them."),
	USAGE_SKYMINES_HOME("usage.skymines-home", "&a/skymines home [id] &7- &eTeleports to the specified skymine."),
	USAGE_SKYMINES_RESET("usage.skymines-reset", "&a/skymines reset [id] &7- &eResets the specified skymine."),
	USAGE_SKYMINES_GIVE("usage.skymines-give", "&a/skymines give [name] {LxHxW} {amount} &7- &eGives a skymine token to the specified player."),

	// admin messages
	RELOADED("admin.reloaded", "&aAutoSell has been reloaded!"),
	;

	private static File file;
	private static FileConfiguration config;

	private final String path;
	private List<String> messages;

	Messages(String path, String...messages) {
		this.path = path;
		this.messages = Arrays.asList(messages);
	}

	public static void init(JavaPlugin plugin) {
		file = new File(plugin.getDataFolder(), "messages.yml");
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			plugin.saveResource("messages.yml", false);
		}
	}

	public static void reload() {
		config = YamlConfiguration.loadConfiguration(file);
		Arrays.stream(Messages.values()).forEach(Messages::update);
	}

	public void update() {
		if (!config.getStringList(path).isEmpty()) {
			messages = Utils.color(config.getStringList(path));
		} else if (config.getString(path) != null) {
			messages = Collections.singletonList(Utils.color(config.getString(path)));
		} else {
			Logger.log("Messages path \"" + path + "\" has failed to load (using default value).");
			messages = Utils.color(messages);
		}
	}

	public void sendTo(CommandSender sender) {
		if (messages.isEmpty()) {
			return;
		}

		messages.forEach(sender::sendMessage);
	}

	public void sendTo(CommandSender sender, Replacer replacer) {
		if (messages.isEmpty()) {
			return;
		}

		replacer.replace(messages).forEach(sender::sendMessage);
	}

	@Override
	public String toString() {
		return String.join("\n", messages);
	}
}