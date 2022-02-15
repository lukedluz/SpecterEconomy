package com.lucas.spectergold.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EconomyGoldPayEvent extends Event {

    private static HandlerList handlers = new HandlerList();
    private Player sender;
    private Player receiver;
    private boolean cancelled = false;
    private double gold = 0;

    public EconomyGoldPayEvent(Player sender, Player receiver, double gold) {
        this.sender = sender;
        this.receiver = receiver;
        this.gold = gold;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getSender() {
        return sender;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Player getReceiver() {
        return receiver;
    }

    public void setReceiver(Player receiver) {
        this.receiver = receiver;
    }

    public double getGold() {
        return gold;
    }

    public void setGold(double gold) {
        this.gold = gold;
    }
}
