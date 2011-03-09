package com.firestar.animate;

import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class animate extends JavaPlugin {
    public Logger log = Logger.getLogger("Minecraft");
    private final blocklistener blockListener = new blocklistener(this);
    private Map<String, Animation> animations = new Hashtable<String, Animation>();
    private Map<String, Animator> animators = new Hashtable<String, Animator>();

    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.BLOCK_RIGHTCLICKED, blockListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_DAMAGED, blockListener, Priority.Normal, this);
    }

    public void onDisable() {

    }

    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        Player player = null;
        String commandName = command.getName();
        if (sender instanceof Player) {
            player = (Player) sender;
        } else {
            log.info("Cannot execute any commands from console for this plugin :(");
            return false;
        }
        Animator animator = getAnimator(player.getName());
        if (commandName.equalsIgnoreCase("cra")) {
            if (animator.hasOpenAnimation()) {
                player.sendMessage("Already have animation open!");
            } else {
                if (args.length == 1) {
                    Animation animation = getAnimation(args[0]);
                    if (animation == null) {
                        createNewAnimation(args[0], player);
                        player.sendMessage("Animation created!");
                    } else {
                        player.sendMessage("Animation with that name already exists!");
                    }
                } else {
                    player.sendMessage("Incorrect Animation Name!");
                }
            }
            return true;
        } else if (commandName.equalsIgnoreCase("cla")) {
            if (!animator.hasOpenAnimation()) {
                player.sendMessage("Animation is not open!");
            } else {
                animator.closeAnimation();
                player.sendMessage("Animation  closed!");
            }
            return true;
        } else if (commandName.equalsIgnoreCase("opa")) {
            if (animator.hasOpenAnimation()) {
                player.sendMessage("Animation already open, please close it!");
            } else {
                if (args.length == 1) {
                    Animation animation = getAnimation(args[0]);
                    if (animation == null) {
                        animator.openAnimation(animation);
                        player.sendMessage("Animation opened!");
                    } else {
                        player.sendMessage("Animation with that name does not exist!");
                    }
                } else {
                    player.sendMessage("Incorrect Animation Name!");
                }
            }
            return true;
        } else if (commandName.equalsIgnoreCase("sea")) {
            if (animator.hasOpenAnimation()) {
                Animation open_anime = animator.getOpenAnimation();
                if (!open_anime.isAreaSet()) {
                    if (animator.locationsSet()) {
                        Area area = new Area(this, player.getWorld(), animator.getLoc1(), animator.getLoc2());
                        open_anime.setArea(area);
                        player.sendMessage("Animation is now set, use saf to set frame! " + area.get_blocks().size());
                        /*
                        Hashtable<Integer, Location> jsu = player_pos.get(Sender_Name);
                        if (jsu.containsKey(0) && jsu.containsKey(1)) {
                            area j = new area(this, player.getWorld(), jsu.get(0), jsu.get(1));
                            animations_save_locations.put(open_anime, j.get_blocks());
                            setAnimation(open_anime);
                            player.sendMessage("Animation is now set, use saf to set frame!" + j.get_blocks().size());
                        } else {
                            player.sendMessage("select the positions!");
                        }
                        */
                    } else {
                        player.sendMessage("select the positions!");
                    }
                } else {
                    player.sendMessage("Animation already set!");
                }
            } else {
                player.sendMessage("No open animation!");
            }
            return true;
        } else if (commandName.equalsIgnoreCase("saf")) {
            if (animator.hasOpenAnimation()) {
                Animation open_anime = animator.getOpenAnimation();
                if (open_anime.isAreaSet()) {
                    frameset this_frameset = open_anime.getFrames();
                    if (this_frameset.frames.size() == 0) {
                        Hashtable<Integer, Block> blocks = new Hashtable<Integer, Block>();
                        for (Location location : open_anime.getArea().get_blocks()) {
                            blocks.put(blocks.size(), this_frameset.this_world.getBlockAt(location));
                        }
                        this_frameset.add_frame(blocks);
                        // TODO this should be unnecessary = just access the last frame of the framset when needed
                        open_anime.setLastFrameBytes(this_frameset.frames.get((this_frameset.frames.size() - 1)).frame_blocks_data);
                        open_anime.setLastFrameTypes(this_frameset.frames.get((this_frameset.frames.size() - 1)).frame_blocks_type);
                        player.sendMessage("Initial frame saved: " + blocks.size() + " Saved Frame: " + this_frameset.frames.size());
                    } else {
                        Hashtable<Integer, Block> blocks = new Hashtable<Integer, Block>();
                        Map<Location, Material> jprevtype = open_anime.getLastFrameTypes();
                        Map<Location, Byte> jprevbyte = open_anime.getLastFrameBytes();
                        for (Location location : open_anime.getArea().get_blocks()) {
                            if (this_frameset.this_world.getBlockAt(location).getType() != jprevtype.get(location)) {
                                blocks.put(blocks.size(), this_frameset.this_world.getBlockAt(location));
                                jprevtype.put(location, this_frameset.this_world.getBlockAt(location).getType());
                                jprevbyte.put(location, this_frameset.this_world.getBlockAt(location).getData());
                            } else if (this_frameset.this_world.getBlockAt(location).getData() != jprevbyte.get(location)) {
                                blocks.put(blocks.size(), this_frameset.this_world.getBlockAt(location));
                                jprevbyte.put(location, this_frameset.this_world.getBlockAt(location).getData());
                            }
                        }
                        this_frameset.add_frame(blocks);
                        open_anime.setLastFrameBytes(jprevbyte);
                        open_anime.setLastFrameTypes(jprevtype);
                        player.sendMessage("blocks set to frame! changes: " + blocks.size() + " Saved Frame: " + this_frameset.frames.size());
                    }
                    //animation_frame_sets.put(open_anime, this_frameset); // unnecessary because the frame set should be modified itself - doesn't need to be readded to the map
                } else {
                    player.sendMessage("Animation not set!");
                }
            } else {
                player.sendMessage("No open animation!");
            }
            return true;
        } else if (commandName.equalsIgnoreCase("play")) {
            if (animator.hasOpenAnimation()) {
                Animation open_anime = animator.getOpenAnimation();
                if (open_anime.isAreaSet()) {
                    if (!open_anime.isPlaying()) {
                        Thread animation_player = null;
                        if (args.length == 1) {
                            if (args[0].equalsIgnoreCase("t")) {
                                open_anime.setRepeat(true);
                                animation_player = new play(this, open_anime);
                            } else {
                                open_anime.setRepeat(false);
                                animation_player = new play(this, open_anime, Integer.valueOf(args[0]));
                            }
                        } else if (args.length == 2) {
                            if (args[1].equalsIgnoreCase("t")) {
                                open_anime.setRepeat(true);
                            }
                            animation_player = new play(this, open_anime, Integer.valueOf(args[0]));
                        } else {
                            open_anime.setRepeat(false);
                            animation_player = new play(this, open_anime);
                        }
                        animation_player.start();
                        player.sendMessage("Now Playing!");
                    } else {
                        player.sendMessage("Already playing!");
                    }
                } else {
                    player.sendMessage("Animation not set!");
                }
            } else {
                player.sendMessage("No open animation!");
            }
            return true;
        } else if (commandName.equalsIgnoreCase("stp")) {
            if (animator.hasOpenAnimation()) {
                Animation open_anime = animator.getOpenAnimation();
                if (open_anime.isAreaSet()) {
                    if (open_anime.isPlaying()) {
                        if (open_anime.isRepeat()) {
                            player.sendMessage("Repeat turned off!");
                            open_anime.setRepeat(false);
                        } else {
                            player.sendMessage("Repeat already off!");
                        }
                    } else {
                        player.sendMessage("Not running!");
                    }
                } else {
                    player.sendMessage("Animation not set!");
                }
            } else {
                player.sendMessage("No open animation!");
            }
            return true;
        } else if (commandName.equalsIgnoreCase("gtf")) {
            if (animator.hasOpenAnimation()) {
                Animation open_anime = animator.getOpenAnimation();
                if (open_anime.isAreaSet()) {
                    if (args.length == 1) {
                        if (args[0].equalsIgnoreCase("lt")) {
                            frameset g = open_anime.getFrames();
                            Integer hi = 0;
                            while (hi < g.frames.size()) {
                                g.gt(hi);
                                hi++;
                            }
                            player.sendMessage("frame " + g.frames.size() + "!");
                        } else if (args[0].equalsIgnoreCase("ft")) {
                            frameset g = open_anime.getFrames();
                            g.gt(0);
                            player.sendMessage("frame 1!");
                        } else if (is_integer(args[0])) {
                            frameset g = open_anime.getFrames();
                            if (g.frames.size() >= Integer.valueOf(args[0])) {
                                Integer hi = 0;
                                while (hi < Integer.valueOf(args[0])) {
                                    g.gt(hi);
                                    hi++;
                                }
                                player.sendMessage("frame " + (Integer.valueOf(args[0]) + 1) + "!");
                            } else {
                                player.sendMessage("No such frame!");
                            }
                        } else {
                            player.sendMessage("Incorrect Input!");
                        }
                    } else {
                        player.sendMessage("Specify a frame!");
                    }
                } else {
                    player.sendMessage("Animation not set!");
                }
            } else {
                player.sendMessage("No open animation!");
            }
            return true;
        }
        return false;
    }

    /**
     * The animation with this name.
     * @param name The name of the animation.
     * @return null if the animation does not yet exist.
     */
    public Animation getAnimation(String name) {
        return animations.get(name);
    }

    /**
     * The animator with this name.
     * @param name The name of the animator.
     * @return The animator, or a new animator if it does not yet exist.
     */
    public Animator getAnimator(String name) {
        Animator animator = animators.get(name);
        if (animator == null) {
            animator = new Animator();
            animators.put(name, animator);
        }
        return animator;
    }

    /**
     * 
     * WARNING: currently will override any existing animation of the same name.
     * @param name
     * @param player
     */
    private void createNewAnimation(String name, Player player) {
        Animation animation = new Animation(new frameset(name, this, player.getWorld()));
        getAnimator(player.getName()).openAnimation(animation);
        animations.put(name, animation);
    }

    public boolean is_integer(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    
}
