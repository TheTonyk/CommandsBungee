package com.thetonyk.CommandsBungee.Commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

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
			
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7Usage: /info <player>")).create());
			return;
			
		}

		if (!PlayerUtils.exist(args[0])) {
			
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ '§6" + args[0] + "§7' has never come on the server.")).create());
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
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7Error to get infos.")).create());
	    	return;
			
		}
		
		Format format = new SimpleDateFormat("dd MMM yyyy HH:mm");
		
		sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7All infos about '§6" + name + "§7'.")).create());
		sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§8⫸ §7UUID: '§6" + uuid.toString() + "§7'.")).create());
		sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§8⫸ §7Rank: '§6" + rank.getName() + "§7'.")).create());
		sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§8⫸ §7Status: " + (Main.proxy.getProxy().getPlayer(uuid) == null ? "§cOffline" : "§aOnline") + "§7.")).create());
		if (Main.proxy.getProxy().getPlayer(uuid) != null) sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§8⫸ §7Server: '§6" + Main.proxy.getProxy().getPlayer(uuid).getServer().getInfo().getName() + "§7'.")).create());
		sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§8⫸ §7First Join: '§6" + format.format(firstJoin) + "§7'.")).create());
		sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§8⫸ §7Last Join: '§6" + format.format(lastJoin) + "§7'.")).create());
		sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§8⫸ §7Last Quit: '§6" + (lastQuit == -1 ? "Never" : format.format(lastJoin)) + "§7'.")).create());
		
		if (PlayerUtils.getRank(sender.getName()) == Rank.ADMIN) { 
		
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§8⫸ §7All IP's used by this player:")).create());
			if (Main.proxy.getProxy().getPlayer(uuid) != null) sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§8⫸   §a" + Main.proxy.getProxy().getPlayer(uuid).getAddress().getAddress().getHostAddress())).create());
			
			for (String ip : ips) {
				
				if (Main.proxy.getProxy().getPlayer(uuid) != null && ip.equalsIgnoreCase(Main.proxy.getProxy().getPlayer(uuid).getAddress().getAddress().getHostAddress())) continue;
				
				sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§8⫸   §c" + ip)).create());
				
			}
			
		}
		
		sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§8⫸ §7All possible alts used by this player:")).create());
		
		Map<String, Integer> alts = PlayerUtils.getAlts(uuid);
		
		if (alts.isEmpty()) {
			
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§8⫸   §7This player don't have any know alts.")).create());
			
		}
		
		for (Entry<String, Integer> alt : alts.entrySet()) {
			
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§8⫸   §a" + alt.getKey() + " §8(§a" + alt.getValue() + " §7IPs common§8)")).create());
			
		}
		
		sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§8⫸ §7All punishements of this player:")).create());
		
		format = new SimpleDateFormat("dd'/'MM'/'yy HH:mm");
		Format formatShort = new SimpleDateFormat("dd'/'MM HH:mm");
		
		for (String[] ban : PlayerUtils.getAllBans(uuid)) {
				
				ComponentBuilder text = new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§8⫸   §6Ban §8| "));
				text.append(ChatColor.translateAlternateColorCodes('§', "§7" + format.format(Long.parseLong(ban[0]))));
				text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§7Banned by §a" + PlayerUtils.getName(Integer.parseInt(ban[3])))).create()));
				text.append(ChatColor.translateAlternateColorCodes('§', " §8| "));
				text.append(ChatColor.translateAlternateColorCodes('§', "§a" + BanCommand.Reasons.valueOf(ban[2].toUpperCase()).getShortName()));
				text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a" + BanCommand.Reasons.valueOf(ban[2].toUpperCase()).getName())).create()));
				text.append(ChatColor.translateAlternateColorCodes('§', " §8| §7" + ((Long.parseLong(ban[1]) == -1 ? "Lifetime" : formatShort.format(Long.parseLong(ban[0]) + Long.parseLong(ban[1]))))));
				
				if (PlayerUtils.getRank(sender.getName()) == Rank.ADMIN) {
					
					text.append(ChatColor.translateAlternateColorCodes('§', " §8| "));
					text.append(ChatColor.translateAlternateColorCodes('§', (PlayerUtils.getCanceledBan(PlayerUtils.getBanId(Long.parseLong(ban[0]), name)) == 0 ? "§c" : "§7§o") + "Cancel"));
					text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', (PlayerUtils.getCanceledBan(PlayerUtils.getBanId(Long.parseLong(ban[0]), name)) == 0 ? "§7Cancel the ban" : "§7Canceled by §a" + PlayerUtils.getName(PlayerUtils.getCanceledBan(PlayerUtils.getBanId(Long.parseLong(ban[0]), name)))))).create()));
					if (PlayerUtils.getCanceledBan(PlayerUtils.getBanId(Long.parseLong(ban[0]), name)) == 0) text.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/unban " + PlayerUtils.getBanId(Long.parseLong(ban[0]), name)));
					
				}
				
				sender.sendMessage(text.create());
				
		}
			
		for (String[] kick : PlayerUtils.getAllKick(uuid)) {
			
			ComponentBuilder text = new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§8⫸   §6Kick §8| "));
			text.append(ChatColor.translateAlternateColorCodes('§', "§7" + format.format(Long.parseLong(kick[0]))));
			text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§7Kicked by §a" + PlayerUtils.getName(Integer.parseInt(kick[2])))).create()));
			text.append(ChatColor.translateAlternateColorCodes('§', " §8| "));
			text.append(ChatColor.translateAlternateColorCodes('§', "§a" + KickCommand.Reasons.valueOf(kick[1].toUpperCase()).getShortName()));
			text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a" + KickCommand.Reasons.valueOf(kick[1].toUpperCase()).getName())).create()));
			
			sender.sendMessage(text.create());
			
		}
		
		for (String[] mute : PlayerUtils.getAllMute(uuid)) {
			
			ComponentBuilder text = new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§8⫸   §6Mute §8| "));
			text.append(ChatColor.translateAlternateColorCodes('§', "§7" + format.format(Long.parseLong(mute[0]))));
			text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§7Muted by §a" + PlayerUtils.getName(Integer.parseInt(mute[3])))).create()));
			text.append(ChatColor.translateAlternateColorCodes('§', " §8| "));
			text.append(ChatColor.translateAlternateColorCodes('§', "§a" + MuteCommand.Reasons.valueOf(mute[2].toUpperCase()).getShortName()));
			text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a" + MuteCommand.Reasons.valueOf(mute[2].toUpperCase()).getName())).create()));
			text.append(ChatColor.translateAlternateColorCodes('§', " §8| §7" + formatShort.format(Long.parseLong(mute[0]) + Long.parseLong(mute[1]))));
			
			if (PlayerUtils.getRank(sender.getName()) == Rank.ADMIN) {
				
				text.append(ChatColor.translateAlternateColorCodes('§', " §8| "));
				text.append(ChatColor.translateAlternateColorCodes('§', (PlayerUtils.getCanceledMute(PlayerUtils.getMuteId(Long.parseLong(mute[0]), name)) == 0 ? "§c" : "§7§o") + "Cancel"));
				text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', (PlayerUtils.getCanceledMute(PlayerUtils.getMuteId(Long.parseLong(mute[0]), name)) == 0 ? "§7Cancel the mute" : "§7Canceled by §a" + PlayerUtils.getName(PlayerUtils.getCanceledMute(PlayerUtils.getMuteId(Long.parseLong(mute[0]), name)))))).create()));
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
