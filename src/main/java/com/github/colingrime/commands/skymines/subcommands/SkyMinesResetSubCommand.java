package com.github.colingrime.commands.skymines.subcommands;

import com.github.colingrime.locale.Messages;
import com.github.colingrime.SkyMines;
import com.github.colingrime.commands.SubCommand;
import com.github.colingrime.skymines.SkyMine;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class SkyMinesResetSubCommand implements SubCommand {

	private final SkyMines plugin;

	public SkyMinesResetSubCommand(SkyMines plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onCommand(CommandSender sender, String subCommand, String[] args) {
		Player player = (Player) sender;
		if (args.length == 0) {
			player.performCommand("skymines list");
			return;
		}

		Optional<SkyMine> skyMine = plugin.getSkyMineManager().getSkyMine(player, args[0]);
		if (skyMine.isPresent()) {
			skyMine.get().reset();
		} else {
			player.sendMessage("You don't have a skymine with the ID: " + args[0]);
		}
	}

	@Override
	public String getName() {
		return "reset";
	}

	@Override
	public Messages getUsage() {
		return Messages.USAGE_SKYMINES_RESET;
	}

	@Override
	public String getPermission() {
		return "skymines.reset";
	}

	@Override
	public boolean requirePlayer() {
		return true;
	}
}