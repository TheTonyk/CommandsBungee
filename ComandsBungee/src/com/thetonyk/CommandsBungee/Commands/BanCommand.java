package com.thetonyk.CommandsBungee.Commands;

import java.sql.SQLException;
import java.sql.Statement;
import java.text.Format;
import java.text.SimpleDateFormat;
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
			
			sender.sendMessage(new ComponentBuilder("§a§lGlobal §8⫸ §7Usage: /ban <player>").create());
			return;
			
		}
		
		if (!PlayerUtils.exist(args[0])) {
			
			sender.sendMessage(new ComponentBuilder("§a§lGlobal §8⫸ §7'§6" + args[0] + "§7' has never come on the server.").create());
			return;
			
		}
		
		if (args.length == 1) {
			
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7Ban of '§6" + args[0] + "§7'...")).create());
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7Choose the reason: §8(§7Click on it§8)")).create());
	        
	        for (Reasons reasons : Reasons.values()) {
	        	
	        	ComponentBuilder text = new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§8⫸ "));
	        	text.append(ChatColor.translateAlternateColorCodes('§', "§6" + reasons.getName()));
	        	text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "Click to choose.")).create()));
	        	text.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ban " + args[0] + " " + reasons.toString().toLowerCase()));
		        sender.sendMessage(text.create());
	        	
	        }

	        return;
			
		}
		
		Reasons reasons = Reasons.OTHERS;
		
		try {
			
			reasons = Reasons.valueOf(args[1].toUpperCase());
			
		} catch (Exception exception) {
			
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7This is not a valid reason.")).create());
			return;
			
		}
		
		if (args.length == 2) {
			
			if (reasons.getTemp()) {
				
				sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7Ban of '§6" + args[0] + "§7' for §6" + reasons.getName() + "§7...")).create());
				sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7Choose the duration: §8(§7Click on it§8)")).create());
				ComponentBuilder text = new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§8⫸ "));
	        	text.append(ChatColor.translateAlternateColorCodes('§', "§6 for 1 day"));
	        	text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "Click to choose.")).create()));
	        	text.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ban " + args[0] + " " + args[1] + " 1d"));
		        sender.sendMessage(text.create());
		        text = new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§8⫸ "));
	        	text.append(ChatColor.translateAlternateColorCodes('§', "§6 for 3 day"));
	        	text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "Click to choose.")).create()));
	        	text.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ban " + args[0] + " " + args[1] + " 3d"));
		        sender.sendMessage(text.create());
		        text = new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§8⫸ "));
	        	text.append(ChatColor.translateAlternateColorCodes('§', "§6 for 1 week"));
	        	text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "Click to choose.")).create()));
	        	text.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ban " + args[0] + " " + args[1] + " 1w"));
		        sender.sendMessage(text.create());
		        text = new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§8⫸ "));
	        	text.append(ChatColor.translateAlternateColorCodes('§', "§6 for 2 week"));
	        	text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "Click to choose.")).create()));
	        	text.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ban " + args[0] + " " + args[1] + " 2w"));
		        sender.sendMessage(text.create());
		        text = new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§8⫸ "));
	        	text.append(ChatColor.translateAlternateColorCodes('§', "§6 for 1 month"));
	        	text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "Click to choose.")).create()));
	        	text.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ban " + args[0] + " " + args[1] + " 1mo"));
		        sender.sendMessage(text.create());
		        text = new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§8⫸ "));
	        	text.append(ChatColor.translateAlternateColorCodes('§', "§6 for 3 month"));
	        	text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "Click to choose.")).create()));
	        	text.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ban " + args[0] + " " + args[1] + " 3mo"));
		        sender.sendMessage(text.create());
		        text = new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§8⫸ "));
	        	text.append(ChatColor.translateAlternateColorCodes('§', "§6 for ever"));
	        	text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "Click to choose.")).create()));
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
				
				sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7Incorrect date format.")).create());
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
			
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7Usage: /ban <player>")).create());
			return;
			
		}
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			sql.executeUpdate("INSERT INTO bans (`player`, `date`, `duration`, `operator`, `reason`, `server`, `cancel`) VALUES ('" + PlayerUtils.getId(player) + "', '" + new Date().getTime() + "', '" + duration + "', '" + PlayerUtils.getId(sender.getName()) + "', '" + reason.toString().toLowerCase() + "', '" + (Main.proxy.getProxy().getPlayer(player) == null ? "none" : Main.proxy.getProxy().getPlayer(player).getServer().getInfo().getName()) + "', 0);");
			sql.close();
			
		} catch (SQLException exception) {
			
			Main.proxy.getLogger().severe("[BanCommand] Error to insert new ban of player " + player + ".");
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7Error to ban the player.")).create());
			return;
			
		}
		
		String expire = "";
		Format format = new SimpleDateFormat("dd MMM yyyy HH:mm");
		
		if (duration > 0) {
			
			expire = "\n§6Expire §8⫸ §7" + format.format(new Date().getTime() + duration);
			
		}
		
		if (Main.proxy.getProxy().getPlayer(player) != null) {
			
			Main.proxy.getProxy().getPlayer(player).disconnect(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§8⫸ §7You are §6banned §7from §aCommandsPVP §8⫷\n\n§6Reason §8⫸ §7" + reason.getName() + expire + "\n\n§8⫸ §7To appeal, contact us on Twitter §b@CommandsPVP §8⫷")).create());
			
		}
		
		for (String alt : PlayerUtils.getAlts(PlayerUtils.getUUID(player)).keySet()) {
			
			if (Main.proxy.getProxy().getPlayer(alt) != null) {
				
				Main.proxy.getProxy().getPlayer(alt).disconnect(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§8⫸ §7You are §6IP banned §7from §aCommandsPVP §8⫷\n\n§6Due to account(s) §8⫸ §7" + player + "\n\n§8⫸ §7To appeal, contact us on Twitter §b@CommandsPVP §8⫷")).create());
				
			}
			
		}
		
		Main.proxy.getProxy().broadcast(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7'§6" + player + "§7' was banned for §6" + reason.getName() + "§7.")).create());
		
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
