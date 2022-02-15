package com.lucas.spectergold.storage;

import java.util.HashSet;

public class Query {
	
	private HashSet<String> update = new HashSet<>();
	private HashSet<String> normal = new HashSet<>();
	
	private boolean async = false;;
	
	public void addUpdate(String querycommand) {
		update.add(querycommand);
	}
	public void add(String querycommand) {
		normal.add(querycommand);
	}
	public void setAsync(boolean arg) {
		this.async = arg;
	}
	public boolean isAsync() {
		return async;
	}
	public HashSet<String> getUpdates() {
		return update;
	}
	public HashSet<String> getNormal() {
		return normal;
	}

}
