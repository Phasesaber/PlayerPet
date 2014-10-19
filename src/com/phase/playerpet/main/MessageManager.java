package com.phase.playerpet.main;

import org.bukkit.entity.Player;

public class MessageManager {

	static String prefix = "§e►§bPlayerPet!§e►§a "; 
	
	public static boolean sendMessage(Player p, String string) {
		p.sendMessage(prefix+string);
		return false;
	}

}
