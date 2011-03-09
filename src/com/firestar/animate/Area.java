package com.firestar.animate;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;

class Area {
    private World this_world = null;
    private Location location1 = null;
    private Location location2 = null;
    private List<Location> blocks;

    public Area(Animate main_plugin, World world, Location loc1, Location loc2) {
        this_world = world;
        location1 = loc1;
        location2 = loc2;
        blocks = null;
    }

    public List<Location> get_blocks() {
        if (blocks != null) {
            return blocks;
        } else {
            Double lc1x = location1.getX();
            Double lc1y = location1.getY();
            Double lc1z = location1.getZ();
            Double lc2x = location2.getX();
            Double lc2y = location2.getY();
            Double lc2z = location2.getZ();
            Location h = null;
            blocks = new LinkedList<Location>();
            if (lc1x <= lc2x) {
                for (double x = lc1x; x <= lc2x; x++) {
                    if (lc1y <= lc2y) {
                        for (double y = lc1y; y <= lc2y; y++) {
                            if (lc1z <= lc2z) {
                                for (double z = lc1z; z <= lc2z; z++) {
                                    h = new Location(this_world, x, y, z);
                                    blocks.add(h);
                                }
                            } else {
                                for (double z = lc1z; z >= lc2z; z--) {
                                    h = new Location(this_world, x, y, z);
                                    blocks.add(h);
                                }
                            }
                        }
                    } else {
                        for (double y = lc1y; y >= lc2y; y--) {
                            if (lc1z <= lc2z) {
                                for (double z = lc1z; z <= lc2z; z++) {
                                    h = new Location(this_world, x, y, z);
                                    blocks.add(h);
                                }
                            } else {
                                for (double z = lc1z; z >= lc2z; z--) {
                                    h = new Location(this_world, x, y, z);
                                    blocks.add(h);
                                }
                            }
                        }
                    }
                }
            } else {
                for (double x = lc1x; x >= lc2x; x--) {
                    if (lc1y <= lc2y) {
                        for (double y = lc1y; y <= lc2y; y++) {
                            if (lc1z <= lc2z) {
                                for (double z = lc1z; z <= lc2z; z++) {
                                    h = new Location(this_world, x, y, z);
                                    blocks.add(h);
                                }
                            } else {
                                for (double z = lc1z; z >= lc2z; z--) {
                                    h = new Location(this_world, x, y, z);
                                    blocks.add(h);
                                }
                            }
                        }
                    } else {
                        for (double y = lc1y; y >= lc2y; y--) {
                            if (lc1z <= lc2z) {
                                for (double z = lc1z; z <= lc2z; z++) {
                                    h = new Location(this_world, x, y, z);
                                    blocks.add(h);
                                }
                            } else {
                                for (double z = lc1z; z >= lc2z; z--) {
                                    h = new Location(this_world, x, y, z);
                                    blocks.add(h);
                                }
                            }
                        }
                    }
                }
            }
            return blocks;
        }
    }
}
