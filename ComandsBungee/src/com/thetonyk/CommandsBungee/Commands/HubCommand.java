package com.thetonyk.CommandsBungee.Commands;

import com.thetonyk.CommandsBungee.Main;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class HubCommand extends Command {
	
	public HubCommand() {
		
		super("hub", "", "lobby");
		
	}
	
	@Override
    public void execute(CommandSender sender, String[] args) {
		
		if (Main.proxy.getProxy().getPlayer(sender.getName()).getServer().getInfo().getName().equalsIgnoreCase("hub")) sender.sendMessage(Main.prefix().append("You are already in the hub.").create());
		
		Main.proxy.getProxy().getPlayer(sender.getName()).connect(Main.proxy.getProxy().getServerInfo("hub"));
    	
    }

}
