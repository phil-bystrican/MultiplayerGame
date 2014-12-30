//Audio manager classes manages the loading and playing of audio files
//such as music and sound FX

package HelperClasses;
import java.io.IOException;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;


public class AudioManager {

	//variable to store music in memory
	Audio audio_music;
	//variable to store sound FX in memory
	Audio audio_soundFX;

	//singleton instance
	private static AudioManager instance = null;


	protected AudioManager() 
	{
		// Exists only to defeat instantiation.
	}

	//singleton accessor/initializer
	public static AudioManager getInstance() {
		if(instance == null) {
			instance = new AudioManager();
		}
		//return the singleton instance
		return instance;
	}


	//loads the game music from a file and stores it in the audio_music variable, returns nothing
	public void loadGameMusic()
	{
		//stop the music before loading new music
		stopMusic();

		try {
			//load the menu game into memory
			audio_music = AudioLoader.getStreamingAudio("OGG", 
					ResourceLoader.getResource("./resources/Music/BackgroundMusic.ogg"));
		} catch (IOException e1) {
			//return null if the song couldn't be loaded
			audio_music = null;
		}
	}

	//loads the game music from a file and stores it in the audio_music variable, returns nothing
	public void loadMenuMusic()
	{
		//stop the music before loading new music
		stopMusic();

		try {
			//load the menu music into memory
			audio_music = AudioLoader.getStreamingAudio("OGG", 
					ResourceLoader.getResource("./resources/Music/MenuMusic.ogg"));
		} catch (IOException e1) {
			//return null if the song couldn't be loaded
			audio_music = null;
		}
	}


	//takes no input, stops the sound fx, returns nothing
	public void playSoundFX()
	{
		//stop the sound FX
		audio_soundFX.playAsSoundEffect(1.0f, 1.0f, false);	
	}

	//takes no input, stops the music, returns nothing
	public void playMusic()
	{
		//play the music
		audio_music.playAsMusic(1.0f, 1.0f, true);	
	}

	//takes no input, stops the music and sound fx, returns nothing
	public void stopAllAudio()
	{
		//stop the music
		audio_music.stop();
		//stop the sound FX
		audio_soundFX.stop();
	}

	//takes no input, stops the sound fx, returns nothing
	public void stopSoundFX()
	{
		//stop the sound FX
		audio_soundFX.stop();
	}

	//takes no input, stops the music, returns nothing
	public void stopMusic()
	{
		if (audio_music != null)
		//stop the music
		audio_music.stop();	
	}
}
