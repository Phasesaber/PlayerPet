package com.phase.playerpet.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.phase.playerpet.main.PlayerPet;

public class LogoutListener implements Listener {
	
	@EventHandler
	public void removeNPC(PlayerQuitEvent e){
		Player p = e.getPlayer();
		if(PlayerPet.hasNPC(p))
			PlayerPet.removeNPC(p);
	}
	
	
}
