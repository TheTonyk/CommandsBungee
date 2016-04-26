package com.thetonyk.CommandsBungee.Commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.thetonyk.CommandsBungee.Main;
import com.thetonyk.CommandsBungee.Utils.PlayerUtils;

import net.md_5.bungee.api.ChatColor;
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
			
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7Usage: /msg <player> <message>")).create());
			return;
			
		}
		
		if (cooldown.contains(sender.getName())) return;
		
		if (Main.proxy.getProxy().getPlayer(args[0]) == null) {
			
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7The player '§6" + args[0] + "§7' is not online.")).create());
			return;
			
		}
		
		if (sender.getName().equalsIgnoreCase(args[0])) {
			
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7You cannot send messages to yourself.")).create());
			return;
			
		}
		
		if (PlayerUtils.exist(Main.proxy.getProxy().getPlayer(sender.getName())) && PlayerUtils.getPrivatesState(Main.proxy.getProxy().getPlayer(sender.getName())) == 0) {
			
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7Your privates messages are disabled.")).create());
			return;
			
		}
		
		if (PlayerUtils.exist(Main.proxy.getProxy().getPlayer(args[0])) && PlayerUtils.getPrivatesState(Main.proxy.getProxy().getPlayer(args[0])) == 0) {
			
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7You can't send messages to '§6" + Main.proxy.getProxy().getPlayer(args[0]).getName() + "§7'.")).create());
			return;
			
		}
		
		StringBuilder message = new StringBuilder();
		
		for (int i = 1; i < args.length; i++) {
			
			message.append(args[i] + " ");
			
		}
		
		sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§6Private §8| §7" + Main.proxy.getProxy().getPlayer(args[0]).getName() + " §c⫷ §f" + message)).create());
		
		ComponentBuilder text = new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§6Private §8| §7" + sender.getName() + " §a⫸ §f"));
		text.append(ChatColor.translateAlternateColorCodes('§', message.toString()));
		text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§7Reply to §a" + sender.getName())).create()));
		text.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + sender.getName() + " "));
		Main.proxy.getProxy().getPlayer(args[0]).sendMessage(text.create());
		
		lastMsg.put(Main.proxy.getProxy().getPlayer(args[0]).getName(), sender.getName());
		
		cooldown.add(sender.getName());
		
		Main.proxy.getProxy().getScheduler().schedule(Main.proxy, new Runnable() {
			
			public void run() {
				
				cooldown.remove(sender.getName());
				
			}
			
		}, 2, TimeUnit.SECONDS);
		
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
