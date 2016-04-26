package com.thetonyk.CommandsBungee.Commands;

import com.thetonyk.CommandsBungee.Main;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class HubCommand extends Command {
	
	public HubCommand() {
		
		super("hub");
		
	}
	
	@Override
    public void execute(CommandSender sender, String[] args) {
		
		Main.proxy.getProxy().getPlayer(sender.getName()).connect(Main.proxy.getProxy().getServerInfo("hub"));
    	
    }

}
