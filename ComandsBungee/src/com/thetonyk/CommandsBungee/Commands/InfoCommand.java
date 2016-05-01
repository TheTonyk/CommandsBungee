
package com.thetonyk.CommandsBungee.Commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.UUID;

import com.thetonyk.CommandsBungee.Main;
import com.thetonyk.CommandsBungee.Utils.DatabaseUtils;
import com.thetonyk.CommandsBungee.Utils.PlayerUtils;
import com.thetonyk.CommandsBungee.Utils.PlayerUtils.Rank;

import static net.md_5.bungee.api.ChatColor.*;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class InfoCommand extends Command implements TabExecutor {

	public InfoCommand() {
		
		super("info", "", "i");
		
	}
	
	@Override
    public void execute(CommandSender sender, String[] args) {
		
		if (!sender.hasPermission("proxy.info")) {
			
			sender.sendMessage(new ComponentBuilder("Unknown command.").create());
    		return;
    		
		}
		
		if (args.length < 1) {
			
			sender.sendMessage(Main.prefix().append("Usage: /info <player>").color(GRAY).create());
			return;
			
		}

		if (!PlayerUtils.exist(args[0])) {
			
			sender.sendMessage(Main.prefix().append("'").color(GRAY).append(args[0]).color(GOLD).append("' has never come on the server.").color(GRAY).create());
			return;
			
		}
		
		String name = null;
		UUID uuid = null;
		String[] ips = null;
		long firstJoin = 0;
		long lastJoin = 0;
		long lastQuit = 0;
		Rank rank = Rank.PLAYER;
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT * FROM users WHERE name ='" + args[0] + "';");
			
			req.next();
			
			name = req.getString("name");
			uuid = UUID.fromString(req.getString("uuid"));
			ips = req.getString("ip").split(";");
			firstJoin = Long.parseLong(req.getString("firstJoin"));
			lastJoin = Long.parseLong(req.getString("lastJoin"));
			lastQuit = Long.parseLong(req.getString("lastQuit"));
			rank = PlayerUtils.getRank(name);
			
			sql.close();
			req.close();
			
		} catch (SQLException e) {
			
			Main.proxy.getLogger().severe("[PlayerUtils] Error to get infos of player " + args[0] + ".");
			sender.sendMessage(Main.prefix().append("Error to get infos.").color(GRAY).create());
	    	return;
			
		}
		
		Format format = new SimpleDateFormat("dd MMM yyyy HH:mm");
		
		sender.sendMessage(Main.prefix().append("All infos about '").color(GRAY).append(name).color(GOLD).append("'.").color(GRAY).create());
		sender.sendMessage(new ComponentBuilder("⫸ ").color(DARK_GRAY).append("UUID: ").color(GRAY).append(uuid.toString()).color(GOLD).create());
		sender.sendMessage(new ComponentBuilder("⫸ ").color(DARK_GRAY).append("Rank: ").color(GRAY).append(rank.getName()).color(GOLD).create());
		sender.sendMessage(new ComponentBuilder("⫸ ").color(DARK_GRAY).append("Status: ").color(GRAY).append(Main.proxy.getProxy().getPlayer(uuid) == null ? "Offline" : "Online").color(Main.proxy.getProxy().getPlayer(uuid) == null ? RED : GREEN).create());
		if (Main.proxy.getProxy().getPlayer(uuid) != null) sender.sendMessage(new ComponentBuilder("⫸ ").color(DARK_GRAY).append("Server: ").color(GRAY).append(Main.proxy.getProxy().getPlayer(uuid).getServer().getInfo().getName()).color(GOLD).create());
		
		ComponentBuilder banned = new ComponentBuilder("⫸ ").color(DARK_GRAY).append("Banned: ").color(GRAY);
		
		if (PlayerUtils.isBanned(uuid) == 0) banned.append("Not currently").color(RED);
		else {
			
			long duration = (PlayerUtils.getBanDuration(PlayerUtils.isBanned(uuid)) - (new Date().getTime() - PlayerUtils.getBanDate(PlayerUtils.isBanned(uuid))));
			long days = TimeUnit.MILLISECONDS.toDays(duration); 
			long hours = TimeUnit.MILLISECONDS.toHours(duration) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(duration)); 
			long minutes = TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration));
			
			if (days > 0) banned.append(String.valueOf(days)).color(GREEN).append("d, ").color(GRAY);
			if (hours > 0) banned.append(String.valueOf(hours)).color(GREEN).append("h, ").color(GRAY);
			if (minutes > 0) banned.append(String.valueOf(minutes)).color(GREEN).append("m.").color(GRAY);
			else banned.append("0").color(GREEN).append("m.").color(GRAY);
			
		}
		
		ComponentBuilder muted = new ComponentBuilder("⫸ ").color(DARK_GRAY).append("Muted: ").color(GRAY);
		
		if (PlayerUtils.isMuted(uuid) == 0) muted.append("Not currently").color(RED);
		else {
			
			long duration = (PlayerUtils.getMuteDuration(PlayerUtils.isMuted(uuid)) - (new Date().getTime() - PlayerUtils.getMuteDate(PlayerUtils.isMuted(uuid))));
			long days = TimeUnit.MILLISECONDS.toDays(duration); 
			long hours = TimeUnit.MILLISECONDS.toHours(duration) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(duration)); 
			long minutes = TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration));
			long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration));
			
			if (days > 0) muted.append(String.valueOf(days)).color(GREEN).append("d, ").color(GRAY);
			if (hours > 0) muted.append(String.valueOf(hours)).color(GREEN).append("h, ").color(GRAY);
			if (minutes > 0) muted.append(String.valueOf(minutes)).color(GREEN).append("m, ").color(GRAY);
			muted.append(String.valueOf(seconds)).color(GREEN).append("s.").color(GRAY);
			
		}
		
		sender.sendMessage(banned.create());
		sender.sendMessage(muted.create());
		sender.sendMessage(new ComponentBuilder("⫸ ").color(DARK_GRAY).append("First Join: ").color(GRAY).append(format.format(firstJoin)).color(GOLD).create());
		sender.sendMessage(new ComponentBuilder("⫸ ").color(DARK_GRAY).append("Last Join: ").color(GRAY).append(format.format(lastJoin)).color(GOLD).create());
		sender.sendMessage(new ComponentBuilder("⫸ ").color(DARK_GRAY).append("Last Quit: ").color(GRAY).append(lastQuit == -1 ? "Never" : format.format(lastJoin)).color(GOLD).create());
		
		if (PlayerUtils.getRank(sender.getName()) == Rank.ADMIN) { 
		
			sender.sendMessage(new ComponentBuilder("⫸ ").color(DARK_GRAY).append("All IP's used by this player:").color(GRAY).create());
			if (Main.proxy.getProxy().getPlayer(uuid) != null) sender.sendMessage(new ComponentBuilder("⫸   ").color(DARK_GRAY).append(Main.proxy.getProxy().getPlayer(uuid).getAddress().getAddress().getHostAddress()).color(GREEN).create());
			
			for (String ip : ips) {
				
				if (Main.proxy.getProxy().getPlayer(uuid) != null && ip.equalsIgnoreCase(Main.proxy.getProxy().getPlayer(uuid).getAddress().getAddress().getHostAddress())) continue;
				
				sender.sendMessage(new ComponentBuilder("⫸   ").color(DARK_GRAY).append(ip).color(RED).create());
				
			}
			
		}
		
		sender.sendMessage(new ComponentBuilder("⫸ ").color(DARK_GRAY).append("All possible alts used by this player:").color(GRAY).create());
		
		Map<String, Integer> alts = PlayerUtils.getAlts(uuid);
		
		if (alts.isEmpty()) {
			
			sender.sendMessage(new ComponentBuilder("⫸   ").color(DARK_GRAY).append("This player don't have any know alts.").color(GRAY).create());
			
		}
		
		for (Entry<String, Integer> alt : alts.entrySet()) {
			
			sender.sendMessage(new ComponentBuilder("⫸   ").color(DARK_GRAY).append(alt.getKey()).color(GOLD).append(" (").color(DARK_GRAY).append(String.valueOf(alt.getValue())).color(GREEN).append(" IPs common").color(GRAY).append(")").color(DARK_GRAY).create());
			
		}
		
		sender.sendMessage(new ComponentBuilder("⫸ ").color(DARK_GRAY).append("All punishements of this player:").color(GRAY).create());
		
		format = new SimpleDateFormat("dd'/'MM'/'yy HH:mm");
		Format formatShort = new SimpleDateFormat("dd'/'MM HH:mm");
		
		for (String[] ban : PlayerUtils.getAllBans(uuid)) {
				
				ComponentBuilder text = new ComponentBuilder("⫸   ").color(DARK_GRAY).append("Ban ").color(GOLD).append("| ").color(DARK_GRAY);
				text.append(format.format(Long.parseLong(ban[0]))).color(GRAY);
				text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Banned by ").color(GRAY).append(PlayerUtils.getName(Integer.parseInt(ban[3]))).color(GREEN).append(".").color(GRAY).create()));
				text.append(" | ").retain(FormatRetention.NONE).color(DARK_GRAY);
				text.append(BanCommand.Reasons.valueOf(ban[2].toUpperCase()).getShortName()).color(GREEN);
				text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(BanCommand.Reasons.valueOf(ban[2].toUpperCase()).getName()).color(GOLD).create()));
				text.append(" | ").retain(FormatRetention.NONE).color(DARK_GRAY).append(((Long.parseLong(ban[1]) == -1 ? "Lifetime" : formatShort.format(Long.parseLong(ban[0]) + Long.parseLong(ban[1]))))).color(GRAY);
				
				if (PlayerUtils.getRank(sender.getName()) == Rank.ADMIN) {
					
					text.append(" | ").color(DARK_GRAY);
					text.append("Cancel").color(PlayerUtils.getCanceledBan(PlayerUtils.getBanId(Long.parseLong(ban[0]), name)) == 0 ? RED : GRAY).italic(PlayerUtils.getCanceledBan(PlayerUtils.getBanId(Long.parseLong(ban[0]), name)) == 0 ? false : true);
					text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(PlayerUtils.getCanceledBan(PlayerUtils.getBanId(Long.parseLong(ban[0]), name)) == 0 ? "Cancel the ban" : "Canceled by ").color(GRAY).append(PlayerUtils.getCanceledBan(PlayerUtils.getBanId(Long.parseLong(ban[0]), name)) == 0 ? "" : PlayerUtils.getName(PlayerUtils.getCanceledBan(PlayerUtils.getBanId(Long.parseLong(ban[0]), name)))).color(GREEN).append(".").color(GRAY).create()));
					if (PlayerUtils.getCanceledBan(PlayerUtils.getBanId(Long.parseLong(ban[0]), name)) == 0) text.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/unban " + PlayerUtils.getBanId(Long.parseLong(ban[0]), name)));
					
				}
				
				sender.sendMessage(text.create());
				
		}
			
		for (String[] kick : PlayerUtils.getAllKick(uuid)) {
			
			ComponentBuilder text = new ComponentBuilder("⫸   ").color(DARK_GRAY).append("Kick ").color(GOLD).append("| ").color(DARK_GRAY);
			text.append(format.format(Long.parseLong(kick[0]))).color(GRAY);
			text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Kicked by ").color(GRAY).append(PlayerUtils.getName(Integer.parseInt(kick[2]))).color(GREEN).append(".").color(GRAY).create()));
			text.append(" | ").retain(FormatRetention.NONE).color(DARK_GRAY);
			text.append(KickCommand.Reasons.valueOf(kick[1].toUpperCase()).getShortName()).color(GREEN);
			text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(KickCommand.Reasons.valueOf(kick[1].toUpperCase()).getName()).color(GOLD).create()));
			
			sender.sendMessage(text.create());
			
		}
		
		for (String[] mute : PlayerUtils.getAllMute(uuid)) {
			
			ComponentBuilder text = new ComponentBuilder("⫸   ").color(DARK_GRAY).append("Mute ").color(GOLD).append("| ").color(DARK_GRAY);
			text.append(format.format(Long.parseLong(mute[0]))).color(GRAY);
			text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Muted by ").color(GRAY).append(PlayerUtils.getName(Integer.parseInt(mute[3]))).color(GREEN).append(".").color(GRAY).create()));
			text.append(" | ").retain(FormatRetention.NONE).color(DARK_GRAY);
			text.append(MuteCommand.Reasons.valueOf(mute[2].toUpperCase()).getShortName()).color(GREEN);
			text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(MuteCommand.Reasons.valueOf(mute[2].toUpperCase()).getName()).color(GOLD).create()));
			text.append(" | ").retain(FormatRetention.NONE).color(DARK_GRAY).append(formatShort.format(Long.parseLong(mute[0]) + Long.parseLong(mute[1]))).color(GRAY);
			
			if (PlayerUtils.getRank(sender.getName()) == Rank.ADMIN) {
				
				text.append(" | ").color(DARK_GRAY);
				text.append("Cancel").color(PlayerUtils.getCanceledMute(PlayerUtils.getMuteId(Long.parseLong(mute[0]), name)) == 0 ? RED : GRAY).italic(PlayerUtils.getCanceledMute(PlayerUtils.getMuteId(Long.parseLong(mute[0]), name)) == 0 ? false : true);
				text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(PlayerUtils.getCanceledMute(PlayerUtils.getMuteId(Long.parseLong(mute[0]), name)) == 0 ? "Cancel the mute" : "Canceled by ").color(GRAY).append(PlayerUtils.getCanceledMute(PlayerUtils.getMuteId(Long.parseLong(mute[0]), name)) == 0 ? "" : PlayerUtils.getName(PlayerUtils.getCanceledMute(PlayerUtils.getMuteId(Long.parseLong(mute[0]), name)))).color(GREEN).append(".").color(GRAY).create()));
				if (PlayerUtils.getCanceledMute(PlayerUtils.getMuteId(Long.parseLong(mute[0]), name)) == 0) text.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/unmute " + PlayerUtils.getMuteId(Long.parseLong(mute[0]), name)));
				
			}
			
			sender.sendMessage(text.create());
			
		}
    	
    }
	
	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		
		List<String> complete = new ArrayList<String>();
		
		if (!sender.hasPermission("proxy.info")) {
			
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
	
}
