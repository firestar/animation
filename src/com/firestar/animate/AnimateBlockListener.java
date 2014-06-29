package com.firestar.animate;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class AnimateBlockListener implements Listener {
    private Animate p = null;

    public AnimateBlockListener(Animate plugin) {
        p = plugin;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockRightClick(PlayerInteractEvent event) {
        if (event.getPlayer().getItemInHand().getType() == Material.WOOD_HOE && event.getAction()==Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            Animator animator = p.getAnimator(player.getName());
            if (!animator.hasOpenAnimation()) {
                player.sendMessage("No animation selected");
            } else {
                Animation open_anime = animator.getOpenAnimation();
                if (!open_anime.isAreaSet()) {
                    animator.setLoc2(event.getClickedBlock().getLocation());
                    player.sendMessage("Position 2 saved!");
                }
            }
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockDamage(PlayerInteractEvent event) {
        if (event.getPlayer().getItemInHand().getType() == Material.WOOD_HOE && event.getAction()==Action.LEFT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            Animator animator = p.getAnimator(player.getName());
            if (!animator.hasOpenAnimation()) {
                player.sendMessage("No animation selected");
            } else {
                Animation open_anime = animator.getOpenAnimation();
                if (!open_anime.isAreaSet()) {
                    animator.setLoc1(event.getClickedBlock().getLocation());
                    player.sendMessage("Position 1 saved!");
                }
            }
            event.setCancelled(true);
        }
    }
}
