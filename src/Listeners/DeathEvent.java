package Listeners;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import SolarFavor.Main;

public class DeathEvent implements Listener {
	Main plugin;

	public DeathEvent(Main main) {
		this.plugin = main;

	}

	@EventHandler
	public void playerDiededEvent(PlayerDeathEvent e) {
		ArrayList<ItemStack> list = (ArrayList<ItemStack>) e.getDrops();
		Location loc = e.getEntity().getLocation();
		Player player = e.getEntity();
		for (ItemStack is : list) {
			if (this.plugin.isSoulbounded(is)) {
				e.setKeepInventory(true);
				
				player.getInventory().clear();
				list.remove(is);
				if (this.plugin.getSoulLeft(is) < 2) {
					player.getInventory().addItem(this.plugin.clearSoulBound(is));
					if(is.getItemMeta().getDisplayName() != null){
						player.sendMessage(this.plugin.convertColorCode(this.plugin.getConfig().getString("outofsoul").replace("<Item>", is.getItemMeta().getDisplayName())));
					}else{
						player.sendMessage(this.plugin.convertColorCode(this.plugin.getConfig().getString("outofsoul").replace("<Item>", is.getType().toString())));
					}
					
				} else {
					player.getInventory().addItem(
							this.plugin.setSoulBound(is, this.plugin.getSoulLeft(is) - 1, this.plugin.getMaxSoul(is)));
				}
				for (ItemStack is2 : list) {
					loc.getWorld().dropItem(loc, is2);
				}
				break;
			} else {
				e.setKeepInventory(false);
				continue;
			}
		}

	}
}
