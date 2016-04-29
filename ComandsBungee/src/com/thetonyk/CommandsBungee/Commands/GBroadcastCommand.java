package com.thetonyk.CommandsBungee.Commands;

import java.util.ArrayList;
import java.util.List;

import com.thetonyk.CommandsBungee.Main;

import static net.md_5.bungee.api.ChatColor.*;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class GBroadcastCommand extends Command implements TabExecutor {
	
	public GBroadcastCommand() {
		
		super("gbroadcast", "", "gbc");
		
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {

		if (!sender.hasPermission("proxy.broadcast")) {
			
			sender.sendMessage(new ComponentBuilder("Unknown command.").create());
    		return;
    		
		}
				
		if (args.length < 1) {
			
			sender.sendMessage(Main.prefix().append("Usage: /gbroadcast <message>").color(GRAY).create());
			return;
			
		}
			
		StringBuilder message = new StringBuilder();
		
		for (int i = 0; i < args.length; i++) {
			
			message.append(args[i] + " ");
			
		}
		
		Main.proxy.getProxy().broadcast(new ComponentBuilder(" ").create());
		Main.proxy.getProxy().broadcast(new ComponentBuilder(" ").create());
		Main.proxy.getProxy().broadcast(Main.prefix().append(message.toString()).color(AQUA).bold(true).create());
		Main.proxy.getProxy().broadcast(new ComponentBuilder(" ").create());
		Main.proxy.getProxy().broadcast(new ComponentBuilder(" ").create());
		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		
		return new ArrayList<String>();
		
	}

}
