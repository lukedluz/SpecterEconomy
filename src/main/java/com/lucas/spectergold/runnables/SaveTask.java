package com.lucas.spectergold.runnables;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.lucas.spectergold.utils.Msg;
import com.lucas.spectergold.utils.Titles;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import com.lucas.spectergold.Main;
import com.lucas.spectergold.api.EconomyAPI;
import com.lucas.spectergold.system.PGold;

public class SaveTask extends BukkitRunnable{
	
	public void run(){
		
		try {
			
			if (!Main.getGoldCore().getFila().isEmpty()) {
				Connection c = Main.getStorage().getConnection();
				Statement stmt = c.createStatement();
				int i = 0;
				for (PGold pm : Main.getGoldCore().getFila()) {
					String caseplayer = pm.getPlayer().toLowerCase();
					stmt.executeUpdate("UPDATE economy SET gold='" + pm.getGold() + "' WHERE case_player='" + caseplayer + "'");
					i++;
				}
				Main.debug("Foram atualizados '" + i + "' saldos na database.");
				stmt.close();
				Main.getGoldCore().getFila().clear();
			}
			Main.getGoldCore().getGoldList().clear();
			for (String pms : Main.getGoldCore().getChache().keySet()){
				PGold ps = Main.getGoldCore().getChache().get(pms);
				Main.getGoldCore().getGoldList().add(ps);
			}
			Main.getGoldCore().getTopList().clear();
			Main.getGoldCore().getTopList().addAll(EconomyAPI.getTop(Main.getInstance().getConfig().getInt("config.top_size")));
			String samuel = Main.getSamuel();
			String newmag = EconomyAPI.getSamuel();
			if (!samuel.equalsIgnoreCase(newmag)) {
				Main.setSamuel(newmag);
				Bukkit.broadcastMessage(Msg.get("new_samuel")
				    .replace("@player", newmag));
				if (Main.getInstance().getConfig().getBoolean("config.titles.enable")) {
                    Titles th = new Titles();
                    th.setTitle("§a@player §fé o novo §aSamuel Bellamy§f.!");
                    th.setSubtitle("§fO pirata mais rico do mundo.");
                    th.setFadeInTime(1);
                    th.setFadeOutTime(1);
                    th.setStayTime(20);
                }
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}
