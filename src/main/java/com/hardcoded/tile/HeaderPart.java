package com.hardcoded.tile;

import com.hardcoded.data.Memory;

/**
 * @author HardCoded
 */
public class HeaderPart {
	/** 0x0 */ public int mipIndex;
	/** 0x18 */ public int mipCompressedSize;
	/** 0x30 */ public int mipSize;
	
	/** 0x48 */ public int clutterIndex;
	/** 0x4c */ public int clutterCompressedSize;
	/** 0x50 */ public int clutterSize;
	
	
	/** 0x54 */ public int[] assetListDefined;
	/** 0x64 */ public int[] assetListIndex;
	/** 0x74 */ public int[] assetListCompressedSize;
	/** 0x84 */ public int[] assetListSize;
	
	
	/** 0x94 */ public int blueprintListDefined;
	/** 0x98 */ public int blueprintListIndex;
	/** 0x9c */ public int blueprintListCompressedSize;
	/** 0xa0 */ public int blueprintListSize;
	
	/** 0xa4 */ public int nodeDefined;
	/** 0xa8 */ public int nodeIndex;
	/** 0xac */ public int nodeCompressedSize;
	/** 0xb0 */ public int nodeSize;
	
	/** 0xc4 */ public int prefabDefined;
	/** 0xc8 */ public int prefabIndex;
	/** 0xcc */ public int prefabCompressedSize;
	/** 0xd0 */ public int prefabSize;
	
	/** 0xd4 */ public int decalDefined;
	/** 0xd8 */ public int decalIndex;
	/** 0xdC */ public int decalCompressedSize;
	/** 0xe0 */ public int decalSize;
	
	/** 0xe4 */ public int[] harvestableListDefined;
	/** 0xf4 */ public int[] harvestableListIndex;
	/** 0x104 */ public int[] harvestableListCompressedSize;
	/** 0x114 */ public int[] harvestableListSize;
	
	private Memory memory;
	private int index = 0;
	public HeaderPart(Memory memory) {
		this.memory = memory;
		this.index = memory.index();
		
		assetListCompressedSize = new int[4];
		assetListSize = new int[4];
		assetListIndex = new int[4];
		assetListDefined = new int[4];
		
		harvestableListCompressedSize = new int[4];
		harvestableListSize = new int[4];
		harvestableListIndex = new int[4];
		harvestableListDefined = new int[4];
	}
	
	protected void read() {
		int old_index = memory.index();
		memory.set(index);
		
		mipIndex = memory.Int();
		mipCompressedSize = memory.Int(0x18);
		mipSize = memory.Int(0x30);
		
		clutterIndex = memory.Int(0x48);
		clutterCompressedSize = memory.Int(0x4c);
		clutterSize = memory.Int(0x50);
		
		for(int i = 0; i < 4; i++) {
			assetListDefined[i] = memory.Int(0x54 + i * 4);
			assetListIndex[i] = memory.Int(0x64 + i * 4);
			assetListCompressedSize[i] = memory.Int(0x74 + i * 4);
			assetListSize[i] = memory.Int(0x84 + i * 4);
		}
		
		blueprintListDefined = memory.Int(0x94);
		blueprintListIndex = memory.Int(0x98);
		blueprintListCompressedSize = memory.Int(0x9c);
		blueprintListSize = memory.Int(0xa0);
		
		nodeDefined = memory.Int(0xa4);
		nodeIndex = memory.Int(0xa8);
		nodeCompressedSize = memory.Int(0xac);
		nodeSize = memory.Int(0xb0);
		
		prefabDefined = memory.Int(0xc4);
		prefabIndex = memory.Int(0xc8);
		prefabCompressedSize = memory.Int(0xcc);
		prefabSize = memory.Int(0xd0);
		
		decalDefined = memory.Int(0xd4);
		decalIndex = memory.Int(0xd8);
		decalCompressedSize = memory.Int(0xdc);
		decalSize = memory.Int(0xe0);
		
		for(int i = 0; i < 4; i++) {
			harvestableListDefined[i] = memory.Int(0xe4 + i * 4);
			harvestableListIndex[i] = memory.Int(0xf4 + i * 4);
			harvestableListCompressedSize[i] = memory.Int(0x104 + i * 4);
			harvestableListSize[i] = memory.Int(0x114 + i * 4);
		}
		
		memory.set(old_index);
	}
	
	protected void write() {
		int old_index = memory.index();
		memory.set(index);
		
		memory.WriteInt(mipIndex);
		memory.WriteInt(mipCompressedSize, 0x18);
		memory.WriteInt(mipSize, 0x30);
		
		memory.WriteInt(clutterIndex, 0x48);
		memory.WriteInt(clutterCompressedSize, 0x4c);
		memory.WriteInt(clutterSize, 0x50);
		
		for(int i = 0; i < 4; i++) {
			memory.WriteInt(assetListDefined[i], 0x54 + i * 4);
			memory.WriteInt(assetListIndex[i], 0x64 + i * 4);
			memory.WriteInt(assetListCompressedSize[i], 0x74 + i * 4);
			memory.WriteInt(assetListSize[i], 0x84 + i * 4);
		}
		
		memory.WriteInt(blueprintListDefined, 0x94);
		memory.WriteInt(blueprintListIndex, 0x98);
		memory.WriteInt(blueprintListCompressedSize, 0x9c);
		memory.WriteInt(blueprintListSize, 0xa0);
		
		memory.WriteInt(nodeDefined, 0xa4);
		memory.WriteInt(nodeIndex, 0xa8);
		memory.WriteInt(nodeCompressedSize, 0xac);
		memory.WriteInt(nodeSize, 0xb0);
		
		memory.WriteInt(prefabDefined, 0xc4);
		memory.WriteInt(prefabIndex, 0xc8);
		memory.WriteInt(prefabCompressedSize, 0xcc);
		memory.WriteInt(prefabSize, 0xd0);
		
		memory.WriteInt(decalDefined, 0xd4);
		memory.WriteInt(decalIndex, 0xd8);
		memory.WriteInt(decalCompressedSize, 0xdc);
		memory.WriteInt(decalSize, 0xe0);
		
		for(int i = 0; i < 4; i++) {
			memory.WriteInt(harvestableListDefined[i], 0xe4 + i * 4);
			memory.WriteInt(harvestableListIndex[i], 0xf4 + i * 4);
			memory.WriteInt(harvestableListCompressedSize[i], 0x104 + i * 4);
			memory.WriteInt(harvestableListSize[i], 0x114 + i * 4);
		}
		
		memory.set(old_index);
	}
	
	public byte[] data() {
		int old_index = memory.index();
		byte[] bytes = memory.Bytes(0x124);
		memory.set(old_index);
		return bytes;
	}
}