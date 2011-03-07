package com.firestar.animate;

import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
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
    public Hashtable<String, frameset> animations = new Hashtable<String, frameset>();
    public Hashtable<String, Boolean> animations_edit = new Hashtable<String, Boolean>();
    public Hashtable<String, Boolean> animations_playing = new Hashtable<String, Boolean>();
    public Hashtable<String, Boolean> animations_repeat = new Hashtable<String, Boolean>();
    public Hashtable<String, String> open_animations = new Hashtable<String, String>();
    public Hashtable<String, Hashtable<Integer, Location>> player_pos = new Hashtable<String, Hashtable<Integer, Location>>();
    public Hashtable<String, Hashtable<Location, Byte>> animations_last_frame_byte = new Hashtable<String, Hashtable<Location, Byte>>();
    public Hashtable<String, Hashtable<Location, Material>> animations_last_frame_type = new Hashtable<String, Hashtable<Location, Material>>();
    public Hashtable<String, Hashtable<Integer, Location>> animations_save_locations = new Hashtable<String, Hashtable<Integer, Location>>();

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
        String Sender_Name = player.getName();
        if (commandName.equalsIgnoreCase("cra")) {
            if (hasOpenAnimation(Sender_Name)) {
                player.sendMessage("Already have animation open!");
            } else {
                if (args.length == 1) {
                    if (!animationExists(args[0])) {
                        World Players_World = player.getWorld();
                        Hashtable<Integer, Location> block_positions = new Hashtable<Integer, Location>();
                        frameset t = new frameset(args[0], this, Players_World);
                        animations.put(args[0], t);
                        animations_save_locations.put(args[0], block_positions);
                        openAnimation(Sender_Name, args[0]);
                        animations_edit.put(args[0], true);
                        animations_playing.put(args[0], false);
                        animations_repeat.put(args[0], false);
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
            if (!hasOpenAnimation(Sender_Name)) {
                player.sendMessage("Animation is not open!");
            } else {
                closeAnimation(Sender_Name);
                player.sendMessage("Animation  closed!");
            }
            return true;
        } else if (commandName.equalsIgnoreCase("opa")) {
            if (hasOpenAnimation(Sender_Name)) {
                player.sendMessage("Animation already open, please close it!");
            } else {
                if (args.length == 1) {
                    if (animationExists(args[0])) {
                        openAnimation(Sender_Name, args[0]);
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
            if (hasOpenAnimation(Sender_Name)) {
                String open_anime = getOpenAnimation(Sender_Name);
                if (!isAnimationSet(open_anime)) {
                    if (player_pos.containsKey(Sender_Name)) {
                        Hashtable<Integer, Location> jsu = player_pos.get(Sender_Name);
                        if (jsu.containsKey(0) && jsu.containsKey(1)) {
                            area j = new area(this, player.getWorld(), jsu.get(0), jsu.get(1));
                            animations_save_locations.put(open_anime, j.get_blocks());
                            setAnimation(open_anime);
                            player.sendMessage("Animation is now set, use saf to set frame!" + j.get_blocks().size());
                        } else {
                            player.sendMessage("select the positions!");
                        }
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
            if (hasOpenAnimation(Sender_Name)) {
                String open_anime = getOpenAnimation(Sender_Name);
                if (isAnimationSet(open_anime)) {
                    frameset this_frameset = getFrameset(open_anime);
                    if (this_frameset.frames.size() == 0) {
                        Hashtable<Integer, Block> blocks = new Hashtable<Integer, Block>();
                        for (Entry<Integer, Location> entry : animations_save_locations.get(open_anime).entrySet()) {
                            blocks.put(blocks.size(), this_frameset.this_world.getBlockAt(entry.getValue()));
                        }
                        this_frameset.add_frame(blocks);
                        animations_last_frame_byte.put(open_anime, this_frameset.frames.get((this_frameset.frames.size() - 1)).frame_blocks_data);
                        animations_last_frame_type.put(open_anime, this_frameset.frames.get((this_frameset.frames.size() - 1)).frame_blocks_type);
                        player.sendMessage("Initial frame saved: " + blocks.size() + " Saved Frame: " + this_frameset.frames.size());
                    } else {
                        Hashtable<Integer, Block> blocks = new Hashtable<Integer, Block>();
                        Hashtable<Location, Material> jprevtype = animations_last_frame_type.get(open_anime);
                        Hashtable<Location, Byte> jprevbyte = animations_last_frame_byte.get(open_anime);
                        for (Entry<Integer, Location> entry : animations_save_locations.get(open_anime).entrySet()) {
                            if (this_frameset.this_world.getBlockAt(entry.getValue()).getType() != jprevtype.get(entry.getValue())) {
                                blocks.put(blocks.size(), this_frameset.this_world.getBlockAt(entry.getValue()));
                                jprevtype.put(entry.getValue(), this_frameset.this_world.getBlockAt(entry.getValue()).getType());
                                jprevbyte.put(entry.getValue(), this_frameset.this_world.getBlockAt(entry.getValue()).getData());
                            } else if (this_frameset.this_world.getBlockAt(entry.getValue()).getData() != jprevbyte.get(entry.getValue())) {
                                blocks.put(blocks.size(), this_frameset.this_world.getBlockAt(entry.getValue()));
                                jprevbyte.put(entry.getValue(), this_frameset.this_world.getBlockAt(entry.getValue()).getData());
                            }
                        }
                        this_frameset.add_frame(blocks);
                        animations_last_frame_byte.put(open_anime, jprevbyte);
                        animations_last_frame_type.put(open_anime, jprevtype);
                        player.sendMessage("blocks set to frame! changes: " + blocks.size() + " Saved Frame: " + this_frameset.frames.size());
                    }
                    animations.put(open_anime, this_frameset);
                } else {
                    player.sendMessage("Animation not set!");
                }
            } else {
                player.sendMessage("No open animation!");
            }
            return true;
        } else if (commandName.equalsIgnoreCase("play")) {
            if (hasOpenAnimation(Sender_Name)) {
                String open_anime = getOpenAnimation(Sender_Name);
                if (isAnimationSet(open_anime)) {
                    if (!isPlaying(open_anime)) {
                        Thread animation_player = null;
                        if (args.length == 1) {
                            if (args[0].equalsIgnoreCase("t")) {
                                animations_repeat.put(open_anime, true);
                                animation_player = new play(this, getFrameset(open_anime), open_anime);
                            } else {
                                animations_repeat.put(open_anime, false);
                                animation_player = new play(this, getFrameset(open_anime), open_anime, Integer.valueOf(args[0]));
                            }
                        } else if (args.length == 2) {
                            if (args[1].equalsIgnoreCase("t")) {
                                animations_repeat.put(open_anime, true);
                            }
                            animation_player = new play(this, getFrameset(open_anime), open_anime, Integer.valueOf(args[0]));
                        } else {
                            animations_repeat.put(open_anime, false);
                            animation_player = new play(this, getFrameset(open_anime), open_anime);
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
            if (hasOpenAnimation(Sender_Name)) {
                String open_anime = getOpenAnimation(Sender_Name);
                if (isAnimationSet(open_anime)) {
                    if (isPlaying(open_anime)) {
                        if (animations_repeat.get(open_anime)) {
                            player.sendMessage("Repeat turned off!");
                            animations_repeat.put(open_anime, false);
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
            if (hasOpenAnimation(Sender_Name)) {
                String open_anime = getOpenAnimation(Sender_Name);
                if (isAnimationSet(open_anime)) {
                    if (args.length == 1) {
                        if (args[0].equalsIgnoreCase("lt")) {
                            frameset g = getFrameset(open_anime);
                            Integer hi = 0;
                            while (hi < g.frames.size()) {
                                g.gt(hi);
                                hi++;
                            }
                            player.sendMessage("frame " + g.frames.size() + "!");
                        } else if (args[0].equalsIgnoreCase("ft")) {
                            frameset g = getFrameset(open_anime);
                            g.gt(0);
                            player.sendMessage("frame 1!");
                        } else if (is_integer(args[0])) {
                            frameset g = getFrameset(open_anime);
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

    public boolean is_integer(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * @param animationName
     * @return
     */
    frameset getFrameset(String animationName) {
        return animations.get(animationName);
    }

    private Boolean setAnimation(String animationName) {
        return animations_edit.put(animationName, false);
    }

    /**
     * @param animationName
     * @return
     */
    boolean isAnimationSet(String animationName) {
        return !animations_edit.get(animationName);
    }

    /**
     * @param player
     * @param animationName
     * @return
     */
    String openAnimation(String player, String animationName) {
        return open_animations.put(player, animationName);
    }

    /**
     * @param player
     * @return
     */
    String closeAnimation(String player) {
        return open_animations.remove(player);
    }

    /**
     * @param animationName
     * @return
     */
    boolean animationExists(String animationName) {
        return animations.containsKey(animationName);
    }

    /**
     * @param animationName
     * @return
     */
    boolean isPlaying(String animationName) {
        return animations_playing.get(animationName);
    }

    /**
     * @param player
     * @return
     */
    boolean hasOpenAnimation(String player) {
        return open_animations.containsKey(player);
    }

    /**
     * @param player
     * @return
     */
    String getOpenAnimation(String player) {
        return open_animations.get(player);
    }
}
