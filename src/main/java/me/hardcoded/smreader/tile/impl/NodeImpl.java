package me.hardcoded.smreader.tile.impl;

import java.util.*;

import me.hardcoded.smreader.tile.data.LuaData;
import me.hardcoded.smreader.tile.object.Node;

/**
 * A node implementation.
 * 
 * @author HardCoded
 * @since v0.1
 */
public class NodeImpl extends TileEntityImpl implements Node {
	private final List<String> definedTags = new ArrayList<>();
	public final Set<String> tags = new HashSet<>();
	private LuaData luaData;
	
	public NodeImpl(List<String> tags) {
		definedTags.addAll(tags);
	}

	@Override
	public List<String> getDefinedTags() {
		return Collections.unmodifiableList(definedTags);
	}
	
	@Override
	public void setTagState(String name, boolean enable) {
		if (!definedTags.contains(name)) throw new NullPointerException("The tag '" + name + "' does not exist inside this node");
		
		if (enable) {
			tags.add(name);
		} else {
			tags.remove(name);
		}
	}
	
	@Override
	public boolean getTagState(String name) {
		if (!definedTags.contains(name)) throw new NullPointerException("The tag '" + name + "' does not exist inside this node");
		return tags.contains(name);
	}
	
	@Override
	public Set<String> getActiveTags() {
		return Collections.unmodifiableSet(tags);
	}
	
	@Override
	public LuaData getLuaData() {
		return luaData;
	}
	
	@Override
	public void setLuaData(LuaData luaData) {
		this.luaData = luaData;
	}
}
