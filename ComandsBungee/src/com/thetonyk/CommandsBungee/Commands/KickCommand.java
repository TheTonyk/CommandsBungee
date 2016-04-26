package com.thetonyk.CommandsBungee.Commands;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.thetonyk.CommandsBungee.Main;
import com.thetonyk.CommandsBungee.Utils.DatabaseUtils;
import com.thetonyk.CommandsBungee.Utils.PlayerUtils;
import com.thetonyk.CommandsBungee.Utils.PlayerUtils.Rank;

import net.md_5.bungee.api.ChatColor;
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
			
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7Usage: /kick <player>")).create());
			return;
			
		}
		
		if (Main.proxy.getProxy().getPlayer(args[0]) == null) {
			
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7'§6" + args[0] + "§7' is not online.")).create());
			return;
			
		}
		
		if (args.length == 1) {
			
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7Kick of '§6" + args[0] + "§7'...")).create());
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7Choose the reason: §8(§7Click on it§8)")).create());
	        
	        for (Reasons reasons : Reasons.values()) {
	        	
	        	ComponentBuilder text = new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§8⫸ "));
	        	text.append(ChatColor.translateAlternateColorCodes('§', "§6" + reasons.getName()));
	        	text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "Click to choose.")).create()));
	        	text.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/kick " + args[0] + " " + reasons.toString().toLowerCase()));
		        sender.sendMessage(text.create());
	        	
	        }

	        return;
			
		}
		
		Reasons reasons = null;
		
		try {
			
			reasons = Reasons.valueOf(args[1].toUpperCase());
			
		} catch (Exception e) {
			
			if (PlayerUtils.getRank(sender.getName()) != Rank.ADMIN) {
				
				sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7This is not a valid reason.")).create());
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
			
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7Usage: /kick <player>")).create());
			return;
			
		}
		
		if (reasons != null) {
		
			try {
				
				Statement sql = DatabaseUtils.getConnection().createStatement();
				sql.executeUpdate("INSERT INTO kick (`player`, `date`, `operator`, `reason`, `server`) VALUES ('" + PlayerUtils.getId(player) + "', '" + new Date().getTime() + "', '" + PlayerUtils.getId(sender.getName()) + "', '" + reasons.toString().toLowerCase() + "', '" + (Main.proxy.getProxy().getPlayer(player) == null ? "none" : Main.proxy.getProxy().getPlayer(player).getServer().getInfo().getName()) + "');");
				sql.close();
				
			} catch (SQLException e) {
				
				Main.proxy.getLogger().severe("[KickCommand] Error to insert new kick of player " + player + ".");
				sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7Error to kick the player.")).create());
				return;
				
			}
		
		}
		
		if (Main.proxy.getProxy().getPlayer(player) != null) Main.proxy.getProxy().getPlayer(player).disconnect(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§8⫸ §7You are §6kicked §7from §aCommandsPVP §8⫷\n\n§6Reason §8⫸ §7" + reason + "\n\n§8⫸ §7This is not a ban §8⫷")).create());
		
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
