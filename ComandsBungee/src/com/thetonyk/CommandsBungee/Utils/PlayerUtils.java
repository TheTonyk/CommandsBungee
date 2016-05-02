package com.thetonyk.CommandsBungee.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.thetonyk.CommandsBungee.Main;
import com.thetonyk.CommandsBungee.Utils.DatabaseUtils;
import com.thetonyk.CommandsBungee.Utils.PlayerUtils;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PlayerUtils {
	
	public static Boolean exist(ProxiedPlayer player) {
		
		Boolean exist = false;
		
		try {
		
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT * FROM users WHERE uuid = '" + player.getUniqueId().toString() + "';");
			
			if (req.next()) exist = true;
			
			sql.close();
			req.close();
			
		} catch (SQLException exception) {
			
			Main.proxy.getLogger().severe("[PlayerUtils] Error to check if player " + player.getName() + " is new.");
			
		}
		
		return exist;
		
	}
	
	public static Boolean exist(String player) {
		
		Boolean exist = false;
		
		try {
		
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT * FROM users WHERE name = '" + player + "';");
			
			if (req.next()) exist = true;
				
			sql.close();
			req.close();
			
		} catch (SQLException exception) {
			
			Main.proxy.getLogger().severe("[PlayerUtils] Error to check if player " + player + " is new.");
			
		}
		
		return exist;
		
	}
	
	public static int getId (String name) {
		
		int id = 0;
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT id FROM users WHERE name ='" + name + "';");
			
			if (req.next()) id = req.getInt("id");
			
			sql.close();
			req.close();
			
		} catch (SQLException exception) {
			
			Main.proxy.getLogger().severe("[PlayerUtils] Error to get id of player " + name + ".");
			
		}
		
		return id;
		
	}
	
	public static int getId (UUID uuid) {
		
		int id = 0;
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT id FROM users WHERE uuid ='" + uuid + "';");
			
			if (req.next()) id = req.getInt("id");
			
			sql.close();
			req.close();
			
		} catch (SQLException exception) {
			
			Main.proxy.getLogger().severe("[PlayerUtils] Error to get id of player with UUID " + uuid + ".");
			
		}
		
		return id;
		
	}

	public static int getPrivatesState (ProxiedPlayer player) {
		
		int privateState = 0;
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT private FROM settings WHERE id = " + PlayerUtils.getId(player.getUniqueId()) + ";");
			
			if (req.next()) privateState = req.getInt("private");
			
			sql.close();
			req.close();
			
		} catch (SQLException exception) {
			
			Main.proxy.getLogger().severe("[PlayerUtils] Error to get private state of player " + player.getName() + ".");
			
		}
		
		return privateState;
		
	}
	
	public static int uniquePlayers() {
		
		int players = 0;
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet count = sql.executeQuery("SELECT COUNT(*) AS NumbersOfPlayers FROM users;");
			
			if (count.next()) players = Integer.parseInt(count.getString("NumbersOfPlayers"));
			
			sql.close();
			count.close();
			
		} catch (SQLException exception) {
			
			Main.proxy.getLogger().severe("[PlayerUtils] Error to get to number of players.");
			
		}
		
		return players;
		
	}
	
	public static void preJoinUpdatePlayer(String player, UUID uuid, String ip) {
			
		Boolean newIP = true;
		String oldIPs = "";
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT * FROM users WHERE uuid = '" + uuid + "';");
			
			if (req.next()) {
				
				if (!req.getString("name").equalsIgnoreCase(player)) Main.newPseudo.put(uuid, req.getString("name"));
			
				for (String playerIP : req.getString("ip").split(";")) {
					
					if (playerIP.equalsIgnoreCase(ip)) newIP = false;
					
				}
				
				oldIPs = req.getString("ip");
				
			}
			
			sql.close();
			req.close();
		
		} catch (SQLException exception) {
			
			Main.proxy.getLogger().severe("[PlayerUtils] Error to get IP's of player " + player + ".");
			
		}
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			
			sql.executeUpdate("UPDATE users SET name = '" + player + "'" + (newIP ? ", ip = '" + oldIPs + ip + ";' " : " ") + "WHERE uuid = '" + uuid + "';");
			
			sql.close();
				
		} catch (SQLException exception) {
			
			Main.proxy.getLogger().severe("[PlayerUtils] Error to update player " + player + ".");
			
		}

	}
	
	public static void joinUpdatePlayer(String player, UUID uuid, String ip) {
		
		if (!exist(Main.proxy.getProxy().getPlayer(uuid))) {
			
			try {
				
				Statement sql = DatabaseUtils.getConnection().createStatement();
				
				sql.executeUpdate("INSERT INTO users (`name`, `uuid`, `ip`, `firstJoin`, `lastJoin`, `lastQuit`, `rank`, `muteState`, `muteReason`, `muteTime`) VALUES ('" + player + "', '" + uuid + "', '" + ip + ";', '" + new Date().getTime() + "', '" + new Date().getTime() + "', '0', 'PLAYER', false, '0', '-1');");
				sql.executeUpdate("INSERT INTO settings (`id`, `players`, `chat`, `mentions`, `private`, `ignored`) VALUES ('" + PlayerUtils.getId(uuid) + "', 1, 1, 1, 1, '');");
				
				sql.close();
				
			} catch (SQLException exception) {
				
				Main.proxy.getLogger().severe("[PlayerUtils] Error to insert new player " + player + ".");
				
			}
			
		} else {
			
			try {
				
				Statement sql = DatabaseUtils.getConnection().createStatement();
				
				sql.executeUpdate("UPDATE users SET lastJoin = '" + new Date().getTime() + "' WHERE uuid = '" + uuid + "';");
					
				sql.close();
					
			} catch (SQLException exception) {
				
				Main.proxy.getLogger().severe("[PlayerUtils] Error to update player " + player + ".");
				
			}
			
		}
		
	}
	
	public static void leaveUpdatePlayer(ProxiedPlayer player) {
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			
			sql.executeUpdate("UPDATE users SET lastQuit = '" + new Date().getTime() + "' WHERE uuid = '" + player.getUniqueId().toString() + "';");
			
			sql.close();
		
		} catch (SQLException exception) {
			
			Main.proxy.getLogger().severe("[PlayerUtils] Error to update player " + player.getName() + ".");
			
		}
		
	}
	
	public static Map<String, Integer> getAlts(UUID id) {
		
		String playerIPs = "";
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet ips = sql.executeQuery("SELECT * FROM users WHERE uuid = '" + id + "';");
			
			if (ips.next()) playerIPs = ips.getString("ip");
			
			sql.close();
			ips.close();
		
		} catch (SQLException e) {
			
			Main.proxy.getLogger().severe("[PlayerUtils] Error to get IP's of player with UUID " + id + ".");
			
		}
		
		Map<String, Integer> alts = new HashMap<String, Integer>();
		
		for (String playerIP : playerIPs.split(";")) {
			
			try {
				
				Statement sql = DatabaseUtils.getConnection().createStatement();
				ResultSet ips = sql.executeQuery("SELECT * FROM users WHERE ip LIKE '%" + playerIP + "%';");
				
				while (ips.next()) {
					
					if (ips.getString("uuid").equalsIgnoreCase(id.toString())) continue;
						
					for (String ip : ips.getString("ip").split(";")) {
						
						if (ip.equalsIgnoreCase(playerIP)) {
							
							if (!alts.containsKey(ips.getString("name"))) alts.put(ips.getString("name"), 1);
							else {
								
								int nb = (alts.get(ips.getString("name")) + 1);
								alts.put(ips.getString("name"), nb);
								
							}
							
						}
						
					}
					
				}
				
				sql.close();
				ips.close();
			
			} catch (SQLException exception) {
				
				Main.proxy.getLogger().severe("[PlayerUtils] Error to get IP's of a player.");
				
			}
			
		}
		
		return alts;
		
	}
	
	public enum Rank {
		
		PLAYER("", "§7Player"), WINNER("§6Winner §8| ", "§6Winner"), FAMOUS("§bFamous §8| ", "§bFamous"), BUILDER("§2Build §8| ", "§2Builder"),STAFF("§cStaff §8| ", "§cStaff"), MOD("§9Mod §8| ", "§9Moderator"), ADMIN("§4Admin §8| ", "§4Admin"), FRIEND("§3Friend §8| ", "§3Friend"), HOST("§cHost §8| ", "§cHost"), ACTIVE_BUILDER("§2Build §8| ", "§2Builder");
		
		String prefix;
		String name;
		
		private Rank(String prefix, String name) {
			
			this.prefix = prefix;
			this.name = name;
			
		}
		
		public String getPrefix() {
			
			return prefix;
			
		}
		
		public String getName() {
			
			return name;
			
		}
		
	}
	
	public static Rank getRank(String player) {
		
		Rank rank = Rank.PLAYER;
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT rank FROM users WHERE name = '" + player + "';");
			
			if (req.next()) rank = Rank.valueOf(req.getString("rank"));
			
			sql.close();
			req.close();
			
		} catch (SQLException exception) {
			
			Main.proxy.getLogger().severe("[PlayerUtils] Error to get the rank of player " + player + ".");
			
		}
		
		return rank;
		
	}
	
	public static UUID getUUID (String name) {
		
		UUID uuid = null;
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT * FROM users WHERE name = '" + name + "';");
			
			if (req.next()) uuid = UUID.fromString(req.getString("uuid"));
			
			sql.close();
			req.close();
			
		} catch (SQLException exception) {
			
			Main.proxy.getLogger().severe("[PlayerUtils] Error to get UUID of player " + name + ".");
			
		}
		
		return uuid;
		
	}
	
	public static UUID getUUID (int id) {
		
		UUID uuid = null;
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT * FROM users WHERE id = '" + id + "';");
			
			if (req.next()) uuid = UUID.fromString(req.getString("uuid"));
				
			sql.close();
			req.close();
			
		} catch (SQLException exception) {
			
			Main.proxy.getLogger().severe("[PlayerUtils] Error to get UUID of player with id " + id + ".");
			
		}
		
		return uuid;
		
	}
	
	public static String getName (int id) {
		
		String name = null;
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT * FROM users WHERE id = '" + id + "';");
			
			if (req.next()) name = req.getString("name");
				
			sql.close();
			req.close();
			
		} catch (SQLException exception) {
			
			Main.proxy.getLogger().severe("[PlayerUtils] Error to get name of player with id " + id + ".");

		}
		
		return name;
		
	}
	
	public static int getChatVisibility (UUID player) {
		
		int chatVisibility = 0;
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT chat FROM settings WHERE id = " + PlayerUtils.getId(player) + ";");
			
			if (req.next()) chatVisibility = req.getInt("chat");
			
			sql.close();
			req.close();

		} catch (SQLException exception) {
			
			Main.proxy.getLogger().severe("[PlayerUtils] Error to get chat setting of player with UUID " + player + ".");
			
		}
		
		return chatVisibility;
		
	}
	
	public static int getMentionsState (UUID player) {
		
		int mentionsState = 0;
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT mentions FROM settings WHERE id = " + PlayerUtils.getId(player) + ";");
			
			if (req.next()) mentionsState = req.getInt("mentions");
			
			sql.close();
			req.close();
			
		} catch (SQLException exception) {
			
			Main.proxy.getLogger().severe("[PlayerUtils] Error to get mentions setting of player with UUID " + player + ".");
			
		}
		
		return mentionsState;
		
	}
	
	public static List<UUID> getIgnoredPlayers (UUID player) {
		
		String ignoredPlayers = "";
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT ignored FROM settings WHERE id = " + PlayerUtils.getId(player) + ";");
			
			if (req.next()) ignoredPlayers = req.getString("ignored");
			
			sql.close();
			req.close();
			
		} catch (SQLException exception) {
			
			Main.proxy.getLogger().severe("[PlayerUtils] Error to get ignored players of player with UUID " + player + ".");
			
		}
		
		List<UUID> ignored = new Gson().fromJson(ignoredPlayers, new TypeToken<List<UUID>>(){}.getType());
		return ignored;
		
	}
	
	public static void setIgnoredPlayers (UUID player, List<UUID> ignoredPlayers) {
		
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		String ignored = gson.toJson(ignoredPlayers);
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			sql.executeUpdate("UPDATE settings SET ignored = '" + ignored + "' WHERE id = " + PlayerUtils.getId(player) + ";");
			sql.close();
			
		} catch (SQLException exception) {
			
			Main.proxy.getLogger().severe("[PlayerUtils] Error to set ignored players of player with UUID " + player + ".");
			
		}
		
	}
	
	/* Kick Methods */
	
	public static List<String[]> getAllKick (UUID player) {
		
		List<String[]> kicks = new ArrayList<String[]>();
		
		try {
		
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT * FROM kick WHERE player = '" + PlayerUtils.getId(player) + "' ORDER BY date DESC;");
			
			while (req.next()) {
				
				String[] kick = new String[3];
				kick[0] = req.getString("date");
				kick[1] = req.getString("reason");
				kick[2] = String.valueOf(req.getInt("operator"));
				
				kicks.add(kick);
				
			}
			
			sql.close();
			req.close();
			
		} catch (SQLException exception) {
			
			Main.proxy.getLogger().severe("[PlayerUtils] Error to get all kick of player with UUID " + player + " is banned.");
			
		}
		
		return kicks;
		
	}
	
	/* Ban Methods */
	
	public static int isBanned (UUID player) {
		
		int id = 0;
		
		try {
		
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT * FROM bans WHERE player = '" + PlayerUtils.getId(player) + "';");
			
			while (req.next()) {
				
				if (Long.parseLong(req.getString("duration")) < 0) {
					
					if (req.getInt("cancel") == 0) id = req.getInt("id");
					
				}
				
				if (Long.parseLong(req.getString("date")) + Long.parseLong(req.getString("duration")) >= new Date().getTime()) {
					
					if (req.getInt("cancel") == 0) id = req.getInt("id");
					
				}
				
			}
			
			sql.close();
			req.close();
			
		} catch (SQLException exception) {
			
			Main.proxy.getLogger().severe("[PlayerUtils] Error to check if player with UUID " + player + " is banned.");
			
		}
		
		return id;
		
	}
	
	public static List<String> isBanned (String ip) {
		
		List<String> players = new ArrayList<String>();
		
		try {
		
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT * FROM users WHERE ip LIKE '%" + ip + "%';");
			
			while (req.next()) {
				
				for (String playerIP : req.getString("ip").split(";")) {
					
					if (!playerIP.equalsIgnoreCase(ip)) continue;
					
					if (PlayerUtils.isBanned(UUID.fromString(req.getString("uuid"))) == 0) continue;
						
					if (players.contains(req.getString("name"))) continue;
					
					players.add(req.getString("name"));
					
				}
				
			}
			
			sql.close();
			req.close();
			
		} catch (SQLException exception) {
			
			Main.proxy.getLogger().severe("[PlayerUtils] Error to check if IP " + ip + " is banned.");
			
		}
		
		return players;
		
	}
	
	public static long getBanDate(int id) {
		
		long date = 0;
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT * FROM bans WHERE id = " + id + ";");
			
			if (req.next()) date = Long.parseLong(req.getString("date"));
				
			sql.close();
			req.close();
			
		} catch (SQLException exception) {
			
			Main.proxy.getLogger().severe("[PlayerUtils] Error to get the ban date of ban id " + id + ".");
			
		}
		
		return date;
		
	}
	
	public static long getBanDuration(int id) {
		
		long duration = 0;
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT * FROM bans WHERE id = " + id + ";");
			
			if (req.next()) duration = Long.parseLong(req.getString("duration"));
			
			sql.close();
			req.close();
			
		} catch (SQLException exception) {
			
			Main.proxy.getLogger().severe("[PlayerUtils] Error to get the ban duration of ban id " + id + ".");
			
		}
		
		return duration;
		
	}
	
	public static String getBanReason(int id) {
		
		String reason = "Banned";
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT * FROM bans WHERE id = " + id + ";");
			
			if (req.next()) reason = req.getString("reason");
				
			sql.close();
			req.close();
			
		} catch (SQLException exception) {
			
			Main.proxy.getLogger().severe("[PlayerUtils] Error to get the ban reason of ban id " + id + ".");
			
		}
		
		return reason;
		
	}
	
	public static List<String[]> getAllBans (UUID player) {
		
		List<String[]> bans = new ArrayList<String[]>();
		
		try {
		
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT * FROM bans WHERE player = '" + PlayerUtils.getId(player) + "' ORDER BY date DESC;");
			
			while (req.next()) {
				
				String[] ban = new String[4];
				ban[0] = req.getString("date");
				ban[1] = req.getString("duration");
				ban[2] = req.getString("reason");
				ban[3] = String.valueOf(req.getInt("operator"));
				bans.add(ban);
				
			}
			
			sql.close();
			req.close();
			
		} catch (SQLException exception) {
			
			Main.proxy.getLogger().severe("[PlayerUtils] Error to get all bans of player with UUID " + player + ".");
			
		}
		
		return bans;
		
	}
	
	public static int getCanceledBan (int id) {
		
		int canceled = 0;
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT * FROM bans WHERE id = " + id + ";");
			
			if (req.next()) canceled = req.getInt("cancel");
			
			sql.close();
			req.close();
			
		} catch (SQLException exception) {
			
			Main.proxy.getLogger().severe("[PlayerUtils] Error to get canceled state of ban with ban id " + id + ".");
			
		}
		
		return canceled;
		
	}
	
	public static int getBanId (long date, String name) {
		
		int id = 0;
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT * FROM bans WHERE date = '" + date + "' AND player = " + PlayerUtils.getId(name) + ";");
			
			if (req.next()) id = req.getInt("id");
			
			sql.close();
			req.close();
			
		} catch (SQLException exception) {
			
			Main.proxy.getLogger().severe("[PlayerUtils] Error to get ban id of ban with date " + date + " and player " + name + ".");
			
		}
		
		return id;
		
	}
	
	public static void cancelBan (int id, int sender) {
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			sql.executeUpdate("UPDATE bans SET cancel = " + sender + " WHERE id = " + id + ";");
			sql.close();
			
		} catch (SQLException exception) {
			
			Main.proxy.getLogger().severe("[PlayerUtils] Error to cancel the ban with id " + id + ".");
			
		}
		
	}
	
	/* Mute Methods */
	
	public static int isMuted (UUID player) {
		
		int muted = 0;
		
		try {
		
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT * FROM mute WHERE player = '" + PlayerUtils.getId(player) + "';");
			
			while (req.next()) {
				
				if (Long.parseLong(req.getString("duration")) < 0) {
					
					if (req.getInt("cancel") == 0) muted = req.getInt("id");
					
				}
				
				if (Long.parseLong(req.getString("date")) + Long.parseLong(req.getString("duration")) >= new Date().getTime()) {
					
					if (req.getInt("cancel") == 0) muted = req.getInt("id");
					
				}
				
			}
			
			sql.close();
			req.close();
			
		} catch (SQLException exception) {
			
			Main.proxy.getLogger().severe("[PlayerUtils] Error to check if player with UUID " + player + " is muted.");
			
		}
		
		return muted;
		
	}
	
	public static long getMuteDate(int id) {
		
		long date = 0;
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT * FROM mute WHERE id = " + id + ";");
			
			if (req.next()) date = Long.parseLong(req.getString("date"));
			
			sql.close();
			req.close();
			
		} catch (SQLException exception) {
			
			Main.proxy.getLogger().severe("[PlayerUtils] Error to get the mute date of player with ban id " + id + ".");
			
		}
		
		return date;
		
	}
	
	public static long getMuteDuration(int id) {
		
		long duration = 0;
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT * FROM mute WHERE id = " + id + ";");
			
			if (req.next()) duration = Long.parseLong(req.getString("duration"));
			
			sql.close();
			req.close();
			
		} catch (SQLException exception) {
			
			Main.proxy.getLogger().severe("[PlayerUtils] Error to get the mute duration of player with ban id " + id + ".");
			
		}
		
		return duration;
		
	}
	
	public static List<String[]> getAllMute (UUID player) {
		
		List<String[]> mutes = new ArrayList<String[]>();
		
		try {
		
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT * FROM mute WHERE player = '" + PlayerUtils.getId(player) + "' ORDER BY date DESC;");
			
			while (req.next()) {
				
				String[] mute = new String[4];
				mute[0] = req.getString("date");
				mute[1] = req.getString("duration");
				mute[2] = req.getString("reason");
				mute[3] = String.valueOf(req.getInt("operator"));
				mutes.add(mute);
				
			}
			
			sql.close();
			req.close();
			
		} catch (SQLException exception) {
			
			Main.proxy.getLogger().severe("[PlayerUtils] Error to get all mute of player with UUID " + player + ".");
			
		}
		
		return mutes;
		
	}
	
	public static int getCanceledMute (int id) {
		
		int canceled = 0;
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT * FROM mute WHERE id = " + id + ";");
			
			if (req.next()) canceled = req.getInt("cancel");
			
			sql.close();
			req.close();
			
		} catch (SQLException exception) {
			
			Main.proxy.getLogger().severe("[PlayerUtils] Error to get canceled state of mute with ban id " + id + ".");
			
		}
		
		return canceled;
		
	}
	
	public static int getMuteId (long date, String name) {
		
		int id = 0;
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			ResultSet req = sql.executeQuery("SELECT * FROM mute WHERE date = '" + date + "' AND player = " + PlayerUtils.getId(name) + ";");
			
			if (req.next()) id = req.getInt("id");
			
			sql.close();
			req.close();
			
		} catch (SQLException exception) {
			
			Main.proxy.getLogger().severe("[PlayerUtils] Error to get mute id of mute with date " + date + " and player " + name + ".");
			
		}
		
		return id;
		
	}
	
	public static void cancelMute (int id, int sender) {
		
		try {
			
			Statement sql = DatabaseUtils.getConnection().createStatement();
			sql.executeUpdate("UPDATE mute SET cancel = " + sender + " WHERE id = " + id + ";");
			sql.close();
			
		} catch (SQLException exception) {
			
			Main.proxy.getLogger().severe("[PlayerUtils] Error to cancel the mute with id " + id + ".");
			
		}
		
	}
	
}
