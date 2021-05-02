package com.hardcoded.tile;

import java.util.List;
import java.util.Set;

import com.hardcoded.tile.object.TileEntity;
import com.hardcoded.utils.NotNull;

/**
 * A node interface.
 * 
 * @author HardCoded <https://github.com/Kariaro>
 */
public interface Node extends TileEntity {
	/**
	 * Returns a unmodifiable list of tags this node defines.
	 * @return a unmodifiable list of tags this node defines
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
	 * Change the state of a tag
	 * @param name the name of the tag
	 * @param enable if the tag should be enabled
	 * @throws NullPointerException if the tag was not found
	 */
	void setTagState(String name, boolean enable);
	
	/**
	 * Returns a unmodifiable set of the active tags inside this node.
	 * @return a unmodifiable set of the active tags inside this node
	 */
	@NotNull
	Set<String> getActiveTags();
}
