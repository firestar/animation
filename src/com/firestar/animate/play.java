package com.firestar.animate;

class play extends Thread {
    private static final int DEFAULT_FRAMESPEED = 500;
    animate p;
    private Integer framespeed;
    private frameset frames = null;
    private String open_anime;
    private Animation animation;

    public play(animate plugin, Animation animation) {
        this(plugin, animation, DEFAULT_FRAMESPEED);
    }

    public play(animate plugin, Animation animation, Integer speed) {
        p = plugin;
        this.animation = animation;
        frames = animation.getFrames();
        framespeed = speed;
        frames.on_time = 0;
    }

    public void run() {
        boolean playing = true;
        int time = 0;
        animation.onStart();
        while (playing) {
            if (time == 0) {
                frames.first();
            } else {
                playing = frames.next();
            }
            if (!playing && animation.isRepeat()) {
                playing = true;
                time = 0;
            } else {
                time++;
            }
            try {
                Thread.sleep(framespeed);
            } catch (InterruptedException e) {
                e.printStackTrace();
                playing = false; // if we get interrupted we should stop the animation.
            }
        }
        animation.onStop();
    }
}
