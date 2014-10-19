package com.phase.playerpet.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.phase.playerpet.main.MessageManager;
import com.phase.playerpet.main.PlayerPet;

public class TeleportListener implements Listener {
	
	@EventHandler
	public void ClickWithStick(PlayerInteractEvent e){
		Player p = e.getPlayer();
		if(!PlayerPet.hasNPC(p)) return;
		if(p.getItemInHand().getType() == Material.STICK && e.getAction() == Action.LEFT_CLICK_AIR){
			e.setCancelled(true);
			PlayerPet.getNPC(p).getBukkitEntity().teleport(p);
			MessageManager.sendMessage(p, "Your pet teleported to you!");
		}
	}
	
	@EventHandler
	public void PlayerTeleport(PlayerTeleportEvent e){
		if(PlayerPet.hasNPC(e.getPlayer()))
			PlayerPet.getNPC(e.getPlayer()).getBukkitEntity().teleport(e.getTo());
	}

}
