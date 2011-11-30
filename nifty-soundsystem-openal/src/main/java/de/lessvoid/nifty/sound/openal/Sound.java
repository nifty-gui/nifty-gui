package de.lessvoid.nifty.sound.openal;

import java.net.URL;
import java.util.logging.Logger;

import de.lessvoid.nifty.sound.openal.slick.Audio;
import de.lessvoid.nifty.sound.openal.slick.SoundStore;
import de.lessvoid.nifty.tools.resourceloader.NiftyResourceLoader;

/**
 * A single sound effect loaded from either OGG or XM/MOD file. Sounds are allocated to 
 * channels dynamically - if not channel is available the sound will not play. 
 *
 * @author kevin
 */
public class Sound {
  private Logger log = Logger.getLogger(Sound.class.getName());
	/** The internal sound effect represent this sound */
	private Audio sound;

	/**
	 * Create a new Sound 
	 * 
	 * @param url The location of the OGG or MOD/XM to load
	 * @throws SlickException Indicates a failure to load the sound effect
	 */
	public Sound(URL url) throws Exception {
		SoundStore.get().init();
		String ref = url.getFile();
		
		try {
			if (ref.toLowerCase().endsWith(".ogg")) {
				sound = SoundStore.get().getOgg(url.openStream());
			} else if (ref.toLowerCase().endsWith(".wav")) {
				sound = SoundStore.get().getWAV(url.openStream());
			} else if (ref.toLowerCase().endsWith(".aif")) {
				sound = SoundStore.get().getAIF(url.openStream());
			} else if (ref.toLowerCase().endsWith(".xm") || ref.toLowerCase().endsWith(".mod")) {
				sound = SoundStore.get().getMOD(url.openStream());
			} else {
				throw new Exception("Only .xm, .mod, .aif, .wav and .ogg are currently supported.");
			}
		} catch (Exception e) {
			log.warning(e.toString());
			throw new Exception("Failed to load sound: "+ref);
		}
	}
	
	/**
	 * Create a new Sound 
	 * 
	 * @param ref The location of the OGG or MOD/XM to load
	 * @throws SlickException Indicates a failure to load the sound effect
	 */
	public Sound(String ref, final NiftyResourceLoader resourceLoader) throws Exception {
		SoundStore.get().init();
		
		try {
			if (ref.toLowerCase().endsWith(".ogg")) {
				sound = SoundStore.get().getOgg(ref, resourceLoader);
			} else if (ref.toLowerCase().endsWith(".wav")) {
				sound = SoundStore.get().getWAV(ref, resourceLoader);
			} else if (ref.toLowerCase().endsWith(".aif")) {
				sound = SoundStore.get().getAIF(ref, resourceLoader);
			} else if (ref.toLowerCase().endsWith(".xm") || ref.toLowerCase().endsWith(".mod")) {
				sound = SoundStore.get().getMOD(ref, resourceLoader);
			} else {
				throw new Exception("Only .xm, .mod, .aif, .wav and .ogg are currently supported.");
			}
		} catch (Exception e) {
			log.warning(e.toString());
			throw new Exception("Failed to load sound: "+ref);
		}
	}
	
	/**
	 * Play this sound effect at default volume and pitch
	 */
	public void play() {
		play(1.0f, 1.0f);
	}
	
	/**
	 * Play this sound effect at a given volume and pitch
	 * 
	 * @param pitch The pitch to play the sound effect at
	 * @param volume The volumen to play the sound effect at
	 */
	public void play(float pitch, float volume) {
		sound.playAsSoundEffect(pitch, volume * SoundStore.get().getSoundVolume(), false);
	}

	/**
	 * Play a sound effect from a particular location
	 * 
	 * @param x The x position of the source of the effect
 	 * @param y The y position of the source of the effect
	 * @param z The z position of the source of the effect
	 */
	public void playAt(float x, float y, float z) {
		playAt(1.0f, 1.0f, x,y,z);
	}
	
	/**
	 * Play a sound effect from a particular location
	 * 
	 * @param pitch The pitch to play the sound effect at
	 * @param volume The volumen to play the sound effect at
	 * @param x The x position of the source of the effect
 	 * @param y The y position of the source of the effect
	 * @param z The z position of the source of the effect
	 */
	public void playAt(float pitch, float volume, float x, float y, float z) {
		sound.playAsSoundEffect(pitch, volume * SoundStore.get().getSoundVolume(), false, x,y,z);
	}
	/**
	 * Loop this sound effect at default volume and pitch
	 */
	public void loop() {
		loop(1.0f, 1.0f);
	}
	
	/**
	 * Loop this sound effect at a given volume and pitch
	 * 
	 * @param pitch The pitch to play the sound effect at
	 * @param volume The volumen to play the sound effect at
	 */
	public void loop(float pitch, float volume) {
		sound.playAsSoundEffect(pitch, volume * SoundStore.get().getSoundVolume(), true);
	}
	
	/**
	 * Check if the sound is currently playing
	 * 
	 * @return True if the sound is playing
	 */
	public boolean playing() {
		return sound.isPlaying();
	}
	
	/**
	 * Stop the sound being played
	 */
	public void stop() {
		sound.stop();
	}
}
