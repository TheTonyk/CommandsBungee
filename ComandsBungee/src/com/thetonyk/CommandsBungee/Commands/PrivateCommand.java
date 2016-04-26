package com.thetonyk.CommandsBungee.Commands;

import java.util.ArrayList;
import java.util.List;

import com.thetonyk.CommandsBungee.Main;

import net.md_5.bungee.api.ChatColor;
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
			
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "Unknown command.")).create());
    		return;
    		
		}
		
		if (args.length < 1) {
			
			sender.sendMessage(new ComponentBuilder("§a§lGlobal §8⫸ §7Usage: /private <message>").create());
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
			
			player.sendMessage(new ComponentBuilder("§6StaffChat §8| §7" + Main.proxy.getProxy().getPlayer(sender.getName()).getName() + " §8⫸ §f" + message).create());
			players++;
			
		}
		
		if (players < 1) {
			
			sender.sendMessage(new ComponentBuilder("§a§lGlobal §8⫸ §7There are no others staff online.").create());
			return;
			
		}
		
		sender.sendMessage(new ComponentBuilder("§6StaffChat §8| §7" + Main.proxy.getProxy().getPlayer(sender.getName()).getName() + " §8⫸ §f" + message).create());
		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		
		List<String> complete = new ArrayList<String>();
		
		return complete;
		
	}
	
}
