package me.hardcoded.smreader.tile.object;

import java.util.List;
import java.util.Set;

import me.hardcoded.smreader.tile.data.LuaData;
import me.hardcoded.smreader.utils.NotNull;

/**
 * A node interface.
 * 
 * @author HardCoded <https://github.com/Kariaro>
 * @since v0.1
 */
public interface Node extends TileEntity {
	/**
	 * Returns an unmodifiable list of tags this node defines.
	 * @return an unmodifiable list of tags this node defines
	 */
	@NotNull
	List<String> getDefinedTags();
	
	/**
	 * Returns {@code true} if this tag is enabled.
	 * @param name the name of the tag
	 * @return {@code true} if this tag is enabled
	 * @throws NullPointerException if the tag was not found
	 */
	boolean getTagState(String name);
	
	/**
	 * Set the state of a tag inside this node.
	 * @param name the name of the tag
	 * @param enable if the tag should be enabled
	 * @throws NullPointerException if the tag was not found
	 */
	void setTagState(String name, boolean enable);
	
	/**
	 * Returns an unmodifiable set of the active tags inside this node.
	 * @return an unmodifiable set of the active tags inside this node
	 */
	@NotNull
	Set<String> getActiveTags();
	
	/**
	 * Returns an object with serialized lua data.
	 * @return an object with serialized lua data
	 */
	LuaData getLuaData();
	
	/**
	 * Set the lua data of this node.
	 * @param data the lua data
	 */
	void setLuaData(LuaData data);
}
