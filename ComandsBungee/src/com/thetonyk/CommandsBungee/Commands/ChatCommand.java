package com.thetonyk.CommandsBungee.Commands;

import static net.md_5.bungee.api.ChatColor.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thetonyk.CommandsBungee.Main;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class ChatCommand extends Command implements TabExecutor {
	
	public ChatCommand() {
		
		super("chat");
		
	}
	
	public static Map<String, Boolean> muted = new HashMap<String, Boolean>();
	
	@Override
    public void execute(CommandSender sender, String[] args) {
		
		if (!sender.hasPermission("proxy.chat")) {
			
			sender.sendMessage(new ComponentBuilder("Unknown command.").create());
    		return;
    		
		}
		
		if (args.length < 1 || (!args[0].equalsIgnoreCase("mute") && !args[0].equalsIgnoreCase("clear"))) {
			
			sender.sendMessage(Main.prefix().append("Usage: /chat <mute|clear> [server]").color(GRAY).create());
			return;
			
		}
		
		if (args.length > 1 && !Main.proxy.getProxy().getServers().keySet().contains(args[1]) && !args[1].equalsIgnoreCase("all")) {
			
			sender.sendMessage(Main.prefix().append("'").color(GRAY).append(args[1]).color(GOLD).append("' is not a online server.").color(GRAY).create());
			return;
			
		}
		
		if (args[0].equalsIgnoreCase("clear")) {
			
			for (int i = 0; i < 100; i++) {
				
				if (args.length > 1 && args[1].equalsIgnoreCase("all")) {
					
					Main.proxy.getProxy().broadcast(new ComponentBuilder("").create());
					continue;
						
				}
				
				for (ProxiedPlayer online : args.length > 1 ? Main.proxy.getProxy().getServerInfo(args[1]).getPlayers() : Main.proxy.getProxy().getPlayer(sender.getName()).getServer().getInfo().getPlayers()) {
					
					online.sendMessage(new ComponentBuilder("").create());
					
				}
				
			}
			
			if (args.length > 1 && args[1].equalsIgnoreCase("all")) {
				
				Main.proxy.getProxy().broadcast(Main.prefix().append("The chat has been cleared.").color(GRAY).create());
				return;
					
			}
			
			for (ProxiedPlayer online : args.length > 1 ? Main.proxy.getProxy().getServerInfo(args[1]).getPlayers() : Main.proxy.getProxy().getPlayer(sender.getName()).getServer().getInfo().getPlayers()) {
				
				online.sendMessage(Main.prefix().append("The chat has been cleared.").color(GRAY).create());
				
			}
			
			return;
			
		}
		
		if (args[0].equalsIgnoreCase("mute")) {
			
			String server = args.length > 1 ? args[1] : Main.proxy.getProxy().getPlayer(sender.getName()).getServer().getInfo().getName();
			
			if (!muted.containsKey(server)) muted.put(server, false);
			
			if (muted.containsKey("all") && muted.get("all") == true && !server.equalsIgnoreCase("all")) {
				
				sender.sendMessage(Main.prefix().append("The chat is global mute. You can't mute this server.").color(GRAY).create());
				return;
				
			}
			
			if (muted.get(server)) {
				
				muted.put(server, false);
				
				if (server.equalsIgnoreCase("all")) Main.proxy.getProxy().broadcast(Main.prefix().append("The chat is no longer muted.").color(GRAY).create());
				else {
					
					for (ProxiedPlayer player : Main.proxy.getProxy().getServerInfo(server).getPlayers()) {
						
						player.sendMessage(Main.prefix().append("The chat is no longer muted.").color(GRAY).create());
						
					}
					
					if (!Main.proxy.getProxy().getPlayer(sender.getName()).getServer().getInfo().getName().equalsIgnoreCase(server)) Main.proxy.getProxy().getPlayer(sender.getName()).sendMessage(Main.prefix().append("The chat is no longer muted in the server '").color(GRAY).append(server).color(GOLD).append("'.").color(GRAY).create());
					
				}
				
				return;
				
			}
			
			if (server.equalsIgnoreCase("all")) muted.clear();
			muted.put(server, true);
			
			if (server.equalsIgnoreCase("all")) Main.proxy.getProxy().broadcast(Main.prefix().append("The chat is now muted.").color(GRAY).create());
			else {
				
				for (ProxiedPlayer player : Main.proxy.getProxy().getServerInfo(server).getPlayers()) {
					
					player.sendMessage(Main.prefix().append("The chat is now muted.").color(GRAY).create());
					
				}
				
				if (!Main.proxy.getProxy().getPlayer(sender.getName()).getServer().getInfo().getName().equalsIgnoreCase(server)) Main.proxy.getProxy().getPlayer(sender.getName()).sendMessage(Main.prefix().append("The chat is now muted in the server '").color(GRAY).append(server).color(GOLD).append("'.").color(GRAY).create());
				
			}
			
			return;
			
		}
    	
    }
	
	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		
		List<String> complete = new ArrayList<String>();
		
		if (!sender.hasPermission("proxy.chat")) return complete;

		if (args.length == 1) {
			
			complete.add("mute");
			complete.add("clear");
			
		} else if (args.length == 2) {
			
			for (ServerInfo server : Main.proxy.getProxy().getServers().values()) {
				
				complete.add(server.getName());
				
			}
			
			complete.add("all");
			
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
