package com.thetonyk.CommandsBungee.Commands;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.thetonyk.CommandsBungee.Main;
import com.thetonyk.CommandsBungee.Utils.DatabaseUtils;
import com.thetonyk.CommandsBungee.Utils.PlayerUtils;
import com.thetonyk.CommandsBungee.Utils.PlayerUtils.Rank;

import static net.md_5.bungee.api.ChatColor.*;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class KickCommand extends Command implements TabExecutor {

	public KickCommand() {
		
		super ("kick");
		
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		String player = null;
		String reason = null;
		
		if (!sender.hasPermission("proxy.kick")) {
			
			sender.sendMessage(new ComponentBuilder("Unknown command.").create());
    		return;
    		
		}
		
		if (args.length < 1) {
			
			sender.sendMessage(Main.prefix().append("Usage: /kick <player>").color(GRAY).create());
			return;
			
		}
		
		if (Main.proxy.getProxy().getPlayer(args[0]) == null) {
			
			sender.sendMessage(Main.prefix().append("'").color(GRAY).append(args[0]).color(GOLD).append("' is not online.").color(GRAY).create());
			return;
			
		}
		
		if (PlayerUtils.getRank(args[0]) != Rank.PLAYER && PlayerUtils.getRank(sender.getName()) != Rank.ADMIN) {
			
			sender.sendMessage(Main.prefix().append("'").color(GRAY).append(args[0]).color(GOLD).append("' can't only be kicked by an Admin.").color(GRAY).create());
			return;
			
		}
		
		if (args.length == 1) {
			
			sender.sendMessage(Main.prefix().append("Kick of '").color(GRAY).append(args[0]).color(GOLD).append("'...").color(GRAY).create());
			sender.sendMessage(Main.prefix().append("Choose the reason: ").color(GRAY).append("(").color(DARK_GRAY).append("Click on it").color(GRAY).append(")").color(DARK_GRAY).create());
	        
	        for (KickCommand.Reasons reasons : KickCommand.Reasons.values()) {
	        	
	        	ComponentBuilder text = new ComponentBuilder("⫸ ").color(DARK_GRAY).append(reasons.getName()).color(GOLD).italic(true);
	        	text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to choose.").color(GRAY).create()));
	        	text.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/kick " + args[0] + " " + reasons.toString().toLowerCase()));
		        sender.sendMessage(text.create());
	        	
	        }

	        return;
			
		}
		
		KickCommand.Reasons reasons = null;
		
		try {
			
			reasons = KickCommand.Reasons.valueOf(args[1].toUpperCase());
			
		} catch (Exception e) {
			
			if (PlayerUtils.getRank(sender.getName()) != Rank.ADMIN) {
				
				sender.sendMessage(Main.prefix().append("This is not a valid reason.").color(GRAY).create());
				return;
				
			}
			
		}
		
		if (args.length > 1) {
			
			if (reasons == null) {
				
				StringBuilder message = new StringBuilder();
				
				for (int i = 1; i < args.length; i++) {
					
					message.append(args[i] + " ");
					
				}
				
				reason = message.toString();
				
			} else reason = reasons.getName();
			
			player = args[0];
			
		}
		
		if (player == null || reason == null) {
			
			sender.sendMessage(Main.prefix().append("Usage: /kick <player>").color(GRAY).create());
			return;
			
		}
		
		if (reasons != null) {
		
			DatabaseUtils.sqlInsert("INSERT INTO kick (`player`, `date`, `operator`, `reason`, `server`) VALUES ('" + PlayerUtils.getId(player) + "', '" + new Date().getTime() + "', '" + PlayerUtils.getId(sender.getName()) + "', '" + reasons.toString().toLowerCase() + "', '" + (Main.proxy.getProxy().getPlayer(player) == null ? "none" : Main.proxy.getProxy().getPlayer(player).getServer().getInfo().getName()) + "');");
		
		}
		
		if (Main.proxy.getProxy().getPlayer(player) != null) Main.proxy.getProxy().getPlayer(player).disconnect(new ComponentBuilder("⫸ ").color(DARK_GRAY).append("You are ").color(GRAY).append("kicked ").color(GOLD).append("from ").color(GRAY).append("CommandsPVP ").color(GREEN).append("⫷").color(DARK_GRAY).append("\n\nReason ").color(GOLD).append("⫸ ").color(DARK_GRAY).append(reason).color(GRAY).append("\n\n⫸ ").color(DARK_GRAY).append("This is not a ban ").color(GRAY).append("⫷").color(DARK_GRAY).create());
		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		
		List<String> complete = new ArrayList<String>();
		
		if (!sender.hasPermission("proxy.kick")) {
			
    		return complete;
    		
		}
		
		if (args.length == 1) {
			
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
	
	public static enum Reasons {
		
		TEAM("Teaming in FFA", "Team"), CHAT("Spam and/or insults", "Chat rules");
	
		private String name;
		private String shortName;
		
		private Reasons(String name, String shortName) {
			
			this.name = name;
			this.shortName = shortName;
			
		}
		
		public String getName() {
			
			return name;
			
		}
		
		public String getShortName() {
			
			return shortName;
			
		}
		
	}
	
}
