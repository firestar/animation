package com.firestar.animate;
import java.util.Hashtable;

import org.bukkit.Location;
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
		if(event.getPlayer().getItemInHand().getTypeId()==290){
			Player player = event.getPlayer();
			String Sender_Name = player.getName();
			if(!p.open_animations.containsKey(Sender_Name)){
				player.sendMessage("No animation selected");
			}else{
				String open_anime=p.open_animations.get(Sender_Name);
				if(p.animations_edit.get(open_anime)){
					/*Location block_pos = event.getBlock().getLocation();
					Hashtable<Integer,Location> k = p.animations_save_locations.get(open_anime);
					if(!k.containsValue(block_pos)){
						k.put(k.size(), block_pos);
						player.sendMessage("Added block to animation!");
					}
					p.animations_save_locations.put(open_anime,k);*/
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
		if(event.getPlayer().getItemInHand().getTypeId()==290){
			Player player = event.getPlayer();
			String Sender_Name = player.getName();
			if(!p.open_animations.containsKey(Sender_Name)){
				player.sendMessage("No animation selected");
			}else{
				String open_anime=p.open_animations.get(Sender_Name);
				if(p.animations_edit.get(open_anime)){
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