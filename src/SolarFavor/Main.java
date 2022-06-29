package SolarFavor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import Listeners.DeathEvent;
import Listeners.ItemClickListener;

public class Main extends JavaPlugin {
	FileConfiguration config;

	@Override
	public void onEnable() {
		registerCommands();
		registerListeners();
		registerGlow();
		config = getConfig();
		config.options().copyDefaults(true);

		saveConfig();

	}

	private void registerCommands() {
		getCommand("sb").setExecutor(new MainCommand(this));

	}

	private void registerListeners() {

		this.getServer().getPluginManager().registerEvents(new ItemClickListener(this), this);
		this.getServer().getPluginManager().registerEvents(new DeathEvent(this), this);
	}

	@Override
	public void onDisable() {

	}

	@SuppressWarnings("unchecked")
	public ItemStack getElixirSoul(int Level) {
		ItemStack is = new ItemStack(Material.matchMaterial(this.getConfig().getString("elixirsoulmaterial")));

		ItemMeta im = is.getItemMeta();
		if (this.getConfig().getBoolean("soulglowing")) {
			Glow glow = new Glow(100);
			im.addEnchant(glow, 1, true);

		}
		ArrayList<String> lore = new ArrayList<>();

		if (this.getConfig().getList("soullore") != null) {

			lore = (ArrayList<String>) this.getConfig().getList("soullore");
		}
		ArrayList<String> ilore = new ArrayList<>();
		for (String a : lore) {

			ilore.add(convertColorCode(a).replace("<Level>", String.valueOf(Level)));
		}
		im.setDisplayName(
				convertColorCode(this.getConfig().getString("soulname").replace("<Level>", String.valueOf(Level))));
		im.setLore(ilore);
		is.setItemMeta(im);
		net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(is);
		nmsItem.getTag().setBoolean("solarfavor.elixirsoul", true);
		nmsItem.getTag().setInt("solarfavor.elixirsoul.level", Level);
		is = CraftItemStack.asBukkitCopy(nmsItem);
		return is;
	}

	@SuppressWarnings("unchecked")
	public ItemStack getElixirRemoval() {
		ItemStack is = new ItemStack(Material.matchMaterial(this.getConfig().getString("elixirremovalmaterial")));

		ItemMeta im = is.getItemMeta();
		if (this.getConfig().getBoolean("removalglowing")) {
			Glow glow = new Glow(100);
			im.addEnchant(glow, 1, true);
		}
		ArrayList<String> lore = new ArrayList<>();

		if (this.getConfig().getList("removallore") != null) {
			lore = (ArrayList<String>) this.getConfig().getList("removallore");
		}
		ArrayList<String> ilore = new ArrayList<>();
		for (String a : lore) {

			ilore.add(convertColorCode(a));
		}
		im.setDisplayName(convertColorCode(this.getConfig().getString("removalname")));
		im.setLore(ilore);
		is.setItemMeta(im);
		net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(is);
		nmsItem.getTag().setBoolean("solarfavor.elixirremoval", true);
		is = CraftItemStack.asBukkitCopy(nmsItem);
		return is;
	}

