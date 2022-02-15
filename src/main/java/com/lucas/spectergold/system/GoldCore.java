package com.lucas.spectergold.system;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.lucas.spectergold.Main;

public class GoldCore {
	
	private HashMap<String, PGold> storage = new HashMap<>();
	private HashSet<PGold> save_fila = new HashSet<>();
	private List<PGold> top = new ArrayList<>();
	private List<PGold> top_list = new ArrayList<>();
	
	public GoldCore(){
		loadAccounts();
	}
	public HashMap<String, PGold> getChache(){
		return storage;
	}
	public HashSet<PGold> getFila(){
		return save_fila;
	}
	public List<PGold> getGoldList() {
		return top;
	}
	public List<PGold> getTopList() {
		return top_list;
	}
	public double getSaldo(String player) {
		String p = player.toLowerCase();
		if (storage.containsKey(p)) {
			return storage.get(p).getGold();
		}
		return 0;
	}
	public PGold getCached(String player){
		if (storage.containsKey(player.toLowerCase())) {
			return storage.get(player.toLowerCase());
		}
		return null;
	}
	public void createAccount(String player){
		PGold pm = new PGold(player);
		pm.setGold(Main.getInstance().getConfig().getDouble("config.saldo_inicial"));
		storage.put(player.toLowerCase(), pm);
		try {
			pm.save(true);
	//		Main.debug("Conta de '" + player + "' criada com sucesso.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void remove(String player, double saldo) {
		String p = player.toLowerCase();
		if (storage.containsKey(p)) {
			PGold pm = storage.get(p);
			pm.setGold(pm.getGold() - saldo);
			if (!save_fila.contains(pm)) {
				save_fila.add(pm);
			}
		}
	}
	public void checkFila(PGold pm) {
		if (!save_fila.contains(pm)) {
			save_fila.add(pm);
		}
	}
	public void add(String player, double saldo) {
		String p = player.toLowerCase();
		if (storage.containsKey(p)) {
			PGold pm = storage.get(p);
			pm.setGold(pm.getGold() + saldo);
			if (!save_fila.contains(pm)) {
				save_fila.add(pm);
			}
		}
	}
	private void loadAccounts(){
		Main.debug("Carregando contas...");
		
		try {
			Connection c = Main.getStorage().getConnection();
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM economy");
			int i = 0;
			while (rs.next()) {
				
				double balance = Double.valueOf(rs.getString("gold"));
				String player = rs.getString("real_player");
				
				PGold pm = new PGold(player);
				pm.setGold(balance);
				storage.put(player.toLowerCase(), pm);
				i++;
			}
			Main.debug("Foram carregadas " + i + " contas.");
			stmt.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Main.debug("§cNÃO FOI POSSÍVEL CARREGAR AS CONTAS, DESABILITANDO...");
			Main.getInstance().getServer().getPluginManager().disablePlugin(Main.getInstance());
		}
	}
}
