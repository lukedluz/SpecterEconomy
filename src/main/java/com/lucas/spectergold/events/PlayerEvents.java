package com.lucas.spectergold.events;

import com.lucas.spectergold.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerEvents implements Listener{
	
	@EventHandler
	public void onplayerJoin(PlayerJoinEvent e){
		Player p = e.getPlayer();
		if (Main.getGoldCore().getCached(p.getName()) == null) {
			Main.getGoldCore().createAccount(p.getName());
		}
	}

}
