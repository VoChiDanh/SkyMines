package com.github.colingrime.panel;

import com.github.colingrime.SkyMines;
import com.github.colingrime.locale.Messages;
import com.github.colingrime.panel.setup.slot.PanelSlot;
import com.github.colingrime.panel.setup.slot.UpgradePanelSlot;
import com.github.colingrime.skymines.SkyMine;
import com.github.colingrime.skymines.upgrades.SkyMineUpgrades;
import com.github.colingrime.skymines.upgrades.types.SkyMineUpgrade;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class UpgradePanel extends Panel {

	private final SkyMines plugin;
	private final SkyMine skyMine;
	private final SkyMineUpgrades upgrades;

	public UpgradePanel(SkyMines plugin, Player viewer, SkyMine skyMine) {
		super(plugin, Bukkit.createInventory(null, plugin.getPanelSettings().getRows() * 9, "UPGRADES"), viewer);
		this.plugin = plugin;
		this.skyMine = skyMine;
		this.upgrades = skyMine.getUpgrades();
	}

	@Override
	protected boolean setupInventory(String[] args) {
		for (Map.Entry<Integer, PanelSlot> upgradePanel : plugin.getPanelSettings().getUpgradePanel().entrySet()) {
			int slotNum = upgradePanel.getKey();
			PanelSlot panelSlot = upgradePanel.getValue();

			setItem(slotNum, panelSlot.getItem(upgrades), (player, clickType) -> {
				if (clickType == ClickType.LEFT && upgradePanel.getValue() instanceof UpgradePanelSlot) {
					SkyMineUpgrade upgrade = upgrades.getUpgrade(((UpgradePanelSlot) panelSlot).getUpgradeType());
					
					if (upgrade.levelUp(player)) {
						String message = Messages.SUCCESS_UPGRADE.toString()
								.replaceAll("%upgrade%", panelSlot.getName(upgrades))
								.replaceAll("%level%", String.valueOf(upgrade.getLevel()));

						player.sendMessage(message);
						skyMine.reset();
					} else {
						Messages.FAILURE_NO_FUNDS.sendTo(player);
					}

					getViewer().closeInventory();
				}
			});
		}

		ItemStack fill = new ItemStack(plugin.getPanelSettings().getFill());
		if (fill.getType() != Material.AIR) {
			for (int i = 0; i < getInventory().getSize(); i++) {
				ItemStack item = getInventory().getItem(i);
				if (item != null && item.getType() == Material.AIR) {
					getInventory().setItem(i, fill);
				}
			}
		}

		return true;
	}
}