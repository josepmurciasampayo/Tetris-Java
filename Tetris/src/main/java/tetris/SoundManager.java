package tetris;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * Class to manage sound effects and background music in the game
 */
public class SoundManager {
	
	// Declaring strings for file paths of different sound files
	private static String musicFile = "sounds/bg.mp3";
	private static String breakFile = "sounds/collide.mp3";
	private static String placedFile = "sounds/placed.mp3";
	private static String lavelFile = "sounds/win.mp3";
	private static String loseFile = "sounds/lose.mp3";
	private static String hammerFile = "sounds/hammer.mp3";
	private static String foundFile = "sounds/found.mp3";
	private static String explodeFile = "sounds/explode.mp3";
	
	// Declaring MediaPlayer objects for different sounds
	private static MediaPlayer bgSound;
	private static MediaPlayer breakSound;
	private static MediaPlayer placedSound;
	private static MediaPlayer levelSound;
	private static MediaPlayer loseSound;
	private static MediaPlayer hammerSound;
	private static MediaPlayer foundSound;
	private static MediaPlayer explodeSound;
	
	/**
	 * Method to set up sound files
	 */
	public static void setup() {
		
		// Loading background music and setting it to loop infinitely
		Media sound = new Media(new File(musicFile).toURI().toString());
		bgSound = new MediaPlayer(sound);
		bgSound.setOnEndOfMedia(new Runnable() {
		       public void run() {
		    	   bgSound.seek(Duration.ZERO);
		       }
		   });
		
		// Loading collision sound effect
		breakSound = new MediaPlayer(new Media(new File(breakFile).toURI().toString()));
		
		// Loading sound effect for when a shape is placed
		placedSound = new MediaPlayer(new Media(new File(placedFile).toURI().toString()));
		
		// Loading sound effect for when the player completes a level
		levelSound = new MediaPlayer(new Media(new File(lavelFile).toURI().toString()));
		
		// Loading sound effect for when the player loses the game
		loseSound = new MediaPlayer(new Media(new File(loseFile).toURI().toString()));
		
		// Loading sound effect for a special hammer powerup
		hammerSound = new MediaPlayer(new Media(new File(hammerFile).toURI().toString()));
		
		// Loading sound effect for a special found powerup
		foundSound = new MediaPlayer(new Media(new File(foundFile).toURI().toString()));
		
		// Loading sound effect for a special explode powerup
		explodeSound = new MediaPlayer(new Media(new File(explodeFile).toURI().toString()));
	}
	
	/**
	 * Method to start playing background music
	 */
	public static void playBackground() {
		bgSound.play();
	}
	
	/**
	 * Method to stop playing background music
	 */
	public static void stopBackground() {
		bgSound.pause();
	}


	/**
	 * Method to play the collision sound effect and set it back to the beginning
	 */
	public static void playBreakSound() {
		breakSound.play();
		breakSound.seek(Duration.ZERO);
	}
	
	
	/**
	 * // Method to play the placed sound effect and set it back to the beginning
	 */
	public static void playPlacedSound() {
		placedSound.play();
		placedSound.seek(Duration.ZERO);
	}
	
	/**
	 * Method to play the level completion sound effect and set it back to the beginning
	 */
	public static void playLevel() {
		levelSound.play();
		levelSound.setVolume(1.0);
		levelSound.seek(Duration.ZERO);
	}

	/**
	 * Method to play the game loose sound effect and set it back to the beginning
	 */
	public static void playlose() {
		loseSound.play();
		loseSound.seek(Duration.ZERO);
	}
	
	/**
	 * Method to play the hammer struck sound effect and set it back to the beginning
	 */
	public static void playHammer() {
		hammerSound.play();
		hammerSound.seek(Duration.ZERO);
	}
	
	/**
	 * Method to play the hammer found sound effect and set it back to the beginning
	 */
	public static void playfound() {
		foundSound.play();
		foundSound.seek(Duration.ZERO);
	}
	
	/**
	 * Method to play the explosion sound effect and set it back to the beginning
	 */
	public static void playExplode() {
		explodeSound.play();
		explodeSound.seek(Duration.ZERO);
	}
	
}
