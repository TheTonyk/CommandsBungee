package com.thetonyk.CommandsBungee;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.thetonyk.CommandsBungee.Commands.AboutCommand;
import com.thetonyk.CommandsBungee.Commands.BanCommand;
import com.thetonyk.CommandsBungee.Commands.BroadcastCommand;
import com.thetonyk.CommandsBungee.Commands.ChatCommand;
import com.thetonyk.CommandsBungee.Commands.GBroadcastCommand;
import com.thetonyk.CommandsBungee.Commands.CmdspyCommand;
import com.thetonyk.CommandsBungee.Commands.HelpCommand;
import com.thetonyk.CommandsBungee.Commands.HubCommand;
import com.thetonyk.CommandsBungee.Commands.IgnoreCommand;
import com.thetonyk.CommandsBungee.Commands.InfoCommand;
import com.thetonyk.CommandsBungee.Commands.KickCommand;
import com.thetonyk.CommandsBungee.Commands.MsgCommand;
import com.thetonyk.CommandsBungee.Commands.MuteCommand;
import com.thetonyk.CommandsBungee.Commands.OrganizeCommand;
import com.thetonyk.CommandsBungee.Commands.PingCommand;
import com.thetonyk.CommandsBungee.Commands.ReplyCommand;
import com.thetonyk.CommandsBungee.Commands.SocialspyCommand;
import com.thetonyk.CommandsBungee.Commands.PrivateCommand;
import com.thetonyk.CommandsBungee.Commands.UnbanCommand;
import com.thetonyk.CommandsBungee.Commands.UnmuteCommand;
import com.thetonyk.CommandsBungee.Listener.MessengerListener;
import com.thetonyk.CommandsBungee.Listener.PlayerListener;

import static net.md_5.bungee.api.ChatColor.*;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

public class Main extends Plugin {
	
	public static Main proxy;
	
	public static Map<ProxiedPlayer, String> cmdspy = new HashMap<ProxiedPlayer, String>();
	public static Map<ProxiedPlayer, String> socialspy = new HashMap<ProxiedPlayer, String>();
	public static Map<UUID, String> newPseudo = new HashMap<UUID, String>();
	
	@Override
	public void onEnable() {
		
		proxy = this;
			
		getLogger().info("CommandsPVP BungeeCord Plugin has been enabled.");
		getLogger().info("Plugin by TheTonyk for CommandsPVP");
		
		this.getProxy().getPluginManager().registerListener(this, new PlayerListener());
		this.getProxy().getPluginManager().registerCommand(this, new MsgCommand());
		this.getProxy().getPluginManager().registerCommand(this, new ReplyCommand());
		this.getProxy().getPluginManager().registerCommand(this, new AboutCommand());
		this.getProxy().getPluginManager().registerCommand(this, new GBroadcastCommand());
		this.getProxy().getPluginManager().registerCommand(this, new PingCommand());
		this.getProxy().getPluginManager().registerCommand(this, new InfoCommand());
		this.getProxy().getPluginManager().registerCommand(this, new CmdspyCommand());
		this.getProxy().getPluginManager().registerCommand(this, new BanCommand());
		this.getProxy().getPluginManager().registerCommand(this, new UnbanCommand());
		this.getProxy().getPluginManager().registerCommand(this, new KickCommand());
		this.getProxy().getPluginManager().registerCommand(this, new MuteCommand());
		this.getProxy().getPluginManager().registerCommand(this, new UnmuteCommand());
		this.getProxy().getPluginManager().registerCommand(this, new PrivateCommand());
		this.getProxy().getPluginManager().registerCommand(this, new OrganizeCommand());
		this.getProxy().getPluginManager().registerCommand(this, new HubCommand());
		this.getProxy().getPluginManager().registerCommand(this, new HelpCommand());
		this.getProxy().getPluginManager().registerCommand(this, new BroadcastCommand());
		this.getProxy().getPluginManager().registerCommand(this, new IgnoreCommand());
		this.getProxy().getPluginManager().registerCommand(this, new ChatCommand());
		this.getProxy().getPluginManager().registerCommand(this, new SocialspyCommand());
		
		this.getProxy().registerChannel("CommandsBungee");
		this.getProxy().getPluginManager().registerListener(this, new MessengerListener());
		
	}
	
	@Override
	public void onDisable() {
		
		getLogger().info("CommandsPVP BungeeCord Plugin has been disabled.");
		
		proxy = null;
		
	}
	
	public static ComponentBuilder prefix() {
		
		return new ComponentBuilder("Global ").color(GREEN).bold(true).append("⫸ ").color(DARK_GRAY).bold(false);
		
	}

}
