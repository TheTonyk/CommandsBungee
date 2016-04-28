package com.thetonyk.CommandsBungee.Commands;

import static net.md_5.bungee.api.ChatColor.*;

import com.thetonyk.CommandsBungee.Main;

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
		
		ComponentBuilder text = Main.prefix().append("Plugins by TheTonyk for CommandsPVP.").color(GRAY);
		sender.sendMessage(text.create());
		
		text = Main.prefix().append("Twitter: ").color(GRAY).append("@TheTonyk").color(AQUA).italic(true);
		text.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Visit the Twitter of ").color(GRAY).append("TheTonyk").color(GREEN).append(".").color(GRAY).create()));
		text.event(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://twitter.com/TheTonyk"));
		sender.sendMessage(text.create());

    	return;
    	
    }

}