	public void registerGlow() {
		try {
			Field f = Enchantment.class.getDeclaredField("acceptingNew");
			f.setAccessible(true);
			f.set(null, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Glow glow = new Glow(70);
			Enchantment.registerEnchantment(glow);
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isElixirSoul(ItemStack is) {
		net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(is);

		if (!nmsItem.hasTag())
			return false;
		if (nmsItem.getTag().getBoolean("solarfavor.elixirsoul")) {

			return true;
		}
		return false;
	}

	public boolean isElixirRemoval(ItemStack is) {
		net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(is);

		if (!nmsItem.hasTag())
			return false;
		if (nmsItem.getTag().getBoolean("solarfavor.elixirremoval")) {

			return true;
		}
		return false;
	}

	public ArrayList<Material> getSoulboundableMaterial() {
		ArrayList<Material> lm = new ArrayList<>();
		for (Object a : this.getConfig().getList("soulboundablematerials")) {
			Material m = Material.matchMaterial(a.toString());
			lm.add(m);
		}
		return lm;
	}

	public String convertColorCode(String code) {
		String string = ChatColor.translateAlternateColorCodes('&', code);
		return string;
	}

	public int getSoulLevel(ItemStack is) {
		if (isElixirSoul(is)) {
			net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(is);
			int level = nmsItem.getTag().getInt("solarfavor.elixirsoul.level");
			return level;
		}
		return 0;

	}

	public boolean isSoulbounded(ItemStack is) {
		if (is.getType() == Material.AIR)
			return false;
		if (is.getItemMeta().getLore() == null)
			return false;
		ItemMeta im = is.getItemMeta();
		for (String a : im.getLore()) {
			if (a.isEmpty())
				continue;

			if (a.contains(this.convertColorCode((this.getConfig().getString("soulboundlore").replace("<left>", ""))
					.replace("<max>", "").replace("/", "")))) {
				return true;
			}
		}
		return false;

	}

	public ItemStack setSoulBound(ItemStack sword, int left, int max) {
		ItemMeta im = sword.getItemMeta();

		if (im.getLore() == null) {

			im.setLore(Arrays.asList(new String[] { this
					.convertColorCode(
							this.getConfig().getString("soulboundlore").replace("<left>", String.valueOf(left)))
					.replace("<max>", String.valueOf(max)) }));
			sword.setItemMeta(im);
			return sword;
		}

		else {
			if (isSoulbounded(sword)) {
				ArrayList<String> z = new ArrayList<>();
				for (String b : im.getLore()) {
					if (b.contains(
							this.convertColorCode((this.getConfig().getString("soulboundlore").replace("<left>", ""))
									.replace("<max>", "").replace("/", "")))) {
						z.add(this.convertColorCode(
								this.getConfig().getString("soulboundlore").replace("<left>", String.valueOf(left)))
								.replace("<max>", String.valueOf(max)));
					} else {
						z.add(b);
					}
				}
				im.setLore(z);
				sword.setItemMeta(im);
				return sword;
			}
			im.setLore(Arrays.asList(new String[] { this
					.convertColorCode(
							this.getConfig().getString("soulboundlore").replace("<left>", String.valueOf(left)))
					.replace("<max>", String.valueOf(max)) }));
			sword.setItemMeta(im);
			return sword;
		}

	}

	public int getSoulLeft(ItemStack is) {
		if (is.getType() == Material.AIR)
			return 0;
		if (is.getItemMeta().getLore() == null)
			return 0;
		ItemMeta im = is.getItemMeta();
		if (isSoulbounded(is)) {
			for (String b : im.getLore()) {
				if (b.contains(this.convertColorCode((this.getConfig().getString("soulboundlore").replace("<left>", ""))

						.replace("<max>", "").replace("/", "")))) {

					String[] sl = b.split(
							this.convertColorCode((this.getConfig().getString("soulboundlore").replace("<left>", ""))

									.replace("<max>", "").replace("/", "")));

					String m = sl[1].split("/")[0];
					return Integer.parseInt(m);
				}
			}

		}
		return 0;
	}

	public int getMaxSoul(ItemStack is) {
		if (is.getType() == Material.AIR)
			return 0;
		if (is.getItemMeta().getLore() == null)
			return 0;
		ItemMeta im = is.getItemMeta();
		if (isSoulbounded(is)) {
			for (String b : im.getLore()) {
				if (b.contains(this.convertColorCode((this.getConfig().getString("soulboundlore").replace("<left>", ""))

						.replace("<max>", "").replace("/", "")))) {

					String[] sl = b.split(
							this.convertColorCode((this.getConfig().getString("soulboundlore").replace("<left>", ""))

									.replace("<max>", "").replace("/", "")));

					String m = sl[1].split("/")[1];
					return Integer.parseInt(m);
				}
			}

		}
		return 0;
	}

	public ItemStack clearSoulBound(ItemStack is) {
		if (is.getType() == Material.AIR)
			return is;
		if (is.getItemMeta().getLore() == null)
			return is;
		ItemMeta im = is.getItemMeta();
		ArrayList<String> lore = (ArrayList<String>) is.getItemMeta().getLore();
		if (isSoulbounded(is)) {
			for (String b : im.getLore()) {
				if (b.contains(this.convertColorCode((this.getConfig().getString("soulboundlore").replace("<left>", ""))

						.replace("<max>", "").replace("/", "")))) {
					lore.remove(b);
					im.setLore(lore);
					is.setItemMeta(im);
					return is;
				}
			}
		}
		return is;

	}
}
