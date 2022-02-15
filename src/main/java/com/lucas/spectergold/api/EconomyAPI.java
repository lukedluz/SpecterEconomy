package com.lucas.spectergold.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bukkit.entity.Player;

import com.lucas.spectergold.Main;
import com.lucas.spectergold.system.PGold;

public class EconomyAPI {
	
	public static List<PGold> getTop(int size) {
		List<PGold> convert = new ArrayList<>();
		convert.addAll(Main.getGoldCore().getGoldList());
		Collections.sort(convert, new Comparator<PGold>() {
				
			@Override
			public int compare(PGold pt1, PGold pt2){
				
				Float f1 = (float)pt1.getGold();
				Float f2 = (float)pt2.getGold();
				
				return f2.compareTo(f1);
				
			}
			
		});
	//	Collections.reverse(convert);
		if (convert.size() > size){
			convert = convert.subList(0, size);
		}
		return convert;
	}
	public static String getSamuel() {
		List<PGold> mag = getTop(1);
		String samuel = mag.get(0).getPlayer();
		return samuel;
	}
	public static double getSaldo(Player player) {
		return Main.getGoldCore().getSaldo(player.getName());
	}
	public static double getSaldo(String player) {
		return Main.getGoldCore().getSaldo(player);
	}
	public static void giveGold(String player, double amount) {
		Main.getGoldCore().add(player, amount);
	}
	public static void removeGold(String player, double amount) {
		Main.getGoldCore().remove(player, amount);
	}
	public static List<PGold> getPluginTopList(){
		return Main.getGoldCore().getTopList();
	}
}
