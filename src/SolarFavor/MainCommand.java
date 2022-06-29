package SolarFavor;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class MainCommand implements CommandExecutor {
	Main plugin;

	public MainCommand(Main main) {
		this.plugin = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandlabel, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			
			if (cmd.getName().equalsIgnoreCase("sb") && player.hasPermission("elixirsoul.op") && args.length > 0) {
				if (args[0].equalsIgnoreCase("reload")) {
					player.sendMessage("reloaded ElixirSoul");
					this.plugin.saveConfig();
					this.plugin.reloadConfig();
					this.plugin.saveConfig();
				}
				else if (args[0].equalsIgnoreCase("give") && args.length > 1) {
					Player specify = Bukkit.getPlayer(args[1]);
					int level = 5;
					if (args.length > 2) {

						level = Integer.parseInt(args[2]);
					}
					specify.getInventory().addItem(this.plugin.getElixirSoul(level));
					player.sendMessage(
							ChatColor.GREEN + "You gave " + specify.getName() + " a elixir soul Level" + level);
					return true;

				}
				else if (args[0].equalsIgnoreCase("remove") && args.length > 1) {
					Player specify = Bukkit.getPlayer(args[1]);

					specify.getInventory().addItem(this.plugin.getElixirRemoval());
					player.sendMessage(ChatColor.GREEN + "You gave " + specify.getName() + " a elixir removal");
					return true;

				}
				
				else if (args[0].equalsIgnoreCase("help")) {
					player.sendMessage(ChatColor.AQUA
							+ "/sb give [playername] [level] To give [playername] a elixir soul level [level]");
					player.sendMessage(
							ChatColor.AQUA + "/sb remove [playername] To give [playername] a elixir removal]");
				} else if (args.length < 1){
					player.sendMessage(ChatColor.DARK_RED + "This command is not exist , do /sb help");
					return false;
				}

			}else{
				return false;
			}
		}
		return false;
	}

}
