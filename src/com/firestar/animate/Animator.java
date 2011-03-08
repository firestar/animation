/**
 * 
 */
package com.firestar.animate;

import org.bukkit.Location;

/**
 * An animator creates and edits animations.
 * 
 * They are able to specify where an animation should be created, and can add frames to an animations frameset.
 * @author Cogito
 *
 */
public class Animator {
    private Location loc1;
    private Location loc2;
    private Animation openAnimation;
    
    public Animator() {
        super();
    }

    public boolean hasOpenAnimation() {
        return openAnimation != null;
    }

    public Animation getOpenAnimation() {
        return openAnimation;
    }

    public void closeAnimation() {
        openAnimation = null;
    }

    public void openAnimation(Animation animation) {
        if (hasOpenAnimation()) {
            return;
        } else {
            openAnimation = animation;
        }
    }

    /**
     * @return the first location
     */
    public final Location getLoc1() {
        return loc1;
    }

    /**
     * @param location the first location
     */
    public final void setLoc1(Location location) {
        this.loc1 = location;
    }

    /**
     * @return the second location
     */
    public final Location getLoc2() {
        return loc2;
    }

    /**
     * @param location the second location
     */
    public final void setLoc2(Location location) {
        this.loc2 = location;
    }

    public boolean locationsSet() {
        return loc1 != null && loc2 != null;
    }
}
