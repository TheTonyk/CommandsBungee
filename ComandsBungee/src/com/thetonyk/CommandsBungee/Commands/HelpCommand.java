package com.thetonyk.CommandsBungee.Commands;

import static net.md_5.bungee.api.ChatColor.*;

import com.thetonyk.CommandsBungee.Main;

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
		
		sender.sendMessage(Main.prefix().append("Help informations:").color(GRAY).create());
		sender.sendMessage(new ComponentBuilder("⫸ ").color(DARK_GRAY).append("Contact a staff member if you need help.").color(GRAY).create());
		sender.sendMessage(new ComponentBuilder("⫸ ").color(DARK_GRAY).append("Minecraft ").color(GRAY).append("1.8.8 ").color(GOLD).append("or ").color(GRAY).append("1.8.9 ").color(GOLD).append("recommended.").color(GRAY).create());
		
		ComponentBuilder text = new ComponentBuilder("⫸ ").color(DARK_GRAY).append("Twitter of the server: ").color(GRAY).append("@CommandsPVP").color(AQUA).italic(true);
		text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Visit our ").color(GRAY).append("Twitter").color(GREEN).append(".").color(GRAY).create()));
		text.event(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://twitter.com/CommandsPVP"));
		text.append(".").color(GRAY);
		sender.sendMessage(text.create());
    	
    }

}
