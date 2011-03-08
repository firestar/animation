/**
 * 
 */
package com.firestar.animate;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;

/**
 * An animation of MineCraft blocks.
 * Includes the frame set that describes each frame of the animation, 
 * where the animation is located and some information about how it should be played.
 * 
 * @author Cogito
 *
 */
public class Animation {
    private frameset frames;
    private boolean playing;
    private boolean repeat;
    private boolean areaSet;
    private Area area;
    private Map<Location, Byte> lastFrameBytes;
    private Map<Location, Material> lastFrameTypes;

    public Animation() {
        this(false);
    }
    public Animation(boolean repeat) {
        super();
        this.playing = false;
        this.repeat  = repeat;
        this.areaSet = false;
    }
    /**
     * @return if the animation is playing
     */
    public final boolean isPlaying() {
        return playing;
    }
    /**
     * Start the animation.
     */
    public final void onStart() {
        this.playing = true;
    }
    /**
     * Stop the animation.
     */
    public final void onStop() {
        this.playing = false;
    }
    /**
     * @return if the animation is repeating
     */
    public final boolean isRepeat() {
        return repeat;
    }
    /**
     * @param repeat set if the animation should repeat
     */
    public final void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }
    /**
     * @return if the location of the animation is set
     */
    public final boolean isAreaSet() {
        return areaSet;
    }
    /**
     * @param area the area to set
     */
    public final void setArea(Area area) {
        if(!areaSet) {
            this.area = area;
            this.areaSet = true;
        }
    }
    /**
     * @return the area
     */
    public final Area getArea() {
        return area;
    }
    /**
     * @return the frame set that contains all the frames for this animation/
     */
    public final frameset getFrames() {
        return frames;
    }
    public void setLastFrameBytes(Map<Location, Byte> lastFrameBytes) {
        this.lastFrameBytes = lastFrameBytes;
    }
    public Map<Location, Byte> getLastFrameBytes() {
        return lastFrameBytes;
    }
    public void setLastFrameTypes(Map<Location, Material> lastFrameTypes) {
        this.lastFrameTypes = lastFrameTypes;
    }
    public Map<Location, Material> getLastFrameTypes() {
        return lastFrameTypes;
    }

}
