package com.hardcoded.tile.impl;

import java.util.*;

import com.hardcoded.tile.Node;

/**
 * A node implementation.
 * 
 * @author HardCoded
 */
public class NodeImpl extends TileEntityImpl implements Node {
	private final List<String> defined_tags = new ArrayList<>();
	public final Set<String> tags = new HashSet<>();
	
	public NodeImpl(List<String> tags) {
		defined_tags.addAll(tags);
	}

	@Override
	public List<String> getDefinedTags() {
		return Collections.unmodifiableList(defined_tags);
	}
	
	@Override
	public void setTagState(String name, boolean enable) {
		if(!defined_tags.contains(name)) throw new NullPointerException("The tag '" + name + "' does not exist inside this node");
		
		if(enable) {
			tags.add(name);
		} else {
			tags.remove(name);
		}
	}
	
	@Override
	public boolean getTagState(String name) {
		if(!defined_tags.contains(name)) throw new NullPointerException("The tag '" + name + "' does not exist inside this node");
		return tags.contains(name);
	}
	
	@Override
	public Set<String> getActiveTags() {
		return Collections.unmodifiableSet(tags);
	}
}
