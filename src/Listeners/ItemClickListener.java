package Listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import SolarFavor.Main;

public class ItemClickListener implements Listener {
	Main plugin;

	public ItemClickListener(Main plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void clickEvent(InventoryClickEvent e) {
		if (e.getClickedInventory() == null)
			return;
		if (e.getCurrentItem().getType() == Material.AIR)
			return;
		if (e.getCursor().getType() == Material.AIR)
			return;
		if (!(e.getAction() == InventoryAction.SWAP_WITH_CURSOR)) {
			return;
		}
			
		ItemStack soul = e.getCursor();
		Player player = (Player) e.getWhoClicked();
		if (this.plugin.isElixirSoul(soul)) {
			ItemStack sword = e.getCurrentItem();

			if (this.plugin.getSoulboundableMaterial().contains(sword.getType())) {

				e.setCancelled(true);
				if(this.plugin.isSoulbounded(sword)){
					if(sword.getItemMeta().getDisplayName() != null){
						player.sendMessage(this.plugin.convertColorCode(this.plugin.getConfig().getString("youritemissoulbounded").replace("<Item>", sword.getItemMeta().getDisplayName())));
						
					}else{
						player.sendMessage(this.plugin.convertColorCode(this.plugin.getConfig().getString("youritemissoulbounded").replace("<Item>", sword.getType().toString())));
					}
					return;
					
				}
				player.getInventory().setItem(e.getSlot(), this.plugin.setSoulBound(sword,
						this.plugin.getSoulLevel(soul), this.plugin.getSoulLevel(soul)));
				if(sword.getItemMeta().getDisplayName() != null){
					player.sendMessage(
						this.plugin.convertColorCode(this.plugin.getConfig().getString("addsoulboundsuccessfully")
								.replace("<Item>", sword.getItemMeta().getDisplayName())));
				}else{
					player.sendMessage(
							this.plugin.convertColorCode(this.plugin.getConfig().getString("addsoulboundsuccessfully")
									.replace("<Item>", sword.getType().toString())));
				}
				
				if (e.getCursor().getAmount() > 1) {
					e.getCursor().setAmount(e.getCursor().getAmount() - 1);
				} else if (e.getCursor().getAmount() <= 1) {
					e.setCursor(new ItemStack(Material.AIR));

				}
			}
		}
		if (this.plugin.isElixirRemoval(soul)) {
			ItemStack sword = e.getCurrentItem();
			if (this.plugin.isSoulbounded(sword)) {
				player.getInventory().setItem(e.getSlot(), new ItemStack(Material.AIR));
				player.getInventory().addItem(this.plugin.clearSoulBound(sword));
				if (e.getCursor().getAmount() > 1) {
					e.getCursor().setAmount(e.getCursor().getAmount() - 1);
				} else if (e.getCursor().getAmount() <= 1) {
					e.setCursor(new ItemStack(Material.AIR));

				}
				if(sword.getItemMeta().getDisplayName() != null){
					player.sendMessage(
						this.plugin.convertColorCode(this.plugin.getConfig().getString("removesoulboundsuccessfully")
								.replace("<Item>", sword.getItemMeta().getDisplayName())));
				}else{
					player.sendMessage(
							this.plugin.convertColorCode(this.plugin.getConfig().getString("removesoulboundsuccessfully")
									.replace("<Item>", sword.getType().toString())));
				}
			}
			
			e.setCancelled(true);
		}
	}

}
