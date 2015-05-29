package de.lessvoid.nifty.sound.minim;

import de.lessvoid.nifty.spi.sound.SoundHandle;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import java.io.File;

/**
 * Implementation of Nifty's SoundHandle for the Minim Java audio library.
 * Minim is an audio library built primarily for use with Processing.
 * @author Xuanming
 */
public class SoundHandleMinim implements SoundHandle {
	
	AudioPlayer player;
	
	/**
	 * Create an instance of SoundHandleMinim.
	 * @param minim Instance of Minim currently in use by Processing (or Java).
	 * @param filename Path to file to be loaded as a sound file.
	 * Note that only MP3, WAV, AIFF, SND and AU are accepted by Minim.
	 */
	public SoundHandleMinim(final Minim minim, final String filename){
		if (fileExists(filename)) {
		    String fileType = filename.substring(filename.indexOf(".", filename.length() - 5), filename.length());
		    if (!fileType.equals(".mp3") && !fileType.equals(".wav") && !fileType.equals(".aiff") && !fileType.equals(".snd") && !fileType.equals(".au")) {
		    	System.err.println("Sound file " + filename + " is not in the correct format. Only MP3, WAV, AIFF, SND and AU are accepted.");
		    } else {
		    	player = minim.loadFile(filename);
		    }
		} else {
			System.err.println("Audio file " + filename + " not found.");
		}
	};

	@Override
	public void play() {
		player.rewind();
		player.play();
	}

	@Override
	public void stop() {
		player.pause();
	}

	@Override
	public void setVolume(float volume) {
		player.setGain(80 - (volume * 95));

	}

	@Override
	public float getVolume() {
		return (80.0f - (float)player.getGain()) / 95.0f;
	}

	@Override
	public boolean isPlaying() {
		return player.isPlaying();
	}

	@Override
	public void dispose() { // Do nothing.
	}

	/**
	 * Private function to check if file passed to SoundHandleMinim exists.
	 * @param filename Path of the file to be loaded as sound.
	 * @return True if file exists. False if file doesn't exist.
	 */
	private boolean fileExists(String filename) {
		File f = new File(filename);
		return f.exists(); 
	}
}
