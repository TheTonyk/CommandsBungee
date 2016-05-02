package com.thetonyk.CommandsBungee.Utils;

import com.thetonyk.CommandsBungee.Utils.PlayerUtils.Rank;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PermissionsUtils {
	
	public static void setPermissions(ProxiedPlayer player) {
		
		Rank rank = PlayerUtils.getRank(player.getName());
		
		if (rank == Rank.PLAYER || rank == Rank.WINNER || rank == Rank.FAMOUS || rank == Rank.BUILDER || rank == Rank.ACTIVE_BUILDER || rank == Rank.FRIEND) return;

		player.setPermission("proxy.organize", true);
		
		if (rank == Rank.HOST) return;
		
		player.setPermission("proxy.organize", false);
		player.setPermission("proxy.ban", true);
		player.setPermission("proxy.info", true);
		player.setPermission("proxy.kick", true);
		player.setPermission("proxy.mute", true);
		
		if (rank == Rank.MOD) return;
					
		player.setPermission("proxy.organize", true);
		player.setPermission("proxy.alerts", true);
		player.setPermission("proxy.cmdspy", true);
		player.setPermission("proxy.socialspy", true);
		player.setPermission("proxy.broadcast", true);
		player.setPermission("proxy.private", true);
		player.setPermission("proxy.chat", true);
		
		if (rank == Rank.STAFF) return;
		
		player.setPermission("proxy.unban", true);
		player.setPermission("proxy.unmute", true);
		
	}
	
	public static void clearPermissions(ProxiedPlayer player) {
		
		player.setPermission("proxy.organize", false);		
		player.setPermission("proxy.ban", false);
		player.setPermission("proxy.info", false);
		player.setPermission("proxy.kick", false);
		player.setPermission("proxy.mute", false);
		player.setPermission("proxy.alerts", false);
		player.setPermission("proxy.cmdspy", false);
		player.setPermission("proxy.broadcast", false);
		player.setPermission("proxy.private", false);
		player.setPermission("proxy.unban", false);
		player.setPermission("proxy.unmute", false);
		
	}

}
