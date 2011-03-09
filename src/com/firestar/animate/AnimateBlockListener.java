package com.firestar.animate;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockRightClickEvent;

public class AnimateBlockListener extends BlockListener {
    private Animate p = null;

    public AnimateBlockListener(Animate plugin) {
        p = plugin;
    }

    public void onBlockRightClick(BlockRightClickEvent event) {
        if (event.getPlayer().getItemInHand().getType() == Material.WOOD_HOE) {
            Player player = event.getPlayer();
            Animator animator = p.getAnimator(player.getName());
            if (!animator.hasOpenAnimation()) {
                player.sendMessage("No animation selected");
            } else {
                Animation open_anime = animator.getOpenAnimation();
                if (!open_anime.isAreaSet()) {
                    animator.setLoc2(event.getBlock().getLocation());
                    player.sendMessage("Position 2 saved!");
                }
            }
        }
    }

    public void onBlockDamage(BlockDamageEvent event) {
        if (event.getPlayer().getItemInHand().getType() == Material.WOOD_HOE) {
            Player player = event.getPlayer();
            Animator animator = p.getAnimator(player.getName());
            if (!animator.hasOpenAnimation()) {
                player.sendMessage("No animation selected");
            } else {
                Animation open_anime = animator.getOpenAnimation();
                if (!open_anime.isAreaSet()) {
                    animator.setLoc1(event.getBlock().getLocation());
                    player.sendMessage("Position 1 saved!");
                }
            }
        }
    }
}
