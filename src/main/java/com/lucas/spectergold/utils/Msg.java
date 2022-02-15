package com.lucas.spectergold.utils;

import com.lucas.spectergold.Main;

public class Msg {

	public static String get(String path){
		return Main.getInstance().getConfig().getString("mensagens." + path).replaceAll("&", "§");
	}
	
}
