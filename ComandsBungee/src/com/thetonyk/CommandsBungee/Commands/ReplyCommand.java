package com.thetonyk.CommandsBungee.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.thetonyk.CommandsBungee.Main;
import com.thetonyk.CommandsBungee.Utils.PlayerUtils;

import static net.md_5.bungee.api.ChatColor.*;
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
			
			sender.sendMessage(Main.prefix().append("Usage: /reply <message>").color(GRAY).create());
			return;
			
		}
		
		if (cooldown.contains(sender.getName())) return;
		
		if (!MsgCommand.lastMsg.containsKey(sender.getName())) {
			
			sender.sendMessage(Main.prefix().append("You have nobody to reply to.").color(GRAY).create());
			return;
			
		}
		
		if (Main.proxy.getProxy().getPlayer(MsgCommand.lastMsg.get(sender.getName())) == null) {
			
			sender.sendMessage(Main.prefix().append("The player '").color(GRAY).append(MsgCommand.lastMsg.get(sender.getName())).color(GOLD).append("' is not online.").color(GRAY).create());
			return;
			
		}
		
		if (PlayerUtils.getPrivatesState(Main.proxy.getProxy().getPlayer(sender.getName())) == 0) {
			
			sender.sendMessage(Main.prefix().append("Your privates messages are disabled.").color(GRAY).create());
			return;
			
		}
		
		if (PlayerUtils.getPrivatesState(Main.proxy.getProxy().getPlayer(MsgCommand.lastMsg.get(sender.getName()))) == 0) {
			
			sender.sendMessage(Main.prefix().append("You can't send messages to '").color(GRAY).append(Main.proxy.getProxy().getPlayer(MsgCommand.lastMsg.get(sender.getName())).getName()).color(GOLD).append("'.").color(GRAY).create());
			return;
			
		}
		
		StringBuilder message = new StringBuilder();
		
		for (int i = 0; i < args.length; i++) {
			
			message.append(args[i] + " ");
			
		}
		
		sender.sendMessage(new ComponentBuilder("Private ").color(GOLD).append("| ").color(DARK_GRAY).append(Main.proxy.getProxy().getPlayer(MsgCommand.lastMsg.get(sender.getName())).getName()).color(GRAY).append(" ⫷ ").color(RED).append(message.toString()).color(WHITE).create());
		
		cooldown.add(sender.getName());
		
		Main.proxy.getProxy().getScheduler().schedule(Main.proxy, new Runnable() {
			
			public void run() {
				
				cooldown.remove(sender.getName());
				
			}
			
		}, 2, TimeUnit.SECONDS);
		
		if (PlayerUtils.getIgnoredPlayers(Main.proxy.getProxy().getPlayer(MsgCommand.lastMsg.get(sender.getName())).getUniqueId()) != null && PlayerUtils.getIgnoredPlayers(Main.proxy.getProxy().getPlayer(MsgCommand.lastMsg.get(sender.getName())).getUniqueId()).contains(Main.proxy.getProxy().getPlayer(sender.getName()).getUniqueId())) return;
		
		MsgCommand.lastMsg.put(Main.proxy.getProxy().getPlayer(MsgCommand.lastMsg.get(sender.getName())).getName(), sender.getName());
		
		ComponentBuilder text = new ComponentBuilder("Private ").color(GOLD).append("| ").color(DARK_GRAY).append(sender.getName()).color(GRAY).append(" ⫸ ").color(GREEN).append(message.toString()).color(WHITE);
		text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Reply to ").color(GRAY).append(sender.getName()).color(GREEN).append(".").color(GRAY).create()));
		text.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + sender.getName() + " "));
		Main.proxy.getProxy().getPlayer(MsgCommand.lastMsg.get(sender.getName())).sendMessage(text.create());
		return;
		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		
		return new ArrayList<String>();
		
	}

}
