package com.thetonyk.CommandsBungee.Listener;

import java.util.UUID;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.thetonyk.CommandsBungee.Main;
import com.thetonyk.CommandsBungee.Utils.PermissionsUtils;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class MessengerListener implements Listener {

	@EventHandler
    public void receivePluginMessage (PluginMessageEvent event) {
		
		if (!event.getTag().equalsIgnoreCase("CommandsBungee")) return;
		
		ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
		String subchannel = in.readUTF();
		
		if (!subchannel.equalsIgnoreCase("updatePermissions")) return;
		
		ProxiedPlayer player = Main.proxy.getProxy().getPlayer(UUID.fromString(in.readUTF()));
		
		PermissionsUtils.clearPermissions(player);
		PermissionsUtils.setPermissions(player);
		
	}
	
}
