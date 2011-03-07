package com.firestar.animate;

class play extends Thread {
	animate p;
	private Integer framespeed = 500;
	private frameset animation=null;
	private String open_anime;
	public play(animate plugin,frameset anim, String name){
		p=plugin;
		open_anime=name;
		animation=anim;
		animation.on_time=0;
	}
	public play(animate plugin,frameset anim, String name, Integer speed){
		p=plugin;
		open_anime=name;
		animation=anim;
		framespeed=speed;
		animation.on_time=0;
	}
	public void run(){
		boolean playing = true;
		int time = 0;
		p.animations_playing.put(open_anime,true);
		while ( playing ) {
			if ( time == 0 ) {
				animation.first();
			} else {
				playing = animation.next();
			}
			if( !playing && p.animations_repeat.get(open_anime) ){
				playing=true;
				time=0;
			}else{
				time++;
			}
			try {
				Thread.sleep(framespeed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		p.animations_playing.put(open_anime,false);
	}
}