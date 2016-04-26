package com.thetonyk.CommandsBungee.Commands;

import java.util.ArrayList;
import java.util.List;

import com.thetonyk.CommandsBungee.Utils.PlayerUtils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class UnbanCommand extends Command implements TabExecutor {
	
	public UnbanCommand() {
		
		super ("unban");
		
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		if (!sender.hasPermission("proxy.unban")) {
			
			sender.sendMessage(new ComponentBuilder("Unknown command.").create());
    		return;
    		
		}
		
		if (args.length < 1) {
			
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7Use §6/info <player>")).create());
			return;
			
		}
		
		if (Integer.parseInt(args[0]) == 0) {
			
			sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7Error to find the ban entry.")).create());
			return;
			
		}
		
		PlayerUtils.cancelBan(Integer.parseInt(args[0]), PlayerUtils.getId(sender.getName()));
		sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7The ban has been canceled")).create());
		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		
		return new ArrayList<String>();
		
	}

}
