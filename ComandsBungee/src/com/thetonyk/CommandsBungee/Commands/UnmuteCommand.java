package com.thetonyk.CommandsBungee.Commands;

import java.util.ArrayList;
import java.util.List;

import com.thetonyk.CommandsBungee.Main;
import com.thetonyk.CommandsBungee.Utils.PlayerUtils;

import static net.md_5.bungee.api.ChatColor.*;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class UnmuteCommand extends Command implements TabExecutor {
	
	public UnmuteCommand() {
		
		super ("unmute");
		
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		if (!sender.hasPermission("proxy.unmute")) {
			
			sender.sendMessage(new ComponentBuilder("Unknown command.").create());
    		return;
    		
		}
		
		if (args.length < 1) {
			
			sender.sendMessage(Main.prefix().append("Use ").color(GRAY).append("/info <player>").color(GOLD).create());
			return;
			
		}
		
		if (Integer.parseInt(args[0]) == 0) {
			
			sender.sendMessage(Main.prefix().append("Error to find the mute entry.").color(GRAY).create());
			return;
			
		}
		
		PlayerUtils.cancelMute(Integer.parseInt(args[0]), PlayerUtils.getId(sender.getName()));
		sender.sendMessage(Main.prefix().append("The mute has been canceled").color(GRAY).create());
		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		
		return new ArrayList<String>();
		
	}

}
