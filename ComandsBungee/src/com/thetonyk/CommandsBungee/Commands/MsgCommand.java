package com.thetonyk.CommandsBungee.Commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import com.thetonyk.CommandsBungee.Main;
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

public class MsgCommand extends Command implements TabExecutor {
	
	public static List<String> cooldown = new ArrayList<String>();
	public static Map<String, String> lastMsg = new HashMap<String, String>();
	
	public MsgCommand() {
		
		super("msg", "", "tell", "w");
		
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		if (args.length < 2) {
			
			sender.sendMessage(Main.prefix().append("Usage: /msg <player> <message>").color(GRAY).create());
			return;
			
		}
		
		if (cooldown.contains(sender.getName())) return;
		
		if (Main.proxy.getProxy().getPlayer(args[0]) == null) {
			
			sender.sendMessage(Main.prefix().append("The player '").color(GRAY).append(args[0]).color(GOLD).append("' is not online.").color(GRAY).create());
			return;
			
		}
		
		if (sender.getName().equalsIgnoreCase(args[0])) {
			
			sender.sendMessage(Main.prefix().append("You cannot send messages to yourself.").color(GRAY).create());
			return;
			
		}
		
		if (PlayerUtils.getPrivatesState(Main.proxy.getProxy().getPlayer(sender.getName())) == 0) {
			
			sender.sendMessage(Main.prefix().append("Your privates messages are disabled.").create());
			return;
			
		}
		
		if (PlayerUtils.getPrivatesState(Main.proxy.getProxy().getPlayer(args[0])) == 0) {
			
			sender.sendMessage(Main.prefix().append("You can't send messages to '").color(GRAY).append(Main.proxy.getProxy().getPlayer(args[0]).getName()).color(GOLD).append("'.").color(GRAY).create());
			return;
			
		}
		
		StringBuilder message = new StringBuilder();
		
		for (int i = 1; i < args.length; i++) {
			
			message.append(args[i] + " ");
			
		}
		
		sender.sendMessage(new ComponentBuilder("Private ").color(GOLD).append("| ").color(DARK_GRAY).append(Main.proxy.getProxy().getPlayer(args[0]).getName()).color(GRAY).append(" ⫷ ").color(RED).append(message.toString()).color(WHITE).create());
		
		cooldown.add(sender.getName());
		
		Main.proxy.getProxy().getScheduler().schedule(Main.proxy, new Runnable() {
			
			public void run() {
				
				cooldown.remove(sender.getName());
				
			}
			
		}, 2, TimeUnit.SECONDS);
		
		if (PlayerUtils.getIgnoredPlayers(Main.proxy.getProxy().getPlayer(args[0]).getUniqueId()) != null && PlayerUtils.getIgnoredPlayers(Main.proxy.getProxy().getPlayer(args[0]).getUniqueId()).contains(Main.proxy.getProxy().getPlayer(sender.getName()).getUniqueId())) return;
		
		lastMsg.put(Main.proxy.getProxy().getPlayer(args[0]).getName(), sender.getName());
		
		for (Entry<ProxiedPlayer, String> player : Main.socialspy.entrySet()) {
			
			if (player.getKey().getName().equalsIgnoreCase(sender.getName()) || player.getKey().getName().equalsIgnoreCase(args[0])) continue;
			
			if (PlayerUtils.getRank(sender.getName()) == Rank.ADMIN && PlayerUtils.getRank(player.getKey().getName()) != Rank.ADMIN) continue;
			
			if (PlayerUtils.getRank(Main.proxy.getProxy().getPlayer(args[0]).getName()) == Rank.ADMIN && PlayerUtils.getRank(player.getKey().getName()) != Rank.ADMIN) continue;
			
			if (!Main.proxy.getProxy().getPlayer(sender.getName()).getServer().getInfo().getName().equalsIgnoreCase(player.getValue()) && !player.getValue().equalsIgnoreCase("all")) continue;
			player.getKey().sendMessage(new ComponentBuilder(sender.getName()).color(DARK_GREEN).append(" ⫸ ").color(DARK_GRAY).append(Main.proxy.getProxy().getPlayer(args[0]).getName()).color(DARK_GREEN).append(": ").color(DARK_GRAY).append(message.toString()).color(GRAY).italic(true).create());
			
		}
		
		ComponentBuilder text = new ComponentBuilder("Private ").color(GOLD).append("| ").color(DARK_GRAY).append(sender.getName()).color(GRAY).append(" ⫸ ").color(GREEN).append(message.toString()).color(WHITE);
		text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Reply to ").color(GRAY).append(sender.getName()).color(GREEN).append(".").color(GRAY).create()));
		text.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + sender.getName() + " "));
		Main.proxy.getProxy().getPlayer(args[0]).sendMessage(text.create());
		return;
		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		
		List<String> players = new ArrayList<String>();
		
		if (args.length == 1) {
			
			for (ProxiedPlayer player : Main.proxy.getProxy().getPlayers()) {
				
				players.add(player.getName());
				
			}
			
		}
		
		List<String> tabCompletions = new ArrayList<String>();
		
		if (args[args.length - 1].isEmpty()) {
			
			for (String type : players) {
				
				tabCompletions.add(type);
				
			}
			
		} else {
			
			for (String type : players) {
				
				if (type.toLowerCase().startsWith(args[args.length - 1].toLowerCase())) {
					
					tabCompletions.add(type);
					
				}
				
			}
			
		}
		
		return tabCompletions;
		
	}

}
