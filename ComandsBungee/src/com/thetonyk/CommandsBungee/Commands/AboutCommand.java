package com.thetonyk.CommandsBungee.Commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.plugin.Command;

public class AboutCommand extends Command {
	
	public AboutCommand() {
		
		super("about");
		
	}
	
	@Override
    public void execute(CommandSender sender, String[] args) {
		
		ComponentBuilder text = new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7Plugins by TheTonyk for CommandsPVP."));
		sender.sendMessage(text.create());
		
		text = new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7Twitter: "));
		text.append(ChatColor.translateAlternateColorCodes('§', "§b@TheTonyk"));
		text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§7Visit the Twitter of §aTheTonyk§7.")).create()));
		text.event(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://twitter.com/TheTonyk"));
		sender.sendMessage(text.create());
		
		
		

    	return;
    	
    }

}
