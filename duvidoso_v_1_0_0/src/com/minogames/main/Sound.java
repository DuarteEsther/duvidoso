package com.minogames.main;

import java.applet.Applet;
import java.applet.AudioClip;



@SuppressWarnings("removal")
public class Sound {

	private AudioClip clip;
	
	public static final Sound musicBackground = new Sound("/msc.wav");
	public static final Sound balaEffect = new Sound ("/bala.mp3");
	public static final Sound inimigoEffect = new Sound ("/inimigo.mp3");
	public static final Sound coletaEffect = new Sound ("/coleta.mp3");
	public static final Sound menuEffect = new Sound ("/pista.wav");
	public static final Sound gameoverEffect = new Sound ("/fim.wav");
	public static final Sound tiroEffect = new Sound ("/tiroinimigo.wav");
	public static final Sound jumpEffect = new Sound ("/pulo.wav");
	public static final Sound dialogEffect = new Sound ("/dialogo.wav");
	
	
	@SuppressWarnings("deprecation")
	private Sound (String name) {
		try {
			clip = Applet.newAudioClip(Sound.class.getResource(name));
		}catch(Throwable e) {}
			
		}
	public void play() {
		try {
			new Thread() {
				@SuppressWarnings("deprecation")
				public void run() {
					clip.play();
				}
				
			}.start();
		}catch(Throwable e) {
			
		}
	}
	
	public void loop() {
		try {
			new Thread() {
				@SuppressWarnings("deprecation")
				public void run() {
					clip.loop();
				}
				
			}.start();
		}catch(Throwable e) {
			
		}
	}
}

