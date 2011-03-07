package com.firestar.animate;
import java.util.Hashtable;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockRightClickEvent;

public class blocklistener extends BlockListener {
	private animate p=null;
	public blocklistener(animate plugin){
		p = plugin;
	}
	public void onBlockRightClick(BlockRightClickEvent event) {
		if(event.getPlayer().getItemInHand().getType()==Material.WOOD_HOE){
			Player player = event.getPlayer();
			String Sender_Name = player.getName();
			if(!p.hasOpenAnimation(Sender_Name)){
				player.sendMessage("No animation selected");
			}else{
				String open_anime=p.getOpenAnimation(Sender_Name);
				if(!p.isAnimationSet(open_anime)){
					if(p.player_pos.containsKey(Sender_Name)){
						Hashtable<Integer,Location> jsu = p.player_pos.get(Sender_Name);
						jsu.put(1, event.getBlock().getLocation());
						p.player_pos.put(Sender_Name,jsu);
					}else{
						Hashtable<Integer,Location> jsu = new Hashtable<Integer,Location>();
						jsu.put(1, event.getBlock().getLocation());
						p.player_pos.put(Sender_Name,jsu);
					}
					player.sendMessage("Position 2 saved!");
				}
			}
		}
    }
	public void onBlockDamage(BlockDamageEvent event) {
		if(event.getPlayer().getItemInHand().getType()==Material.WOOD_HOE){
			Player player = event.getPlayer();
			String Sender_Name = player.getName();
			if(!p.hasOpenAnimation(Sender_Name)){
				player.sendMessage("No animation selected");
			}else{
				String open_anime=p.getOpenAnimation(Sender_Name);
				if(!p.isAnimationSet(open_anime)){
					if(p.player_pos.containsKey(Sender_Name)){
						Hashtable<Integer,Location> jsu = p.player_pos.get(Sender_Name);
						jsu.put(0, event.getBlock().getLocation());
						p.player_pos.put(Sender_Name,jsu);
					}else{
						Hashtable<Integer,Location> jsu = new Hashtable<Integer,Location>();
						jsu.put(0, event.getBlock().getLocation());
						p.player_pos.put(Sender_Name,jsu);
					}
					player.sendMessage("Position 1 saved!");
				}
			}
		}
    }
}