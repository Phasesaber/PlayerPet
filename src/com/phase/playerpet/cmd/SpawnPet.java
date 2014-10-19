package com.phase.playerpet.cmd;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.lenis0012.bukkit.npc.NPC;
import com.lenis0012.bukkit.npc.NPCFactory;
import com.lenis0012.bukkit.npc.NPCProfile;
import com.phase.playerpet.main.MessageManager;
import com.phase.playerpet.main.PlayerPet;

public class SpawnPet {
	
	PlayerPet plugin = PlayerPet.getInstance();
	
	public boolean spawnPet(final Player p, Command d, String c, final String[] args) {
		
		if(args.length == 2){
			NPC npc = spawnNpc(p, args[1], args[1]);
			MessageManager.sendMessage(p, args[1].replace('&', '§').replace('-', ' ').replaceAll("`", "") + "§a has been spawned!");
			PlayerPet.addNPC(p, npc);
		}
		else if(args.length == 3){
			NPC npc = spawnNpc(p, args[1], args[2]);
			MessageManager.sendMessage(p, args[1].replace('&', '§').replace('-', ' ').replaceAll("`", "") + "§a has been spawned with the skin of " + args[2] + "!");
			PlayerPet.TeamNpcNameList.add(args[1].replace('&', '§').replace('-', ' ').replaceAll("`", ""));
			PlayerPet.addNPC(p, npc);
		}
		else{
			PetCommand.showHelpMenu(p);
		}
		return false;
	}
	
	public NPC spawnNpc(final Player p, String name, String skin){
		final NPCFactory factory = new NPCFactory(plugin);
		final NPCProfile profile = new NPCProfile(name.replace('&', '§').replace('-', ' ').replaceAll("`", ""), skin);
	    final NPC npc = factory.spawnHumanNPC(p.getLocation(), profile);
		new Thread() {
		    @Override
		    public void run() {
		        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){
		        	@SuppressWarnings("deprecation")
					public void run(){
		        		if(npc.getBukkitEntity().getPassenger() != null && npc.getBukkitEntity().getPassenger() == p){
		        			npc.setYaw(p.getLocation().getYaw());
		        			npc.setPitch(p.getLocation().getPitch());
		        		}else if(p.getItemInHand().getType() == Material.STICK){
		        			npc.lookAt(p.getTargetBlock(null, 100).getLocation());
		        		}else
		        			npc.lookAt(p.getLocation());
		        	}}, 0l, 1l);
		        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){
		        	@SuppressWarnings("deprecation")
					public void run(){
		        		if(npc.getBukkitEntity().getPassenger() != null && npc.getBukkitEntity().getPassenger() == p || p.getItemInHand().getType() == Material.STICK){
		        			if(!npc.getBukkitEntity().getLocation().equals(p.getTargetBlock(null, 100).getLocation()))
		        			npc.pathfindTo(p.getTargetBlock(null, 100).getLocation(), .2, 50);
		        		}
		        		else
		        			if(!npc.getBukkitEntity().getLocation().equals(p.getLocation()))
		        			npc.pathfindTo(p.getLocation(), .2, 50);
		        	}}, 0l, 30l);
		    }
		}.start();
		return npc;
	}
	
	public boolean removePet(final Player p, Command d, String c, final String[] args) {
	
		if(args.length == 1){
			MessageManager.sendMessage(p, "PlayerPet removed!");
			
			if (Bukkit.getScoreboardManager().getMainScoreboard().getTeam(PlayerPet.getNPC(p).getBukkitEntity().getName()) != null){
				Bukkit.getScoreboardManager().getMainScoreboard().getTeam(PlayerPet.getNPC(p).getBukkitEntity().getName()).setPrefix("");
				Bukkit.getScoreboardManager().getMainScoreboard().getTeam(PlayerPet.getNPC(p).getBukkitEntity().getName()).setSuffix("");
			}
			PlayerPet.removeNPC(p);
		}else{
			PetCommand.showHelpMenu(p);
		}
		
		return false;
	}

}
