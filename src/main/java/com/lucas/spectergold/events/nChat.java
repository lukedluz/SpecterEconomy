package com.lucas.spectergold.events;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import com.lucas.spectergold.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class nChat implements Listener {

    @EventHandler
    public void onChat(ChatMessageEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (e.getTags().contains("samuel")) {
            if (Main.getSamuel().equalsIgnoreCase(e.getSender().getName())) {
                e.setTagValue("samuel", Main.getInstance().getConfig().getString("nchat.samuel_prefix")
                .replace('&', '§'));
            }
        }
    }
}
