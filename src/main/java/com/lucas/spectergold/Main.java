package com.lucas.spectergold;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.lucas.spectergold.cmds.Gold;
import com.lucas.spectergold.events.nChat;
import com.lucas.spectergold.hook.VaultHandler;
import com.lucas.spectergold.runnables.SaveTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import com.lucas.spectergold.events.PlayerEvents;
import com.lucas.spectergold.storage.Query;
import com.lucas.spectergold.storage.Storage;
import com.lucas.spectergold.system.GoldCore;
import com.lucas.spectergold.system.PGold;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin{
	
	private static Main instance;
	private static Storage database;
	private static GoldCore core;
	private static SaveTask save_task;
	private static VaultHandler vault;
    private static String SAMUEL = "";

	public void onEnable() {
        saveDefaultConfig();
        instance = this;
        initDatabase();
        core = new GoldCore();
        getServer().getPluginManager().registerEvents(new PlayerEvents(), this);
        if (getConfig().getBoolean("nchat.ativar")) {
            getServer().getPluginManager().registerEvents(new nChat(), this);
        }
        getCommand("gold").setExecutor(new Gold());
        setupVault();
        debug("Plugin inicializado.");
        save_task = new SaveTask();
        save_task.runTaskTimerAsynchronously(this, 2*20L, 60*20L);
	}
	public void onDisable(){
		try {
			debug("Checando contas...");
			if (!Main.getGoldCore().getFila().isEmpty()) {
				Connection c = Main.getStorage().getConnection();
				Statement stmt = c.createStatement();
				int i = 0;
				for (PGold pm : Main.getGoldCore().getFila()) {
					String caseplayer = pm.getPlayer().toLowerCase();
					stmt.executeUpdate("UPDATE economy SET money='" + pm.getGold() + "' WHERE case_player='" + caseplayer + "'");
					i++;
				}
				Main.debug("Foram salvas '" + i + "' saldos na database.");
				c.close();
				stmt.close();
				//Main.getMGoldCore().getFila().clear();
			}
			debug("Desabilitando...");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void setSamuel(String mag) {
	    SAMUEL = mag;
    }

    public static String getSamuel() {
	    return SAMUEL;
    }

    public static GoldCore getGoldCore(){
		return core;
	}

	public static Main getInstance(){
		return instance;
	}

	public static void debug(String msg){
		System.out.println("[SpecterEconomy] " + msg);
	}

	public static Storage getStorage(){
		return database;
	}

	private void initDatabase(){
		Main.database = new Storage(this);
		if (getConfig().getBoolean("MySQL.enable")) {
			String user = getConfig().getString("MySQL.user");
			String senha = getConfig().getString("MySQL.senha");
			String host = getConfig().getString("MySQL.host");
			String database = getConfig().getString("MySQL.database");
			Main.database.initMySQL(user, senha, host, database);
		} else {
			Main.database.initSQLite("storage.db");
		}
		Query q = new Query();
		/*
		 * stmt.execute("CREATE TABLE IF NOT EXISTS economy (real_player TEXT, case_player TEXT, gold TEXT)");
			stmt.execute("CREATE TABLE IF NOT EXISTS inative (player TEXT, time TEXT)");
		 */
		q.add("CREATE TABLE IF NOT EXISTS economy (real_player varchar(16), case_player varchar(16), gold TEXT)");
		q.add("CREATE TABLE IF NOT EXISTS inative (player TEXT, time TEXT)");
		q.setAsync(false);
		try {
			Main.database.execute(q);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			debug("Nao foi possível gerar as tabelas do plugin, desabilitando...");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		debug("Database inicializada.");
	}
	public void setupVault(){
		vault = new VaultHandler();
		getServer().getServicesManager().register(Economy.class, vault, this, ServicePriority.Highest);
		debug("Vault suport ativado.");
	}
	
}
