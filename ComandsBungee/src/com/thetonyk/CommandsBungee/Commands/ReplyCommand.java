package com.thetonyk.CommandsBungee.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.thetonyk.CommandsBungee.Main;
import com.thetonyk.CommandsBungee.Utils.PlayerUtils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class ReplyCommand extends Command implements TabExecutor {
	
	List<String> cooldown = new ArrayList<String>();
	
	public ReplyCommand() {
		
		super("reply", "", "r");
		
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		if (args.length < 1) {
			
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7Usage: /reply <message>")).create());
			return;
			
		}
		
		if (cooldown.contains(sender.getName())) return;
		
		if (!MsgCommand.lastMsg.containsKey(sender.getName())) {
			
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7You have nobody to reply to.")).create());
			return;
			
		}
		
		if (Main.proxy.getProxy().getPlayer(MsgCommand.lastMsg.get(sender.getName())) == null) {
			
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7The player '§6" + MsgCommand.lastMsg.get(sender.getName()) + "§7' is not online.")).create());
			return;
			
		}
		
		if (PlayerUtils.exist(Main.proxy.getProxy().getPlayer(sender.getName())) && PlayerUtils.getPrivatesState(Main.proxy.getProxy().getPlayer(sender.getName())) == 0) {
			
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7Your privates messages are disabled.")).create());
			return;
			
		}
		
		if (PlayerUtils.exist(Main.proxy.getProxy().getPlayer(MsgCommand.lastMsg.get(sender.getName()))) && PlayerUtils.getPrivatesState(Main.proxy.getProxy().getPlayer(MsgCommand.lastMsg.get(sender.getName()))) == 0) {
			
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7You can't send messages to '§6" + Main.proxy.getProxy().getPlayer(MsgCommand.lastMsg.get(sender.getName())).getName() + "§7'.")).create());
			return;
			
		}
		
		StringBuilder message = new StringBuilder();
		
		for (int i = 0; i < args.length; i++) {
			
			message.append(args[i] + " ");
			
		}
		
		sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§6Private §8| §7" + Main.proxy.getProxy().getPlayer(MsgCommand.lastMsg.get(sender.getName())).getName() + " §c⫷ §f" + message)).create());
		
		ComponentBuilder text = new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§6Private §8| §7" + sender.getName() + " §a⫸ §f"));
		text.append(ChatColor.translateAlternateColorCodes('§', message.toString()));
		text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§7Reply to §a" + sender.getName())).create()));
		text.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + sender.getName() + " "));
		Main.proxy.getProxy().getPlayer(MsgCommand.lastMsg.get(sender.getName())).sendMessage(text.create());
		
        MsgCommand.lastMsg.put(Main.proxy.getProxy().getPlayer(MsgCommand.lastMsg.get(sender.getName())).getName(), sender.getName());
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
		
		return new ArrayList<String>();
		
	}

}
