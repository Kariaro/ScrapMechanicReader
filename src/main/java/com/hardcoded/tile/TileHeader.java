package com.hardcoded.tile;

import java.util.UUID;

import com.hardcoded.data.Pointer;
import com.hardcoded.error.TileException;

public class TileHeader {
	private byte[] bytes;
	public int version;
	public long creatorId;
	public UUID uuid;
	public int width;
	public int height;
	public int type;
	
	public int cellHeadersOffset;
	public int cellHeadersSize;
	
	public Header[] headers;
	
	// TODO - Mailformated Tile Header Exception
	public TileHeader(byte[] bytes) {
		this.bytes = bytes;
		
		Pointer reader = new Pointer(bytes);
		String magic = reader.NextString(4, true);
		if(!magic.equals("TILE")) {
			throw new TileException("File magic value was wrong. Should be 'TILE'");
		}
		
		version = reader.NextInt();
		
		if(version <= 1000000) {
			uuid = reader.NextUuid(true);
			creatorId = reader.NextLong();
		} else {
			uuid = UUID.randomUUID();
			creatorId = 0;
		}
		
		width = reader.NextInt();
		height = reader.NextInt();
		headers = new Header[width * height];
		
		cellHeadersOffset = reader.NextInt();
		cellHeadersSize = reader.NextInt();
		reader.move(8);
		// int local_c4 = reader.NextInt();
		// int local_c0 = reader.NextInt();
		
		if(version <= 1000000) {
			type = reader.NextInt() >>> 0x18;
		}
		
		// Assert(reader.index() == cellHeadersOffset, "pos == header.cellHeadersOffset", 207);
		
		if(width * height != 0) {
			byte[] headerBytes = new byte[width * height * 0x124];
			
			for(int i = 0; i < width * height; i++) {
				reader.NextBytes(headerBytes, i * 0x124, cellHeadersSize);
			}
			
			fillHeaderBytes(headerBytes);
		}
	}
	
	public byte[] data() {
		return bytes;
	}
	
	private TileHeader fillHeaderBytes(byte[] bytes) {
		for(int i = 0; i < width * height; i++) {
			Header header = new Header();
			System.arraycopy(bytes, i * 0x124, header.data(), 0, 0x124);
			
			header.init();
			headers[i] = header;
		}
		
		return this;
	}
	
	public Header getHeader(int x, int y) {
		return headers[x + y * width];
	}
	
	public class Header {
		private byte[] bytes;
		
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
		
		
		/** 0x94 */ public int bytes_94;
		/** 0x98 */ public int bytes_98;
		/** 0x9c */ public int blueprintListCompressedSize;
		/** 0xa0 */ public int blueprintListSize;
		
		/** 0xa4 */ public int bytes_a4;
		/** 0xa8 */ public int bytes_a8;
		/** 0xac */ public int nodeCompressedSize;
		/** 0xb0 */ public int nodeSize;
		
		/** 0xc4 */ public int bytes_c4;
		/** 0xc8 */ public int bytes_c8;
		/** 0xcc */ public int prefabCompressedSize;
		/** 0xd0 */ public int prefabSize;
		
		/** 0xd4 */ public int bytes_d4;
		/** 0xd8 */ public int bytes_d8;
		/** 0xdC */ public int decalCompressedSize;
		/** 0xe0 */ public int decalSize;
		
		
		/** 0xe4 */ public boolean[] harvestableListDefined;
		/** 0xf4 */ public int[] harvestableListIndex;
		/** 0x104 */ public int[] harvestableListCompressedSize;
		/** 0x114 */ public int[] harvestableListSize;
		
		public Header() {
			bytes = new byte[0x124];
			assetListCompressedSize = new int[4];
			assetListSize = new int[4];
			assetListIndex = new int[4];
			assetListDefined = new int[4];
			

			harvestableListCompressedSize = new int[4];
			harvestableListSize = new int[4];
			harvestableListIndex = new int[4];
			harvestableListDefined = new boolean[4];
		}
		
		private void init() {
			Pointer memory = new Pointer(bytes);
			
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
			
			bytes_94 = memory.Int(0x94);
			bytes_98 = memory.Int(0x98);
			blueprintListCompressedSize = memory.Int(0x9c);
			blueprintListSize = memory.Int(0xa0);
			
			bytes_a4 = memory.Int(0xa4);
			bytes_a8 = memory.Int(0xa8);
			nodeCompressedSize = memory.Int(0xac);
			nodeSize = memory.Int(0xb0);
			
			bytes_c4 = memory.Int(0xc4);
			bytes_c8 = memory.Int(0xc8);
			prefabCompressedSize = memory.Int(0xcc);
			prefabSize = memory.Int(0xd0);
			
			bytes_d4 = memory.Int(0xd4);
			bytes_d8 = memory.Int(0xd8);
			decalCompressedSize = memory.Int(0xdc);
			decalSize = memory.Int(0xe0);
			
			for(int i = 0; i < 4; i++) {
				harvestableListDefined[i] = memory.Int(0xe4 + i * 4) != 0;
				harvestableListIndex[i] = memory.Int(0xf4 + i * 4);
				harvestableListCompressedSize[i] = memory.Int(0x104 + i * 4);
				harvestableListSize[i] = memory.Int(0x114 + i * 4);
			}
		}
		
		public byte[] data() {
			return bytes;
		}
	}
}
