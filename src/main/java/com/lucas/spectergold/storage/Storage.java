package com.lucas.spectergold.storage;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Storage {
	
	private String user = "";
	private String password = "";
	private String host = "";
	private String database = "";
	private int port = 3306;
	
	private Connection c = null;
	
	private StorageType type;
	
	private Plugin plugin;
	
	public Storage(Plugin plugin) {
		this.plugin = plugin;
	}
	
	public void initMySQL(String user, String password, String host, String database, int port) {
		this.user = user;
		this.password = password;
		this.host = host;
		this.database = database;
		this.port = port;
		
		this.type = StorageType.MySQL;
		
		try {
			
			debug("§aConectando ao servidor MySQL...");
			debug("§aHost: " + host);
			c = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, password);
			debug("§aConexao estabelecida com sucesso!");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void initMySQL(String user, String password, String host, String database) {
		this.user = user;
		this.password = password;
		this.host = host;
		this.database = database;
		
		this.type = StorageType.MySQL;
		
		try {
			
			debug("§aConectando ao servidor MySQL...");
			debug("§aHost: " + host);
			c = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, password);
			debug("§aConexão estabelecida com sucesso!");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	public void execute(Query query) throws SQLException {
		if (query.isAsync()) {
			new BukkitRunnable() { // No lag in principal server thread
				
				@Override
				public void run() {
					try {
					// TODO Auto-generated method stub
						if (!query.getUpdates().isEmpty()) {
							for (String a : query.getUpdates()) {
								PreparedStatement pstmt = c.prepareStatement(a);
								pstmt.executeUpdate();
								pstmt.close();
							}
						}
						if (!query.getNormal().isEmpty()) {
							for (String a : query.getNormal()) {
								PreparedStatement pstmt = c.prepareStatement(a);
								pstmt.execute();
								pstmt.close();
							}
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}.runTaskAsynchronously(plugin);
		} else {
			if (!query.getUpdates().isEmpty()) {
				for (String a : query.getUpdates()) {
					PreparedStatement pstmt = c.prepareStatement(a);
					pstmt.executeUpdate();
					pstmt.close();
				}
			}
			if (!query.getNormal().isEmpty()) {
				for (String a : query.getNormal()) {
					PreparedStatement pstmt = c.prepareStatement(a);
					pstmt.execute();
					pstmt.close();
				}
			}
		}
	}
	// Init sql storage
	public void initSQLite(String database) {
		this.database = database;
		type = StorageType.SQLite;
		File path = new File("plugins/" + plugin.getName());
		if (!path.exists())
			path.mkdirs();
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:plugins/" + plugin.getName() + "/" + database);
			debug("§aSQLite inicializado com sucesso.");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void debug(String msg) {
		plugin.getLogger().log(Level.INFO, msg);
	}
	
	public Connection getConnection() throws SQLException {
		
		if (c.isClosed() || c == null) {
			if (type == StorageType.SQLite) {
				try {
					Class.forName("org.sqlite.JDBC");
					c = DriverManager.getConnection("jdbc:sqlite:plugins/" + plugin.getName() + "/" + database);
					debug("§aSQLite Conectado.");
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			} else {
				debug("§cConectando ao servidor MySQL...");
				debug("Host: " + host);
				c = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, password);
				debug("§aConexão estabelecida com sucesso!");
			}
			if (c.isClosed() || c == null) {
				return null;
			}
			return c;
		} else {
			return c;
		}
	}
}
enum StorageType {
	
	MySQL, SQLite
	
}
