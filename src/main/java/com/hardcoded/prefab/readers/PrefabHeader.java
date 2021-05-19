package com.hardcoded.prefab.readers;

import com.hardcoded.data.BitStream;

/**
 * A prefab header.
 * 
 * @author HardCoded
 * @since v0.2
 */
public class PrefabHeader {
	public int blueprintCount;
	public int hasBlueprints;
	
	public int prefabCount;
	public int hasPrefabs;
	
	public int nodeCount;
	public int hasNodes;
	
	public int assetCount;
	public int hasAssets;
	
	public int decalsCount;
	public int hasDecals;
	
	public int count_0x54;
	public int has_0x5c;
	
	
	private BitStream stream;
	private int version;
	
	public PrefabHeader(BitStream stream, int version) {
		this.version = version;
		this.stream = stream;
	}
	
	protected void read() {
		stream.move(4 * 8);
		blueprintCount = stream.readInt();
		stream.move(4 * 8);
		hasBlueprints = stream.readInt();
		
		stream.move(4 * 8);
		prefabCount = stream.readInt();
		stream.move(4 * 8);
		hasPrefabs = stream.readInt();
		
		stream.move(4 * 8);
		nodeCount = stream.readInt();
		stream.move(4 * 8);
		hasNodes = stream.readInt();
		
		if(2 < version) {
			stream.move(4 * 8);
			assetCount = stream.readInt();
			stream.move(4 * 8);
			hasAssets = stream.readInt();
		}
		
		if(3 < version) {
			stream.move(4 * 8);
			decalsCount = stream.readInt();
			stream.move(4 * 8);
			hasDecals = stream.readInt();
		}
		
		if(5 < version) {
			stream.move(4 * 8);
			count_0x54 = stream.readInt();
			stream.move(4 * 8);
			has_0x5c = stream.readInt();
		}
	}
}