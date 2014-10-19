package com.phase.playerpet.cmd;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.phase.playerpet.main.MessageManager;
import com.phase.playerpet.main.PlayerPet;

public class PrefixPet {

	public boolean prefixPet(Player p, String[] args) {
		if(PlayerPet.hasNPC(p)){
			Player npc = PlayerPet.getNPC(p).getBukkitEntity();
			Scoreboard s = Bukkit.getScoreboardManager().getMainScoreboard();
			String teamName = args[1].replace('&', '§').replace('-', ' ');
			npc.setScoreboard(s);
			if(s.getTeam(npc.getName()) == null)
				s.registerNewTeam(npc.getName());
			Team t = s.getTeam(npc.getName());
				if(teamName.length() < 13){
					t.setPrefix(teamName + " §r");
					t.addPlayer(npc);
					MessageManager.sendMessage(p, "Prefix " + teamName + " has been added to " + npc.getName() + "!");
				}
				else
					MessageManager.sendMessage(p, "The prefix must be less than 13 characters!");
			return true;
		}
		else{
			MessageManager.sendMessage(p, "You don't have a PlayerPet!");
			return false;
		}
	}

	public boolean suffixPet(Player p, String[] args) {
		if(PlayerPet.hasNPC(p)){
			Player npc = PlayerPet.getNPC(p).getBukkitEntity();
			Scoreboard s = Bukkit.getScoreboardManager().getMainScoreboard();
			String teamName = args[1].replace('&', '§').replace('-', ' ');
			npc.setScoreboard(s);
			if(s.getTeam(npc.getName()) == null)
				s.registerNewTeam(npc.getName());
			Team t = s.getTeam(npc.getName());
				if(teamName.length() < 13){
					t.setSuffix("§r " + teamName);
					t.addPlayer(npc);
					MessageManager.sendMessage(p, "Suffix " + teamName + " has been added to " + npc.getName() + "!");
				}
				else
					MessageManager.sendMessage(p, "The suffix must be less than 13 characters!");
			return true;
		}
		else{
			MessageManager.sendMessage(p, "You don't have a PlayerPet!");
			return false;
		}
	}

}
