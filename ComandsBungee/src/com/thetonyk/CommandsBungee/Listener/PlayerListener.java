package com.thetonyk.CommandsBungee.Listener;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.thetonyk.CommandsBungee.Main;
import com.thetonyk.CommandsBungee.Commands.BanCommand;
import com.thetonyk.CommandsBungee.Commands.ChatCommand;
import com.thetonyk.CommandsBungee.Utils.PermissionsUtils;
import com.thetonyk.CommandsBungee.Utils.PlayerUtils;
import com.thetonyk.CommandsBungee.Utils.PlayerUtils.Rank;

import static net.md_5.bungee.api.ChatColor.*;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerListener implements Listener {
	
	@EventHandler
	public void onProxyPing (ProxyPingEvent event) {
		
		int version = event.getResponse().getVersion().getProtocol();
		ComponentBuilder text = new ComponentBuilder("                    §6§k|||§r §a§lCommandsPVP §r§6§k|||§r\n§a⫸       §61.8 §7& §61.9 §7Arenas §a| §7Follow §b@CommandsPVP      §a⫷");
		if (event.getConnection().getVirtualHost().getHostName().equalsIgnoreCase("92.222.234.237") || event.getConnection().getVirtualHost().getHostName().equalsIgnoreCase("uhc.commandspvp.com")) text = new ComponentBuilder("                    §6§k|||§r §a§lCommandsPVP §r§6§k|||§r\n§a⫸       §6UHC §7coming soon §a| §7Follow §b@CommandsPVP      §a⫷");
		ServerPing.Protocol protocol;
		
		if (version == 47 || version == 107 || version == 108 || version == 109) {
		
			protocol = new ServerPing.Protocol("§61.8.x §a| §61.9.x §7only", version);
		
		} else {
			
			protocol = new ServerPing.Protocol("§61.8.x §a| §61.9.x §7only", 109);
			
		}
		
		ServerPing.PlayerInfo[] playersList = new ServerPing.PlayerInfo[3];
		
		playersList[0] = new ServerPing.PlayerInfo(" ", UUID.randomUUID());
		playersList[1] = new ServerPing.PlayerInfo(" §7You can connect in §61.8.x §7and §61.9.x §7Minecraft versions. ", UUID.randomUUID());
		playersList[2] = new ServerPing.PlayerInfo(" ", UUID.randomUUID());
		
		int count = Main.proxy.getProxy().getOnlineCount();
		if (event.getConnection().getVirtualHost().getHostName().equalsIgnoreCase("92.222.234.237") || event.getConnection().getVirtualHost().getHostName().equalsIgnoreCase("uhc.commandspvp.com")) count = Main.proxy.getProxy().getServerInfo("uhc").getPlayers().size();
		
		ServerPing.Players players = new ServerPing.Players(250, count, playersList);
		
		event.getResponse().setDescriptionComponent(text.create()[0]);
		event.getResponse().setVersion(protocol);
		event.getResponse().setPlayers(players);
		
	}
	
	@EventHandler
	public void onJoin (PostLoginEvent event) {
		
		PermissionsUtils.setPermissions(event.getPlayer());
		
		if (!PlayerUtils.exist(event.getPlayer())) {
			
			for (ProxiedPlayer player : Main.proxy.getProxy().getPlayers()) {
				
				if (player == event.getPlayer()) continue;
				
				if (player.getServer().getInfo().getName().equalsIgnoreCase("hub")) player.sendMessage(new ComponentBuilder("⫸ ").color(DARK_GRAY).append("Welcome to ").color(GRAY).append(event.getPlayer().getName()).color(GREEN).append(" on the server!").color(GRAY).append(" (").color(DARK_GRAY).append("#").color(GRAY).append(String.valueOf((PlayerUtils.uniquePlayers() + 1))).color(GOLD).append(")").color(DARK_GRAY).create());
				
			}
			
			event.getPlayer().sendMessage(new ComponentBuilder("⫸ ").color(DARK_GRAY).append("Welcome to ").color(GRAY).append(event.getPlayer().getName()).color(GREEN).append(" on the server!").color(GRAY).append(" (").color(DARK_GRAY).append("#").color(GRAY).append(String.valueOf((PlayerUtils.uniquePlayers() + 1))).color(GOLD).append(")").color(DARK_GRAY).create());
			
		}
		
		PlayerUtils.joinUpdatePlayer(event.getPlayer().getName(), event.getPlayer().getUniqueId(), event.getPlayer().getAddress().getAddress().getHostAddress());
		
	}
	
	@EventHandler
	public void onQuit (PlayerDisconnectEvent event) {
		
		PlayerUtils.leaveUpdatePlayer(event.getPlayer());
		PermissionsUtils.clearPermissions(event.getPlayer());
		
		if (Main.newPseudo.containsKey(event.getPlayer().getUniqueId())) Main.newPseudo.remove(event.getPlayer().getUniqueId());
		
	}
	
	@EventHandler
	public void onChat (ChatEvent event) {
		
		if (event.isCommand() && !event.getMessage().substring(1).startsWith("p ") && !event.getMessage().substring(1).startsWith("private ") && !event.getMessage().substring(1).startsWith("o ") && !event.getMessage().substring(1).startsWith("organize ") && !event.getMessage().substring(1).startsWith("msg ") && !event.getMessage().substring(1).startsWith("tell ") && !event.getMessage().substring(1).startsWith("w ") && !event.getMessage().substring(1).startsWith("reply ") && !event.getMessage().substring(1).startsWith("r ")) {
			
			for (Entry<ProxiedPlayer, String> player : Main.cmdspy.entrySet()) {
				
				if (player.getKey().getName().equalsIgnoreCase(event.getSender().toString())) continue;
				
				if (PlayerUtils.getRank(((ProxiedPlayer) event.getSender()).getName()) == Rank.ADMIN && PlayerUtils.getRank(player.getKey().getName()) != Rank.ADMIN) continue;
				
				if (!Main.proxy.getProxy().getPlayer(event.getSender().toString()).getServer().getInfo().getName().equalsIgnoreCase(player.getValue()) && !player.getValue().equalsIgnoreCase("all")) continue;
				player.getKey().sendMessage(new ComponentBuilder(event.getSender().toString()).color(DARK_BLUE).append(": ").color(DARK_GRAY).append(event.getMessage()).color(GRAY).italic(true).create());
				
			}
			
			ProxiedPlayer player = Main.proxy.getProxy().getPlayer(event.getSender().toString());
			String message = event.getMessage();
			String command = message.split(" ")[0];
			command = command.substring(1).toLowerCase();
			
			if (PlayerUtils.getRank(player.getName()) != Rank.ADMIN) {
				
				switch (command) {
				case "achievement":
				case "ban-ip": 
				case "banlist": 
				case "blockdata":
				case "clone": 
				case "debug": 
				case "deop":
				case "defaultgamemode":
				case "difficulty":
				case "enchant":
				case "effect":
				case "entitydata":
				case "execute":
				case "fill":
				case "filter":
				case "gamerule":
				case "icanhasbukkit":
				case "kill":
				case "me":
				case "op":
				case "packet":
				case "packet_filter":
				case "pardon":
				case "pardon-ip":
				case "particle":
				case "pl":
				case "playsound":
				case "plugins":
				case "pregenerator":
				case "preg":
				case "protocol":
				case "publish":
				case "reload":
				case "replaceitem":
				case "restart":
				case "rl":
				case "save-all":
				case "save-off":
				case "save-on":
				case "say":
				case "scoreboard":
				case "setblock":
				case "setidletimeout":
				case "spawnpoint":
				case "spreadplayer":
				case "stats":
				case "stop":
				case "summon":
				case "tellraw":
				case "time":
				case "timings":
				case "testfor":
				case "testforblock":
				case "testforblocks":
				case "title":
				case "toggledownfall":
				case "trigger":
				case "ver":
				case "version":
				case "viaversion":
				case "viaver":
				case "weather":
				case "worldborder":
				case "xp": 
				case "?":
					player.sendMessage(new ComponentBuilder("Unknown command.").create());
					event.setCancelled(true);
					return;
				default:
					break;
					
				}
			
			
				if (command.startsWith("bukkit:") || command.startsWith("minecraft:") || command.startsWith("protocollib:") || command.startsWith("spigot:") || command.startsWith("uhc:") || command.startsWith("pregenerator:") || command.startsWith("viaversion:") || command.startsWith("commandshub:") || command.startsWith("commandsarena:") || command.startsWith("commandsparkour:")) {
					
					player.sendMessage(new ComponentBuilder("Unknown command.").create());
					event.setCancelled(true);
					return;
					
				}
				
			}
			
		}
		
		if (event.getSender() instanceof ProxiedPlayer) {
			
			if (event.isCommand() && !event.getMessage().substring(1).startsWith("msg") && !event.getMessage().substring(1).startsWith("tell") && !event.getMessage().substring(1).startsWith("w") && !event.getMessage().substring(1).startsWith("reply") && !event.getMessage().substring(1).startsWith("r")) return;
			
			if ((ChatCommand.muted.containsKey("all") && ChatCommand.muted.get("all")) || (ChatCommand.muted.containsKey(((ProxiedPlayer) event.getSender()).getServer().getInfo().getName()) && ChatCommand.muted.get(((ProxiedPlayer) event.getSender()).getServer().getInfo().getName())) && PlayerUtils.getRank(((ProxiedPlayer) event.getSender()).getName()) != Rank.ADMIN && PlayerUtils.getRank(((ProxiedPlayer) event.getSender()).getName()) != Rank.HOST && PlayerUtils.getRank(((ProxiedPlayer) event.getSender()).getName()) != Rank.STAFF && PlayerUtils.getRank(((ProxiedPlayer) event.getSender()).getName()) != Rank.MOD) {
				
				event.setCancelled(true);
				((ProxiedPlayer) event.getSender()).sendMessage(Main.prefix().append("The chat is currently muted.").color(GRAY).create());
				return;
				
			}
			
			int muteId = PlayerUtils.isMuted(((ProxiedPlayer) event.getSender()).getUniqueId());
			
			if (muteId <= 0) return;
			
			event.setCancelled(true);
			
			long duration = (PlayerUtils.getMuteDuration(muteId) - (new Date().getTime() - PlayerUtils.getMuteDate(muteId)));
			long days = TimeUnit.MILLISECONDS.toDays(duration); 
			long hours = TimeUnit.MILLISECONDS.toHours(duration) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(duration)); 
			long minutes = TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration));
			long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration));
			
			((ProxiedPlayer) event.getSender()).sendMessage(Main.prefix().append("Your are muted for:").color(GRAY).create());
			ComponentBuilder text = Main.prefix();
			if (days > 0) text.append(String.valueOf(days)).color(GREEN).append(" days, ").color(GRAY);
			if (hours > 0) text.append(String.valueOf(hours)).color(GREEN).append(" hours, ").color(GRAY);
			if (minutes > 0) text.append(String.valueOf(minutes)).color(GREEN).append(" minutes, ").color(GRAY);
			text.append(String.valueOf(seconds)).color(GREEN).append(" seconds.").color(GRAY);
			((ProxiedPlayer) event.getSender()).sendMessage(text.create());
			
		}
		
	}

	@EventHandler
	public void onConnect (ServerConnectedEvent event) {
		
		if (event.getPlayer().hasPermission("proxy.cmdspy")) Main.cmdspy.put(event.getPlayer(), event.getServer().getInfo().getName());
		if (event.getPlayer().hasPermission("proxy.socialspy")) Main.socialspy.put(event.getPlayer(), event.getServer().getInfo().getName());
		
		Map<String, Integer> alts = PlayerUtils.getAlts(event.getPlayer().getUniqueId());
		
		if (alts.isEmpty()) return;
		
		ComponentBuilder text = Main.prefix().append(event.getPlayer().getName()).color(GREEN);
		text.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/info " + event.getPlayer().getName()));
		text.append(" is possibly an alt of ").retain(FormatRetention.NONE).color(GRAY);
		
		for (int i = 0; i < alts.keySet().size(); i++) {
			
			text.append((String) alts.keySet().toArray()[i]).color(GREEN);
			text.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/info " + alts.keySet().toArray()[i]));
			
			if (alts.keySet().size() > 1 && i < (alts.keySet().size() - 1)) text.append(", ").retain(FormatRetention.NONE).color(GRAY);
			
		}
		
		text.append(".").retain(FormatRetention.NONE).color(GRAY);
		
		for (ProxiedPlayer player : event.getServer().getInfo().getPlayers()) {
			
			if (!player.hasPermission("proxy.alerts")) continue;
	
			player.sendMessage(text.create());
			
		}
		
		if (Main.newPseudo.containsKey(event.getPlayer().getUniqueId())) {
			
			ComponentBuilder newPseudo = Main.prefix().append("'").color(GRAY).append(Main.newPseudo.get(event.getPlayer().getUniqueId())).color(GOLD).append("' changed his name for '").color(GRAY).append(event.getPlayer().getName()).color(GOLD).append("'.").color(GRAY);
			
			for (ProxiedPlayer player : event.getServer().getInfo().getPlayers()) {
				
				player.sendMessage(newPseudo.create());
				
			}
			
		}
		
	}
	
	@EventHandler
	public void onLogin (LoginEvent event) {		
		
		if (!PlayerUtils.exist(event.getConnection().getUniqueId())) return;
		
		PlayerUtils.preJoinUpdatePlayer(event.getConnection().getName(), event.getConnection().getUniqueId(), event.getConnection().getAddress().getAddress().getHostAddress());
		
		int banId = PlayerUtils.isBanned(event.getConnection().getUniqueId());
		
		if (banId > 0) {
			
			event.setCancelled(true);
			String expire = "";
			
			if (PlayerUtils.getBanDuration(banId) > 0) {
				
				SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy HH:mm");
				format.setTimeZone(TimeZone.getTimeZone("UTC"));
				expire = "\n§6Expire §8⫸ §7" + format.format(PlayerUtils.getBanDate(banId) + PlayerUtils.getBanDuration(banId)) + " UTC";
				
			}
			
			event.setCancelReason("§8⫸ §7You are §6banned §7from §aCommandsPVP §8⫷\n\n§6Reason §8⫸ §7" + BanCommand.Reasons.valueOf(PlayerUtils.getBanReason(banId).toUpperCase()).getName() + expire + "\n\n§8⫸ §7To appeal, contact us on Twitter §b@CommandsPVP §8⫷");
			
			for (ProxiedPlayer player : Main.proxy.getProxy().getPlayers()) {
				
				if (!player.hasPermission("proxy.alerts")) continue;
				
				player.sendMessage(Main.prefix().append("'").color(GRAY).append(event.getConnection().getName()).color(GOLD).append("' can't connect due to a ban ").color(GRAY).append("(").color(DARK_GRAY).append("Reason(s): ").color(GRAY).append(BanCommand.Reasons.valueOf(PlayerUtils.getBanReason(banId).toUpperCase()).getName()).color(GOLD).append(")").color(DARK_GRAY).create());
				
			}
			
			return;
			
		}
		
		List<String> accounts = PlayerUtils.isBanned(event.getConnection().getAddress().getAddress().getHostAddress());
		
		if (!accounts.isEmpty()) {
			
			event.setCancelled(true);
			String account = "";
			
			for (String name : accounts) {
				
				account += name + ", ";
				
			}
			
			event.setCancelReason("§8⫸ §7You are §6IP banned §7from §aCommandsPVP §8⫷\n\n§6Due to account(s) §8⫸ §7" + account.subSequence(0, (account.length() - 2)) + "\n\n§8⫸ §7To appeal, contact us on Twitter §b@CommandsPVP §8⫷");
			
			for (ProxiedPlayer player : Main.proxy.getProxy().getPlayers()) {
				
				if (!player.hasPermission("proxy.alerts")) continue;
				
				player.sendMessage(Main.prefix().append("'").color(GRAY).append(event.getConnection().getName()).color(GOLD).append("' can't connect due to a IP ban ").color(GRAY).append("(").color(DARK_GRAY).append("Account(s): ").color(GRAY).append(account.subSequence(0, (account.length() - 2)).toString()).color(GOLD).append(")").color(DARK_GRAY).create());
				
			}
			
			return;
			
		}
		
	}
	
}
