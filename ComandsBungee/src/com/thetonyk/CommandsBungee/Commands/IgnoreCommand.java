package com.thetonyk.CommandsBungee.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.thetonyk.CommandsBungee.Main;
import com.thetonyk.CommandsBungee.Utils.PlayerUtils;
import com.thetonyk.CommandsBungee.Utils.PlayerUtils.Rank;

import static net.md_5.bungee.api.ChatColor.*;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class IgnoreCommand extends Command implements TabExecutor {
	
	public IgnoreCommand() {
		
		super("ignore");
		
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		List<UUID> ignoredPlayers = PlayerUtils.getIgnoredPlayers(Main.proxy.getProxy().getPlayer(sender.getName()).getUniqueId()) == null ? new ArrayList<UUID>() : PlayerUtils.getIgnoredPlayers(Main.proxy.getProxy().getPlayer(sender.getName()).getUniqueId());
		
		if (args.length < 1) {
			
			sender.sendMessage(Main.prefix().append("Usage: /ignore <player>").color(GRAY).create());
			
			if (!ignoredPlayers.isEmpty()) {
				
				ComponentBuilder players = Main.prefix().append("Ignored players: ").color(GRAY);
				int i = 1;
				
				for (UUID player : ignoredPlayers) {
					
					players.append(PlayerUtils.getName(PlayerUtils.getId(player)) + "").color(GREEN);
					if (i < ignoredPlayers.size()) players.append(", ").color(GRAY);
					else players.append(".").color(GRAY);
					i++;
					
				}
				
				sender.sendMessage(players.create());
				
			}
			
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
		
		if (ignoredPlayers.contains(PlayerUtils.getUUID(args[0]))) {
			
			ignoredPlayers.remove(PlayerUtils.getUUID(args[0]));
			sender.sendMessage(Main.prefix().append("The player '").color(GRAY).append(args[0]).color(GOLD).append("' is no longer ignored.").color(GRAY).create());
			
		} else {
		
			ignoredPlayers.add(PlayerUtils.getUUID(args[0]));
			sender.sendMessage(Main.prefix().append("The player '").color(GRAY).append(args[0]).color(GOLD).append("' is now ignored.").color(GRAY).create());
	
		}
		
		PlayerUtils.setIgnoredPlayers(Main.proxy.getProxy().getPlayer(sender.getName()).getUniqueId(), ignoredPlayers);
		
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
