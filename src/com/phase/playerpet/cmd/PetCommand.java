package com.phase.playerpet.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.phase.playerpet.main.MessageManager;

public class PetCommand implements CommandExecutor {
	
	private SpawnPet spawnPet = new SpawnPet();
	private PrefixPet prefix = new PrefixPet();
	
	@Override
	public boolean onCommand(CommandSender sender, Command d, String c, String[] args) {
		if(!(sender instanceof Player)){
			System.out.println("You have to be a player to do this command!");
			return false;
		}
		Player p = (Player) sender;
		if(args.length == 0){
			showHelpMenu(p);
			return false;
		}
		
		if(args[0].equalsIgnoreCase("spawn"))
			if(p.hasPermission("playerpet") || p.isOp())
				return spawnPet.spawnPet(p, d, c, args);
			else
				return MessageManager.sendMessage(p, "You aren't allowed to do that!");
		if(args[0].equalsIgnoreCase("remove"))
			if(p.hasPermission("playerpet") || p.isOp())
				return spawnPet.removePet(p, d, c, args);
			else
				return MessageManager.sendMessage(p, "You aren't allowed to do that!");
		if(args[0].equalsIgnoreCase("prefix"))
			if(p.hasPermission("playerpet") || p.isOp())
				return prefix.prefixPet(p, args);
		if(args[0].equalsIgnoreCase("suffix"))
			if(p.hasPermission("playerpet") || p.isOp())
				return prefix.suffixPet(p, args);
			else 		
				return MessageManager.sendMessage(p, "You aren't allowed to do that!");
		showHelpMenu(p);
		
		return false;
	}
	
	public static void showHelpMenu(Player p){
		p.sendMessage("§b►►►►►►►►§ePlayerPet!§b►►►►►►►►►►");
		p.sendMessage("§e/playerpet spawn <name> [skin]");
		p.sendMessage("§e/playerpet remove");
		p.sendMessage("§e/playerpet prefix <prefix>");
		p.sendMessage("§e/playerpet suffix <suffix>");
		p.sendMessage("§b►►►►►►►►►►►►►►►►►►►►►►►►►►►►►►►►");
	
	}

}