package com.firestar.animate;

import java.util.Hashtable;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

class frame {
    private World this_world = null;
    public Hashtable<Location, Material> frame_blocks_type = new Hashtable<Location, Material>();
    public Hashtable<Location, Byte> frame_blocks_data = new Hashtable<Location, Byte>();

    public frame(animate main_plugin, World world) {
        this_world = world;
    }

    public void add_block(Block block) {
        frame_blocks_data.put(block.getLocation(), block.getData());
        frame_blocks_type.put(block.getLocation(), block.getType());
    }

    public void draw() {

        for (Entry<Location, Material> entry : frame_blocks_type.entrySet()) {
            if (this_world.getBlockAt(entry.getKey()).getType() != entry.getValue()) {
                this_world.getBlockAt(entry.getKey()).setType(entry.getValue());
            }
        }
        for (Entry<Location, Byte> entry : frame_blocks_data.entrySet()) {
            if (this_world.getBlockAt(entry.getKey()).getData() != entry.getValue()) {
                this_world.getBlockAt(entry.getKey()).setData(entry.getValue());
            }
        }
    }
}
