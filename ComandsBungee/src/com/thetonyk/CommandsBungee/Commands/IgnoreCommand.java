package com.thetonyk.CommandsBungee.Commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.thetonyk.CommandsBungee.Main;
import com.thetonyk.CommandsBungee.Utils.PlayerUtils;
import com.thetonyk.CommandsBungee.Utils.PlayerUtils.Rank;

import static net.md_5.bungee.api.ChatColor.*;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class IgnoreCommand extends Command implements TabExecutor {
	
	public static Map<UUID, List<UUID>> ignored = new HashMap<>();
	
	public IgnoreCommand() {
		
		super("ignore");
		
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		if (args.length < 1) {
			
			sender.sendMessage(Main.prefix().append("Usage: /ignore <player>").color(GRAY).create());
			return;
			
		}
		
		if (!PlayerUtils.exist(args[0])) {
			
			sender.sendMessage(Main.prefix().append("The player '").color(GRAY).append(args[0]).color(GOLD).append("' has never come on the server.").color(GRAY).create());
			return;
			
		}
		
		if (sender.getName().equalsIgnoreCase(args[0])) {
			
			sender.sendMessage(Main.prefix().append("You can't ignore yourself.").color(GRAY).create());
			return;
			
		}
		
		if (PlayerUtils.getRank(args[0]) == Rank.ADMIN || PlayerUtils.getRank(args[0]) == Rank.HOST || PlayerUtils.getRank(args[0]) == Rank.MOD || PlayerUtils.getRank(args[0]) == Rank.STAFF) {
			
			sender.sendMessage(Main.prefix().append("You can't ignore this player.").color(GRAY).create());
			return;
			
		}
		
		List<UUID> ignoredPlayers = ignored.containsKey(Main.proxy.getProxy().getPlayer(sender.getName()).getUniqueId()) ? ignored.get(Main.proxy.getProxy().getPlayer(sender.getName()).getUniqueId()) : new ArrayList<UUID>();
		
		if (ignoredPlayers.contains(PlayerUtils.getUUID(args[0]))) {
			
			ignoredPlayers.remove(PlayerUtils.getUUID(args[0]));
			ignored.put(Main.proxy.getProxy().getPlayer(sender.getName()).getUniqueId(), ignoredPlayers);
			sender.sendMessage(Main.prefix().append("The player '").color(GRAY).append(args[0]).color(GOLD).append("' is no longer ignored.").color(GRAY).create());
			return;
			
		}
		
		ignoredPlayers.add(PlayerUtils.getUUID(args[0]));
		ignored.put(Main.proxy.getProxy().getPlayer(sender.getName()).getUniqueId(), ignoredPlayers);
		sender.sendMessage(Main.prefix().append("The player '").color(GRAY).append(args[0]).color(GOLD).append("' is now ignored.").color(GRAY).create());
		return;
		
	}
	
	@Override
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
