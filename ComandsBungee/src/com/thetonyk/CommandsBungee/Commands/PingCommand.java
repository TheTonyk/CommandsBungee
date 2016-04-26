package com.thetonyk.CommandsBungee.Commands;

import java.util.ArrayList;
import java.util.List;

import com.thetonyk.CommandsBungee.Main;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class PingCommand extends Command implements TabExecutor {
	
	public PingCommand() {
		
		super("ping", "", "ms");
		
	}
	
	public void execute(CommandSender sender, String[] args) {
		
		if (args.length < 1) {
			
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7Your ping: §a" + Main.proxy.getProxy().getPlayer(sender.getName()).getPing() + "ms")).create());
			return;
			
		}
		
		if (Main.proxy.getProxy().getPlayer(args[0]) == null) {
			
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7The player '§6" + args[0] + "§7' is not online.")).create());
			return;
			
		}
		
		sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7'§6" + Main.proxy.getProxy().getPlayer(args[0]).getName() + "§7' ping: §a" + Main.proxy.getProxy().getPlayer(args[0]).getPing() + "ms")).create());
		
	}
	
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		
		List<String> players = new ArrayList<String>();
		
		if (args.length == 1) {
			
			for (ProxiedPlayer player : Main.proxy.getProxy().getPlayers()) {
				
				players.add(player.getName());
				
			}
			
		}
		
		List<String> tabCompletions = new ArrayList<String>();
		
		if (args[args.length - 1].isEmpty()) {
			
			for (String type : players) {
				
				tabCompletions.add(type);
				
			}
			
		} else {
			
			for (String type : players) {
				
				if (type.toLowerCase().startsWith(args[args.length - 1].toLowerCase())) {
					
					tabCompletions.add(type);
					
				}
				
			}
			
		}
		
		return tabCompletions;
		
	}

}
