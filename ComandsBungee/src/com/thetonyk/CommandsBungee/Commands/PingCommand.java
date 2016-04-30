package com.thetonyk.CommandsBungee.Commands;

import java.util.ArrayList;
import java.util.List;

import com.thetonyk.CommandsBungee.Main;

import static net.md_5.bungee.api.ChatColor.*;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class PingCommand extends Command implements TabExecutor {
	
	public PingCommand() {
		
		super("ping", "", "ms");
		
	}
	
	public void execute(CommandSender sender, String[] args) {
		
		if (args.length < 1) {
			
			sender.sendMessage(Main.prefix().append("Your ping: ").color(GRAY).append(Main.proxy.getProxy().getPlayer(sender.getName()).getPing() + "ms").color(GREEN).create());
			return;
			
		}
		
		if (Main.proxy.getProxy().getPlayer(args[0]) == null) {
			
			sender.sendMessage(Main.prefix().append("The player '").color(GRAY).append(args[0]).color(GOLD).append("' is not online.").color(GRAY).create());
			return;
			
		}
		
		sender.sendMessage(Main.prefix().append("Ping of '").color(GRAY).append(Main.proxy.getProxy().getPlayer(args[0]).getName()).color(GOLD).append("': ").color(GRAY).append(Main.proxy.getProxy().getPlayer(args[0]).getPing() + "ms").color(GREEN).create());
		
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
