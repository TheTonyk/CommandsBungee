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

public class OrganizeCommand extends Command implements TabExecutor {

	public OrganizeCommand() {
		
		super ("organize", "", "o");
		
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		if (!sender.hasPermission("proxy.organize")) {
			
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "Unknown command.")).create());
    		return;
    		
		}
		
		if (args.length < 1) {
			
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7Usage: /organize <message>")).create());
			return;
			
		}
		
		StringBuilder message = new StringBuilder();
		
		for (int i = 0; i < args.length; i++) {
			
			message.append(args[i] + " ");
			
		}
		
		int players = 0;
		
		for (ProxiedPlayer player : Main.proxy.getProxy().getPlayers()) {
			
			if (player.getName() == sender.getName() || !player.hasPermission("proxy.organize")) continue;
			
			player.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§6OrganizeChat §8| §7" + Main.proxy.getProxy().getPlayer(sender.getName()).getName() + " §8⫸ §f" + message)).create());
			players++;
			
		}
		
		if (players < 1) {
			
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7There are no others Host/Staff online.")).create());
			return;
			
		}
		
		sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§6OrganizeChat §8| §7" + Main.proxy.getProxy().getPlayer(sender.getName()).getName() + " §8⫸ §f" + message)).create());
		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		
		return new ArrayList<String>();
		
	}
	
}
