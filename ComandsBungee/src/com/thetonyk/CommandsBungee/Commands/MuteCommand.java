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
import com.thetonyk.CommandsBungee.Utils.PlayerUtils.Rank;

import static net.md_5.bungee.api.ChatColor.*;
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
		MuteCommand.Reasons reason = null;
		
		if (!sender.hasPermission("proxy.mute")) {
			
			sender.sendMessage(new ComponentBuilder("Unknown command.").create());
    		return;
    		
		}
		
		if (args.length < 1) {
			
			sender.sendMessage(Main.prefix().append("Usage: /mute <player>").color(GRAY).create());
			return;
			
		}
		
		if (!PlayerUtils.exist(args[0])) {
			
			sender.sendMessage(Main.prefix().append("'").color(GRAY).append(args[0]).color(GOLD).append("' has never come on the server.").color(GRAY).create());
			return;
			
		}
		
		if (PlayerUtils.getRank(args[0]) != Rank.PLAYER && PlayerUtils.getRank(sender.getName()) != Rank.ADMIN) {
			
			sender.sendMessage(Main.prefix().append("'").color(GRAY).append(args[0]).color(GOLD).append("' can't only be muted by an Admin.").color(GRAY).create());
			return;
			
		}
		
		if (PlayerUtils.isMuted(PlayerUtils.getUUID(args[0])) > 0) {
			
			sender.sendMessage(Main.prefix().append("'").color(GRAY).append(args[0]).color(GOLD).append("' is already muted.").color(GRAY).create());
			return;
			
		}
		
		if (args.length == 1) {
			
			sender.sendMessage(Main.prefix().append("Mute of '").color(GRAY).append(args[0]).color(GOLD).append("'...").color(GRAY).create());
			sender.sendMessage(Main.prefix().append("Choose the reason: ").color(GRAY).append("(").color(DARK_GRAY).append("Click on it").color(GRAY).append(")").color(DARK_GRAY).create());
	        
	        for (MuteCommand.Reasons reasons : MuteCommand.Reasons.values()) {
	        	
	        	ComponentBuilder text = new ComponentBuilder("⫸ ").color(DARK_GRAY).append(reasons.getName()).color(GOLD).italic(true);
	        	text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to choose.").color(GRAY).create()));
	        	text.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mute " + args[0] + " " + reasons.toString().toLowerCase()));
		        sender.sendMessage(text.create());
	        	
	        }

	        return;
			
		}
		
		MuteCommand.Reasons reasons = Reasons.SPAM;
		
		try {
			
			reasons = MuteCommand.Reasons.valueOf(args[1].toUpperCase());
			
		} catch (Exception e) {
			
			sender.sendMessage(Main.prefix().append("This is not a valid reason.").color(GRAY).create());
			return;
			
		}
		
		if (args.length == 2) {
				
			sender.sendMessage(Main.prefix().append("Mute of '").color(GRAY).append(args[0]).color(GOLD).append("' for ").color(GRAY).append(reasons.getName()).color(GOLD).append("...").color(GRAY).create());
			sender.sendMessage(Main.prefix().append("Choose the duration: ").color(GRAY).append("(").color(DARK_GRAY).append("Click on it").color(GRAY).append(")").color(DARK_GRAY).create());
	        ComponentBuilder text = new ComponentBuilder("⫸ ").color(DARK_GRAY).append("for 15 minutes").color(GOLD);
        	text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to choose.").color(GRAY).create()));
        	text.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mute " + args[0] + " " + args[1] + " 15m"));
	        sender.sendMessage(text.create());
	        text = new ComponentBuilder("⫸ ").color(DARK_GRAY).append("for 30 minutes").color(GOLD);
        	text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to choose.").color(GRAY).create()));
        	text.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mute " + args[0] + " " + args[1] + " 30m"));
	        sender.sendMessage(text.create());
	        text = new ComponentBuilder("⫸ ").color(DARK_GRAY).append("for 1 hour").color(GOLD);
        	text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to choose.").color(GRAY).create()));
        	text.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mute " + args[0] + " " + args[1] + " 1h"));
	        sender.sendMessage(text.create());
	        text = new ComponentBuilder("⫸ ").color(DARK_GRAY).append("for 1 day").color(GOLD);
        	text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to choose.").color(GRAY).create()));
        	text.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mute " + args[0] + " " + args[1] + " 1d"));
	        sender.sendMessage(text.create());
	        text = new ComponentBuilder("⫸ ").color(DARK_GRAY).append("for 3 days").color(GOLD);
        	text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to choose.").color(GRAY).create()));
        	text.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mute " + args[0] + " " + args[1] + " 3d"));
	        sender.sendMessage(text.create());
	        text = new ComponentBuilder("⫸ ").color(DARK_GRAY).append("for 5 days").color(GOLD);
        	text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to choose.").color(GRAY).create()));
        	text.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mute " + args[0] + " " + args[1] + " 5d"));
	        sender.sendMessage(text.create());
			return;
			
		}
		
		if (args.length > 2) {
			
			if (DateUtils.parseDateDiff(args[2]) == 0) {
				
				sender.sendMessage(Main.prefix().append("Incorrect date format.").color(GRAY).create());
				return;
				
			}
			
		}
		
		if (args.length == 3) {
			
			player = args[0];
			reason = reasons;
			
			duration = (DateUtils.parseDateDiff(args[2].toUpperCase()) - new Date().getTime());
			
		}
		
		if (player == null || reason == null || duration == 0) {
			
			sender.sendMessage(Main.prefix().append("Usage: /mute <player>").color(GRAY).create());
			return;
			
		}
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			sql.executeUpdate("INSERT INTO mute (`player`, `date`, `duration`, `operator`, `reason`, `server`, `cancel`) VALUES ('" + PlayerUtils.getId(player) + "', '" + new Date().getTime() + "', '" + duration + "', '" + PlayerUtils.getId(sender.getName()) + "', '" + reason.toString().toLowerCase() + "', '" + (Main.proxy.getProxy().getPlayer(player) == null ? "none" : Main.proxy.getProxy().getPlayer(player).getServer().getInfo().getName()) + "', 0);");
			sql.close();
			
		} catch (SQLException e) {
			
			Main.proxy.getLogger().severe("[MuteCommand] Error to insert new mute of player " + player + ".");
			sender.sendMessage(Main.prefix().append("Error to mute the player.").color(GRAY).create());
			return;
			
		}
		
		String durationString = args[2].replaceAll("m", " minutes").replaceAll("h", " hours").replaceAll("d", " days");
			
		for (ProxiedPlayer serverPlayer : Main.proxy.getProxy().getPlayer(sender.getName()).getServer().getInfo().getPlayers()) {
		
			serverPlayer.sendMessage(Main.prefix().append("'").color(GRAY).append(player).color(GOLD).append("' was muted for ").color(GRAY).append(reason.getName()).color(GOLD).append(" (").color(DARK_GRAY).append(durationString).color(GREEN).append(")").color(DARK_GRAY).create());
			
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
