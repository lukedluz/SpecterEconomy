package com.lucas.spectergold.system;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.lucas.spectergold.Main;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class PGold {
	
	private String player = "";
	private double gold = 0;
	
	public PGold(String player){
		this.player = player;
	//	loadData();
	}
	
	public void loadData() {
		try {
			Connection c = Main.getStorage().getConnection();
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT gold FROM economy WHERE case_player='" + player.toLowerCase() + "'");
			if (rs.next()){
				this.gold = Double.parseDouble(rs.getString("gold"));
			//	Main.debug("Gold de " + player + " retornado da database.");
			}
			stmt.close();
			rs.close();
		} catch (SQLException e){
			Bukkit.getPlayer(player).kickPlayer("§cOcorreu um erro interno, tente relogar.");
		}
	}
	
	public double getGold(){
		return this.gold;
	}
	public String getPlayer(){
		return this.player;
	}
	public void setGold(double value){
		this.gold = value;
	}
	public void save(boolean async) throws SQLException{
		if (!async) {
			Connection c = Main.getStorage().getConnection();
			Statement stmt = c.createStatement();
			Statement stmt2 = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT case_player FROM economy WHERE case_player='" + player.toLowerCase() + "'");
			if (rs.next()){
				stmt2.executeUpdate("UPDATE economy SET gold='" + gold + "' WHERE case_player='" + player.toLowerCase() + "'");
			} else {
				stmt2.execute("INSERT INTO economy (real_player, case_player, gold) VALUES ('" + player + "','" + player.toLowerCase() + "','" + gold + "')");
			}
			stmt.close();
			rs.close();
		//	Main.debug("Gold de " + player + " salvo.");
		} else {
			new BukkitRunnable() {
				
				@Override
				public void run() {
					try {
						Connection c = Main.getStorage().getConnection();
						Statement stmt = c.createStatement();
						Statement stmt2 = c.createStatement();
						ResultSet rs = stmt.executeQuery("SELECT case_player FROM economy WHERE case_player='" + player.toLowerCase() + "'");
						if (rs.next()){
							stmt2.executeUpdate("UPDATE economy SET gold='" + gold + "' WHERE case_player='" + player.toLowerCase() + "'");
						} else {
							stmt2.execute("INSERT INTO economy (real_player, case_player, gold) VALUES ('" + player + "','" + player.toLowerCase() + "','" + gold + "')");
						}
						stmt.close();
						rs.close();
				//		Main.debug("Gold de " + player + " salvo.");
					} catch (SQLException e){
						e.printStackTrace();
					}
					
				}
			}.runTaskAsynchronously(Main.getInstance());
		}
	}
}
