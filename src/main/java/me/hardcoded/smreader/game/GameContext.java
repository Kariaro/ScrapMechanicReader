package me.hardcoded.smreader.game;

import java.io.File;

/**
 * This class is used to supply data about the current game
 * 
 * @author HardCoded
 * @since v0.2
 */
public class GameContext {
	protected final File path;
	protected final boolean valid;
	
	/**
	 * Create an invalid {@code GameContext} 
	 */
	public GameContext() {
		this.path = null;
		this.valid = false;
	}
	
	/**
	 * Create a new {@code GameContext} class that points to the game ScrapMechanic.
	 * @param path the absolute path of ScrapMechanic
	 */
	public GameContext(String path) {
		this.path = new File(path);
		this.valid = true;
	}
	
	/**
	 * Create a new {@code GameContext} class that points to the game ScrapMechanic.
	 * <p>If {@code file} was {@code null} then this context will be invalid.
	 * @param file the absolute file of ScrapMechanic
	 */
	public GameContext(File file) {
		this.path = file;
		this.valid = (file != null);
	}
	
	/**
	 * Returns {@code true} if this context is valid.
	 * @return {@code true} if this context is valid
	 */
	public boolean isValid() {
		return valid;
	}
	
	/**
	 * Returns the path of the game or {@code null} if {@link #isValid()} is {@code false}.
	 * @return the path of the game
	 */
	public File getPath() {
		if(!isValid()) return null;
		return path;
	}
	
	/**
	 * Returns the path {@code $SURVIVAL_DATA} or {@code null} if {@link #isValid()} is {@code false}.
	 * @return the path {@code $SURVIVAL_DATA}
	 */
	public File getSurvivalData() {
		if(!isValid()) return null;
		return new File(path, "Survival");
	}
	
	/**
	 * Returns the path {@code $GAME_DATA} or {@code null} if {@link #isValid()} is {@code false}.
	 * @return the path {@code $GAME_DATA}
	 */
	public File getGameData() {
		if(!isValid()) return null;
		return new File(path, "Data");
	}
	
	/**
	 * Returns the path {@code $CHALLENGE_DATA} or {@code null} if {@link #isValid()} is {@code false}.
	 * @return the path {@code $CHALLENGE_DATA}
	 */
	public File getChallengeData() {
		if(!isValid()) return null;
		return new File(path, "Challenge");
	}
	
	/**
	 * Returns the absolute path of a game resource.
	 * @param path the resource path
	 * @return the absolute file of the resource
	 */
	public File resolve(String path) {
		if(!isValid() || path == null) return null;
		if(path.startsWith("$GAME_DATA")) return new File(getSurvivalData(), path.substring(10));
		if(path.startsWith("$SURVIVAL_DATA")) return new File(getSurvivalData(), path.substring(14));
		if(path.startsWith("$CHALLENGE_DATA")) return new File(getSurvivalData(), path.substring(15));
		
		return null;
	}
}
