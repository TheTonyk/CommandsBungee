package com.thetonyk.CommandsBungee.Commands;

import java.util.ArrayList;
import java.util.List;
import com.thetonyk.CommandsBungee.Main;
import com.thetonyk.CommandsBungee.Utils.PlayerUtils;
import com.thetonyk.CommandsBungee.Utils.PlayerUtils.Rank;

import static net.md_5.bungee.api.ChatColor.*;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class SocialspyCommand extends Command implements TabExecutor {

	public SocialspyCommand() {
		
		super("socialspy", "", "social");
		
	}
	
	@Override
    public void execute(CommandSender sender, String[] args) {
		
		if (!sender.hasPermission("proxy.socialspy")) {
			
			sender.sendMessage(new ComponentBuilder("Unknown command.").create());
    		return;
    		
		}
		
		if (args.length < 1 || (!args[0].equalsIgnoreCase("on") && !args[0].equalsIgnoreCase("off"))) {
			
			sender.sendMessage(Main.prefix().append("Usage: /socialspy <on|off> [server]" + (PlayerUtils.getRank(sender.toString()) == Rank.ADMIN ? " [player]" : "")).color(GRAY).create());
			return;
			
		}
		
		if (args.length > 1 && !Main.proxy.getProxy().getServers().keySet().contains(args[1]) && !args[1].equalsIgnoreCase("all")) {
			
			sender.sendMessage(Main.prefix().append("'").color(GRAY).append(args[1]).color(GOLD).append("' is not a online server.").color(GRAY).create());
			return;
			
		}
		
		ProxiedPlayer player = Main.proxy.getProxy().getPlayer(sender.toString());

		if (args.length > 2 && PlayerUtils.getRank(sender.toString()) == Rank.ADMIN) {
			
			if (Main.proxy.getProxy().getPlayer(args[2]) == null) {
			
				sender.sendMessage(Main.prefix().append("'").color(GRAY).append(args[2]).color(GOLD).append("' is not online.").color(GRAY).create());
				return;
			
			}
			
			player = Main.proxy.getProxy().getPlayer(args[2]);
			
		}
		
		if (args[0].equalsIgnoreCase("off")) {
			
			sender.sendMessage(Main.prefix().append("The social spy has been disabled.").color(GRAY).create());
			
			if (player != Main.proxy.getProxy().getPlayer(sender.toString())) player.sendMessage(Main.prefix().append("The social spy has been disabled.").color(GRAY).create());
			
			if (!Main.socialspy.containsKey(player)) return;
			
			Main.socialspy.remove(player);
			
		}
		
		if (args[0].equalsIgnoreCase("on")) {
			
			sender.sendMessage(Main.prefix().append("The social spy has been enabled.").color(GRAY).create());
			
			if (player != Main.proxy.getProxy().getPlayer(sender.toString())) player.sendMessage(Main.prefix().append("The social spy has been enabled.").color(GRAY).create());
			
			String server = args.length < 2 ? player.getServer().getInfo().getName() : args[1];
			
			Main.socialspy.put(player, server);
			
		}
    	
    }
	
	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		
		List<String> complete = new ArrayList<String>();
		
		if (!sender.hasPermission("proxy.socialspy")) return complete;

		if (args.length == 1) {
			
			complete.add("on");
			complete.add("off");
			
		} else if (args.length == 2) {
			
			for (ServerInfo server : Main.proxy.getProxy().getServers().values()) {
				
				complete.add(server.getName());
				
			}
			
			complete.add("all");
			
		} else if (args.length == 3 && PlayerUtils.getRank(sender.getName()) == Rank.ADMIN) {
			
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
