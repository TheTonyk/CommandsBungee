package com.thetonyk.CommandsBungee.Commands;

import java.util.ArrayList;
import java.util.List;
import com.thetonyk.CommandsBungee.Main;
import com.thetonyk.CommandsBungee.Utils.PlayerUtils;
import com.thetonyk.CommandsBungee.Utils.PlayerUtils.Rank;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class CmdspyCommand extends Command implements TabExecutor {

	public CmdspyCommand() {
		
		super("cmdspy", "", "spy");
		
	}
	
	@Override
    public void execute(CommandSender sender, String[] args) {
		
		if (!sender.hasPermission("proxy.cmdspy")) {
			
			sender.sendMessage(new ComponentBuilder("Unknown command.").create());
    		return;
    		
		}
		
		if (args.length < 1) {
			
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7Usage: /cmdspy <on|off> [server]" + (PlayerUtils.getRank(sender.toString()) == Rank.ADMIN ? " [player]" : ""))).create());
			return;
			
		}
		
		if (!args[0].equalsIgnoreCase("on") && !args[0].equalsIgnoreCase("off")) {
			
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7Usage: /cmdspy <on|off> [server]" + (PlayerUtils.getRank(sender.toString()) == Rank.ADMIN ? " [player]" : ""))).create());
			return;
			
		}
		
		if (args.length > 1 && !Main.proxy.getProxy().getServers().keySet().contains(args[1]) && !args[1].equalsIgnoreCase("all")) {
			
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ '§6" + args[1] + "§7' is not a started server.")).create());
			return;
			
		}
		
		ProxiedPlayer player = Main.proxy.getProxy().getPlayer(sender.toString());

		if (args.length > 2) {
			
			if (Main.proxy.getProxy().getPlayer(args[2]) == null) {
			
				sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ '§6" + args[2] + "§7' is not online.")).create());
				return;
			
			}
			
			player = Main.proxy.getProxy().getPlayer(args[2]);
			
		}
		
		if (args[0].equalsIgnoreCase("off")) {
			
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7The command spy has been disabled.")).create());
			
			if (player != Main.proxy.getProxy().getPlayer(sender.toString())) player.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7The command spy has been disabled.")).create());
			
			if (!Main.cmdspy.containsKey(player)) return;
			
			Main.cmdspy.remove(player);
			
		}
		
		if (args[0].equalsIgnoreCase("on")) {
			
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7The command spy has been enabled.")).create());
			
			if (player != Main.proxy.getProxy().getPlayer(sender.toString())) player.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7The command spy has been enabled.")).create());
			
			String server = args.length < 2 ? player.getServer().getInfo().getName() : args[1];
			
			Main.cmdspy.put(player, server);
			
		}
    	
    }
	
	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		
		List<String> complete = new ArrayList<String>();
		
		if (!sender.hasPermission("proxy.cmdspy")) return complete;

		if (args.length == 1) {
			
			complete.add("on");
			complete.add("off");
			
		} else if (args.length == 2) {
			
			for (ServerInfo server : Main.proxy.getProxy().getServers().values()) {
				
				complete.add(server.getName());
				
			}
			
			complete.add("all");
			
		} else if (args.length == 3) {
			
			for (ProxiedPlayer player : Main.proxy.getProxy().getPlayers()) {
				
				complete.add(player.getName());
				
			}
			
		}
		
		List<String> tabCompletions = new ArrayList<String>();
		
		if (args[args.length - 1].isEmpty()) {
			
			for (String type : complete) {
				
				tabCompletions.add(type);
				
			}
			
		} else {
			
			for (String type : complete) {
				
				if (type.toLowerCase().startsWith(args[args.length - 1].toLowerCase())) {
					
					tabCompletions.add(type);
					
				}
				
			}
			
		}
		
		return tabCompletions;
		
	}
	
}
