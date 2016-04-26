package com.thetonyk.CommandsBungee.Commands;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.thetonyk.CommandsBungee.Main;
import com.thetonyk.CommandsBungee.Utils.DatabaseUtils;
import com.thetonyk.CommandsBungee.Utils.DateUtils;
import com.thetonyk.CommandsBungee.Utils.PlayerUtils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class MuteCommand extends Command implements TabExecutor {

	public MuteCommand() {
		
		super ("mute");
		
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		String player = null;
		long duration = 0;
		Reasons reason = null;
		
		if (!sender.hasPermission("proxy.mute")) {
			
			sender.sendMessage(new ComponentBuilder("Unknown command.").create());
    		return;
    		
		}
		
		if (args.length < 1) {
			
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7Usage: /mute <player>")).create());
			return;
			
		}
		
		if (!PlayerUtils.exist(args[0])) {
			
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7'§6" + args[0] + "§7' has never come on the server.")).create());
			return;
			
		}
		
		if (args.length == 1) {
			
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7Mute of '§6" + args[0] + "§7'...")).create());
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7Choose the reason: §8(§7Click on it§8)")).create());
	        
	        for (Reasons reasons : Reasons.values()) {
	        	
	        	ComponentBuilder text = new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§8⫸ "));
	        	text.append(ChatColor.translateAlternateColorCodes('§', "§6" + reasons.getName()));
	        	text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "Click to choose.")).create()));
	        	text.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mute " + args[0] + " " + reasons.toString().toLowerCase()));
		        sender.sendMessage(text.create());
	        	
	        }

	        return;
			
		}
		
		Reasons reasons = Reasons.SPAM;
		
		try {
			
			reasons = Reasons.valueOf(args[1].toUpperCase());
			
		} catch (Exception e) {
			
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7This is not a valid reason.")).create());
			return;
			
		}
		
		if (args.length == 2) {
				
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7Mute of '§6" + args[0] + "§7' for §6" + reasons.getName() + "§7...")).create());
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7Choose the duration: §8(§7Click on it§8)")).create());
	        ComponentBuilder text = new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§8⫸ "));
        	text.append(ChatColor.translateAlternateColorCodes('§', "§6 for 15 minutes"));
        	text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "Click to choose.")).create()));
        	text.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mute " + args[0] + " " + args[1] + "15m"));
	        sender.sendMessage(text.create());
	        text = new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§8⫸ "));
        	text.append(ChatColor.translateAlternateColorCodes('§', "§6 for 30 minutes"));
        	text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "Click to choose.")).create()));
        	text.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mute " + args[0] + " " + args[1] + "30m"));
	        sender.sendMessage(text.create());
	        text = new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§8⫸ "));
        	text.append(ChatColor.translateAlternateColorCodes('§', "§6 for 1 hour"));
        	text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "Click to choose.")).create()));
        	text.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mute " + args[0] + " " + args[1] + "1h"));
	        sender.sendMessage(text.create());
	        text = new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§8⫸ "));
        	text.append(ChatColor.translateAlternateColorCodes('§', "§6 for 1 day"));
        	text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "Click to choose.")).create()));
        	text.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mute " + args[0] + " " + args[1] + "1d"));
	        sender.sendMessage(text.create());
	        text = new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§8⫸ "));
        	text.append(ChatColor.translateAlternateColorCodes('§', "§6 for 3 days"));
        	text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "Click to choose.")).create()));
        	text.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mute " + args[0] + " " + args[1] + "3d"));
	        sender.sendMessage(text.create());
	        text = new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§8⫸ "));
        	text.append(ChatColor.translateAlternateColorCodes('§', "§6 for 5 days"));
        	text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "Click to choose.")).create()));
        	text.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mute " + args[0] + " " + args[1] + "5d"));
	        sender.sendMessage(text.create());
			return;
			
		}
		
		if (args.length > 2) {
			
			if (DateUtils.parseDateDiff(args[2]) == 0) {
				
				sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7Incorrect date format.")).create());
				return;
				
			}
			
		}
		
		if (args.length == 3) {
			
			player = args[0];
			reason = reasons;
			
			duration = (DateUtils.parseDateDiff(args[2].toUpperCase()) - new Date().getTime());
			
		}
		
		if (player == null || reason == null || duration == 0) {
			
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7Usage: /mute <player>")).create());
			return;
			
		}
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			sql.executeUpdate("INSERT INTO mute (`player`, `date`, `duration`, `operator`, `reason`, `server`, `cancel`) VALUES ('" + PlayerUtils.getId(player) + "', '" + new Date().getTime() + "', '" + duration + "', '" + PlayerUtils.getId(sender.getName()) + "', '" + reason.toString().toLowerCase() + "', '" + (Main.proxy.getProxy().getPlayer(player) == null ? "none" : Main.proxy.getProxy().getPlayer(player).getServer().getInfo().getName()) + "', 0);");
			sql.close();
			
		} catch (SQLException e) {
			
			Main.proxy.getLogger().severe("[MuteCommand] Error to insert new mute of player " + player + ".");
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7Error to mute the player.")).create());
			return;
			
		}
		
		String durationString = args[2].replaceAll("m", " minutes").replaceAll("d", " days");
		
		if (Main.proxy.getProxy().getPlayer(player) == null) return;
			
		for (ProxiedPlayer serverPlayer : Main.proxy.getProxy().getPlayer(player).getServer().getInfo().getPlayers()) {
		
			serverPlayer.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7'§6" + player + "§7' was muted for §6" + reason.getName() + "§7 §8(§a" + durationString + "§8)")).create());
			
		}
		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		
		List<String> complete = new ArrayList<String>();
		
		if (!sender.hasPermission("proxy.mute")) {
			
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
		
		SPAM("Spam", "Spam"), SPOIL("Spoiling when dead", "Spoil"), INSULTS("Insults, disrespect and/or provocation", "Insults"), HACKUSATION("Excessive hackusations", "Hackusation");
	
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
