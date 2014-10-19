package com.phase.playerpet.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import com.phase.playerpet.main.MessageManager;
import com.phase.playerpet.main.PlayerPet;

public class RideEntity implements Listener {
	
	@EventHandler
	public void clickPlayerPet(PlayerInteractEntityEvent e){
		Player p = e.getPlayer();
		Entity n = e.getRightClicked();
		if(PlayerPet.getNPC(p).getBukkitEntity().equals(n)){
			if(p.isSneaking()){
				if(p.getPassenger() != null && p.getPassenger().equals(PlayerPet.getNPC(p).getBukkitEntity())) return;
				p.setPassenger(n);
				MessageManager.sendMessage(p, "Your pet is riding you!");
			}
			else{
				if(p.getPassenger() != null && p.getPassenger().equals(PlayerPet.getNPC(p).getBukkitEntity())) return;
				n.setPassenger(p);
				MessageManager.sendMessage(p, "You are riding your pet!");
			}
		}	
	}
	
	@EventHandler
	public void dismountPet(PlayerInteractEvent e){
		Player p = e.getPlayer();
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
			if(p.isSneaking()){
				if(p.getPassenger() == null) return;
				if(p.getPassenger().equals(PlayerPet.getNPC(p).getBukkitEntity())){
					PlayerPet.getNPC(p).getBukkitEntity().setSneaking(true);
					p.setPassenger(null);
					MessageManager.sendMessage(p, "Your pet has been dismounted!");
					e.setCancelled(true);
				}
			}
		}
	}
	
}
