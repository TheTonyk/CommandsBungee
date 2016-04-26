package com.thetonyk.CommandsBungee.Commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.plugin.Command;

public class HelpCommand extends Command {
	
	public HelpCommand() {
		
		super("help");
		
	}
	
	@Override
    public void execute(CommandSender sender, String[] args) {
		
		sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§a§lGlobal §8⫸ §7Help informations:")).create());
		sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§8⫸ §7Contact a staff member if you need help.")).create());
		sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§8⫸ §7Minecraft §61.8.8 §7or §61.8.9 §7recommended.")).create());
		
		ComponentBuilder text = new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§8⫸ §7Twitter of the server: "));
		text.append(ChatColor.translateAlternateColorCodes('§', "§b@CommandsPVP"));
		text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('§', "§7Visit our §aTwitter§7.")).create()));
		text.event(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://twitter.com/CommandsPVP"));
		text.append(ChatColor.translateAlternateColorCodes('§', "§7."));
		
		sender.sendMessage(text.create());
    	
    }

}
