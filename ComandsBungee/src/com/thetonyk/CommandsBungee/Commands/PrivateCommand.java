package com.thetonyk.CommandsBungee.Commands;

import java.util.ArrayList;
import java.util.List;

import com.thetonyk.CommandsBungee.Main;

import static net.md_5.bungee.api.ChatColor.*;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class PrivateCommand extends Command implements TabExecutor {

	public PrivateCommand() {
		
		super ("private", "", "p");
		
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		if (!sender.hasPermission("proxy.private")) {
			
			sender.sendMessage(new ComponentBuilder("Unknown command.").create());
    		return;
    		
		}
		
		if (args.length < 1) {
			
			sender.sendMessage(Main.prefix().append("Usage: /private <message>").color(GRAY).create());
			return;
			
		}
		
		StringBuilder message = new StringBuilder();
		
		for (int i = 0; i < args.length; i++) {
			
			message.append(args[i] + " ");
			
		}
		
		int players = 0;
		
		for (ProxiedPlayer player : Main.proxy.getProxy().getPlayers()) {
			
			if (player.getName() == sender.getName()) {
				
				continue;
				
			}
			
			if (!player.hasPermission("proxy.private")) {
				
				continue;
				
			}
			
			player.sendMessage(new ComponentBuilder("StaffChat ").color(GOLD).append("| ").color(DARK_GRAY).append(Main.proxy.getProxy().getPlayer(sender.getName()).getName()).color(GRAY).append(" ⫸ ").color(DARK_GRAY).append(message.toString()).color(WHITE).create());
			players++;
			
		}
		
		if (players < 1) {
			
			sender.sendMessage(Main.prefix().append("There are no others staff online.").color(GRAY).create());
			return;
			
		}
		
		sender.sendMessage(new ComponentBuilder("StaffChat ").color(GOLD).append("| ").color(DARK_GRAY).append(Main.proxy.getProxy().getPlayer(sender.getName()).getName()).color(GRAY).append(" ⫸ ").color(DARK_GRAY).append(message.toString()).color(WHITE).create());
		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		
		List<String> complete = new ArrayList<String>();
		
		return complete;
		
	}
	
}
