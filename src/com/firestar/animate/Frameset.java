package com.firestar.animate;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Hashtable;
import java.util.Map.Entry;

import org.bukkit.World;
import org.bukkit.block.Block;

class Frameset {
    private Animate p = null;
    public World this_world;
    public Hashtable<Integer, Frame> frames = new Hashtable<Integer, Frame>();
    public Integer on_time = 0;
    public String frameset_name = "";

    public Frameset(String name, Animate main_plugin, World world) {
        frameset_name = name;
        p = main_plugin;
        this_world = world;
    }

    public void add_frame(Hashtable<Integer, Block> blocks) {
        Frame g = new Frame(p, this_world);
        for (Entry<Integer, Block> entry : blocks.entrySet()) {
            g.add_block(entry.getValue());
        }
        frames.put(frames.size(), g);
    }

    public void output(Integer num, Frame frame_blocks) {
        /*
         * try{ FileWriter fstream = new FileWriter("out_frame_"+num+"_data.txt"); BufferedWriter out = new BufferedWriter(fstream);
         * out.write(frame_blocks.frame_blocks_data.toString()); out.close(); }catch (Exception e){//Catch exception if any
         * System.err.println("Error: " + e.getMessage()); } try{ FileWriter fstream = new FileWriter("out_frame_"+num+"_type.txt");
         * BufferedWriter out = new BufferedWriter(fstream); out.write(frame_blocks.frame_blocks_type.toString()); out.close(); }catch
         * (Exception e){//Catch exception if any System.err.println("Error: " + e.getMessage()); }
         */
    }

    public boolean first() {
        Frame this_frame = null;
        on_time = 0;
        this_frame = frames.get(0);
        output(0, this_frame);
        p.getServer().broadcastMessage("Frame 1: " + this_frame.frame_blocks_data.size() + " Blocks");
        this_frame.draw();
        return true;
    }

    public boolean next() {
        Frame this_frame = null;
        if (on_time + 1 < frames.size()) {
            ++on_time;
            this_frame = frames.get(on_time);
            output(on_time, this_frame);
            p.getServer().broadcastMessage("Frame " + (on_time + 1) + ": " + this_frame.frame_blocks_data.size() + " Blocks");
            this_frame.draw();
            return true;
        } else {
            return false;
        }
    }

    public boolean gt(Integer framenum) {
        Frame this_frame = null;
        this_frame = frames.get(framenum);
        output(framenum, this_frame);
        p.getServer().broadcastMessage("Frame " + (framenum + 1) + ": " + this_frame.frame_blocks_data.size() + " Blocks");
        this_frame.draw();
        return true;
    }

    public boolean prev() {
        Frame this_frame = null;
        if ((on_time - 1) >= 0) {
            --on_time;
            this_frame = frames.get(on_time);
            p.getServer().broadcastMessage("Frame " + on_time + ": " + this_frame.frame_blocks_data.size() + " Blocks");
            this_frame.draw();
            return true;
        } else {
            return false;
        }
    }
}
