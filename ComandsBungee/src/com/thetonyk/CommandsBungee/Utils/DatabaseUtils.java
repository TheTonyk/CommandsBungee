package com.thetonyk.CommandsBungee.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.thetonyk.CommandsBungee.Main;

public class DatabaseUtils {

    private static Connection connection;

    public static Connection getConnection() throws SQLException {
    	
        if (connection != null) if (connection.isValid(1)) return connection; else connection.close();

        try {
        	
            Class.forName("com.mysql.jdbc.Driver");
            
        } catch (ClassNotFoundException ex) {
        	
            Main.proxy.getLogger().severe("[DatabaseUtils] Unable to load the JDBC Driver.");
            
        }
        
        connection = DriverManager.getConnection("jdbc:mysql://localhost/commandspvp", PassUtils.user, PassUtils.pass);

        return connection;
        
    }
    
    public static void sqlInsert (String request) {
    	
    	Main.proxy.getProxy().getScheduler().runAsync(Main.proxy, new Runnable() {
			
			public void run() {
				
				try {
		    		
		    		DatabaseUtils.getConnection().createStatement().executeUpdate(request);
		    		
		    	} catch (SQLException exception) {
		    		
		    		return;
		    		
		    	}
				
			}
			
		});

    }

}


