package com.thetonyk.CommandsBungee.Commands;

import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.thetonyk.CommandsBungee.Main;
import com.thetonyk.CommandsBungee.Utils.DatabaseUtils;
import com.thetonyk.CommandsBungee.Utils.DateUtils;
import com.thetonyk.CommandsBungee.Utils.PlayerUtils;

import static net.md_5.bungee.api.ChatColor.*;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class BanCommand extends Command implements TabExecutor {

	public BanCommand() {
		
		super ("ban");
		
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		String player = null;
		long duration = 0;
		Reasons reason = null;
		
		if (!sender.hasPermission("proxy.ban")) {
			
			sender.sendMessage(new ComponentBuilder("Unknown command.").create());
    		return;
    		
		}
		
		if (args.length < 1) {
			
			sender.sendMessage(Main.prefix().append("Usage: /ban <player>").color(GRAY).create());
			return;
			
		}
		
		if (!PlayerUtils.exist(args[0])) {
			
			sender.sendMessage(Main.prefix().append("'").color(GRAY).append(args[0]).color(GOLD).append("' has never come on the server.").color(GRAY).create());
			return;
			
		}
		
		if (args.length == 1) {
			
			sender.sendMessage(Main.prefix().append("Ban of '").color(GRAY).append(args[0]).color(GOLD).append("'...").color(GRAY).create());
			sender.sendMessage(Main.prefix().append("Choose the reason: ").color(GRAY).append("(").color(DARK_GRAY).append("Click on it").color(GRAY).append(")").color(DARK_GRAY).create());
	        
	        for (Reasons reasons : Reasons.values()) {
	        	
	        	ComponentBuilder text = new ComponentBuilder("⫸ ").color(DARK_GRAY).append(reasons.getName()).color(GOLD).italic(true);
	        	text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to choose.").color(GRAY).create()));
	        	text.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ban " + args[0] + " " + reasons.toString().toLowerCase()));
		        sender.sendMessage(text.create());
	        	
	        }

	        return;
			
		}
		
		Reasons reasons = Reasons.OTHERS;
		
		try {
			
			reasons = Reasons.valueOf(args[1].toUpperCase());
			
		} catch (Exception exception) {
			
			sender.sendMessage(Main.prefix().append("This is not a valid reason.").color(GRAY).create());
			return;
			
		}
		
		if (args.length == 2) {
			
			if (reasons.getTemp()) {
				
				sender.sendMessage(Main.prefix().append("Ban of '").color(GRAY).append(args[0]).color(GOLD).append("'...").color(GRAY).create());
				sender.sendMessage(Main.prefix().append("Choose the duration: ").color(GRAY).append("(").color(DARK_GRAY).append("Click on it").color(GRAY).append(")").color(DARK_GRAY).create());
				ComponentBuilder text = new ComponentBuilder("⫸ ").color(DARK_GRAY).append("for 1 day").color(GOLD).italic(true);
	        	text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to choose.").color(GRAY).create()));
	        	text.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ban " + args[0] + " " + args[1] + " 1d"));
		        sender.sendMessage(text.create());
		        text = new ComponentBuilder("⫸ ").color(DARK_GRAY).append("for 3 days").color(GOLD).italic(true);
	        	text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to choose.").color(GRAY).create()));
	        	text.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ban " + args[0] + " " + args[1] + " 3d"));
		        sender.sendMessage(text.create());
		        text = new ComponentBuilder("⫸ ").color(DARK_GRAY).append("for 1 week").color(GOLD).italic(true);
	        	text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to choose.").color(GRAY).create()));
	        	text.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ban " + args[0] + " " + args[1] + " 1w"));
		        sender.sendMessage(text.create());
		        text = new ComponentBuilder("⫸ ").color(DARK_GRAY).append("for 2 weeks").color(GOLD).italic(true);
	        	text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to choose.").color(GRAY).create()));
	        	text.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ban " + args[0] + " " + args[1] + " 2w"));
		        sender.sendMessage(text.create());
		        text = new ComponentBuilder("⫸ ").color(DARK_GRAY).append("for 1 month").color(GOLD).italic(true);
	        	text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to choose.").color(GRAY).create()));
	        	text.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ban " + args[0] + " " + args[1] + " 1mo"));
		        sender.sendMessage(text.create());
		        text = new ComponentBuilder("⫸ ").color(DARK_GRAY).append("for 3 months").color(GOLD).italic(true);
	        	text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to choose.").color(GRAY).create()));
	        	text.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ban " + args[0] + " " + args[1] + " 3mo"));
		        sender.sendMessage(text.create());
		        text = new ComponentBuilder("⫸ ").color(DARK_GRAY).append("for ever").color(GOLD).italic(true);
	        	text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to choose.").color(GRAY).create()));
	        	text.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ban " + args[0] + " " + args[1] + " ever"));
		        sender.sendMessage(text.create());
				return;
				
			}
			
			reason = reasons;
			player = args[0];
			duration = -1;
			
		}
		
		if (args.length > 2) {
			
			if (DateUtils.parseDateDiff(args[2]) == 0 && !args[2].equalsIgnoreCase("ever")) {
				
				sender.sendMessage(Main.prefix().append("Incorrect date format.").color(GRAY).create());
				return;
				
			}
			
		}
		
		if (args.length == 3) {
			
			player = args[0];
			reason = reasons;
			
			if (args[2].equalsIgnoreCase("ever")) {
				
				duration = -1;
				
			} else {

				duration = (DateUtils.parseDateDiff(args[2].toUpperCase()) - new Date().getTime());
				
			}
			
		}
		
		if (player == null || reason == null || duration == 0) {
			
			sender.sendMessage(Main.prefix().append("Usage: /ban <player>").color(GRAY).create());
			return;
			
		}
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			sql.executeUpdate("INSERT INTO bans (`player`, `date`, `duration`, `operator`, `reason`, `server`, `cancel`) VALUES ('" + PlayerUtils.getId(player) + "', '" + new Date().getTime() + "', '" + duration + "', '" + PlayerUtils.getId(sender.getName()) + "', '" + reason.toString().toLowerCase() + "', '" + (Main.proxy.getProxy().getPlayer(player) == null ? "none" : Main.proxy.getProxy().getPlayer(player).getServer().getInfo().getName()) + "', 0);");
			sql.close();
			
		} catch (SQLException exception) {
			
			Main.proxy.getLogger().severe("[BanCommand] Error to insert new ban of player " + player + ".");
			sender.sendMessage(Main.prefix().append("Error to ban the player.").color(GRAY).create());
			return;
			
		}
		
		ComponentBuilder message = new ComponentBuilder("⫸ ").color(DARK_GRAY).append("You are ").color(GRAY).append("banned ").color(GOLD).append("from ").color(GRAY).append("CommandsPVP ").color(GREEN).append("⫷").color(DARK_GRAY).append("\n\nReason ").color(GOLD).append("⫸ ").color(DARK_GRAY).append(reason.getName()).color(GRAY);
		SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy HH:mm");
		format.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		if (duration > 0) message.append("\nExpire ").color(GOLD).append("⫸ ").color(DARK_GRAY).append(format.format(new Date().getTime() + duration) + " UTC").color(GRAY);
		
		message.append("\n\n⫸ ").color(DARK_GRAY).append("To appeal, contact us on Twitter ").color(GRAY).append("@CommandsPVP ").color(AQUA).append("⫷").color(DARK_GRAY);
		
		if (Main.proxy.getProxy().getPlayer(player) != null) Main.proxy.getProxy().getPlayer(player).disconnect(message.create());
		
		for (String alt : PlayerUtils.getAlts(PlayerUtils.getUUID(player)).keySet()) {
			
			if (Main.proxy.getProxy().getPlayer(alt) != null) Main.proxy.getProxy().getPlayer(alt).disconnect(new ComponentBuilder("⫸ ").color(DARK_GRAY).append("You are ").color(GRAY).append("IP banned ").color(GOLD).append("from ").color(GRAY).append("CommandsPVP ").color(GREEN).append("⫷").color(DARK_GRAY).append("\n\nDue to account(s) ").color(GOLD).append("⫸ ").color(DARK_GRAY).append(player).color(GRAY).append("\n\n⫸ ").color(DARK_GRAY).append("To appeal, contact us on Twitter ").color(GRAY).append("@CommandsPVP ").color(AQUA).append("⫷").color(DARK_GRAY).create());
			
		}
		
		message = Main.prefix().append("'").color(GRAY).append(player).color(GOLD).append("' was banned for ").color(GRAY).append(reason.getName()).color(GOLD);
		
		if (args.length > 2) {
		
			String durationString = "";
			
			if (!args[2].equalsIgnoreCase("ever")) durationString = args[2].replaceAll("d", " days").replaceAll("w", " weeks").replaceAll("mo", " months");
			
			if (args[2].equalsIgnoreCase("ever")) message.append(".").color(GRAY);
			else message.append(" (").color(DARK_GRAY).append(durationString).color(GREEN).append(")").color(DARK_GRAY);
			
		} else message.append(".").color(GRAY);
		
		for (ProxiedPlayer serverPlayer : Main.proxy.getProxy().getPlayer(sender.getName()).getServer().getInfo().getPlayers()) {
		
			serverPlayer.sendMessage(message.create());
			
		}
		
		if (Main.proxy.getProxy().getPlayer(player) != null) {
			
			for (ProxiedPlayer serverPlayer : Main.proxy.getProxy().getPlayer(player).getServer().getInfo().getPlayers()) {
				
				serverPlayer.sendMessage(message.create());
				
			}
			
		}
		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		
		List<String> complete = new ArrayList<String>();
		
		if (!sender.hasPermission("proxy.ban")) return complete;
		
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
		
		FORCEFIELD("Forcefield, Aimbot", "Forcefield", false), FLY("Speedhack, Flyhack", "Speed/Fly", false), OTHERS("Autoclick or others hacks", "Other hack", false), TEAM("Teaming in FFA", "Teaming", true), LANGUAGE("Bad language and/or spam", "Chat Rules", true), ALT ("Alt Account for evading", "Alt", true);
	
		private String name;
		private String shortName;
		private Boolean temp;
		
		private Reasons(String name, String shortName, Boolean temp) {
			
			this.name = name;
			this.shortName = shortName;
			this.temp = temp;
			
		}
		
		public String getName() {
			
			return name;
			
		}
		
		public Boolean getTemp() {
			
			return temp;
			
		}
		
		public String getShortName() {
			
			return shortName;
			
		}
		
	}
	
}
