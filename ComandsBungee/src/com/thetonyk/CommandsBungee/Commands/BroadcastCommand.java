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

public class BroadcastCommand extends Command implements TabExecutor {
	
	public BroadcastCommand() {
		
		super("broadcast", "", "bc");
		
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {

		if (!sender.hasPermission("proxy.broadcast")) {
			
			sender.sendMessage(new ComponentBuilder("Unknown command.").create());
    		return;
    		
		}
				
		if (args.length < 1) {
			
			sender.sendMessage(Main.prefix().append("Usage: /broadcast <message>").color(GRAY).create());
			return;
			
		}
			
		StringBuilder message = new StringBuilder();
		
		for (int i = 0; i < args.length; i++) {
			
			message.append(args[i] + " ");
			
		}
		
		for (ProxiedPlayer player : Main.proxy.getProxy().getPlayer(sender.getName()).getServer().getInfo().getPlayers()) {
			
			player.sendMessage(new ComponentBuilder("⫸ ").color(DARK_GRAY).append(message.toString()).color(AQUA).bold(true).create());
			
		}
		
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		
		return new ArrayList<String>();
		
	}

}
