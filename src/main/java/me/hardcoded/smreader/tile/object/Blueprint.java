package me.hardcoded.smreader.tile.object;

import me.hardcoded.smreader.utils.NotNull;

/**
 * A blueprint interface.
 * 
 * @author HardCoded
 * @since v0.2
 */
public interface Blueprint extends TileEntity {
	
	/**
	 * Returns {@code true} if this blueprint contains json data and not a path.
	 * <p>If this method returns {@code false} you will have to load the file from {@link #getValue()}
	 * @return {@code true} if this blueprint contains json data and not a path
	 */
	boolean isLoaded();
	
	/**
	 * Returns the string value of this blueprint.
	 * <p>If {@link #isLoaded()} returned {@code false} this method will return a pathname.
	 * @return the string value of this blueprint
	 */
	@NotNull
	String getValue();
	
	/**
	 * Set the string value of this blueprint.
	 * @param value the new value of this blueprint
	 */
	void setValue(String value);
}
