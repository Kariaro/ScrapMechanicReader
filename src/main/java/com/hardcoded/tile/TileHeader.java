package com.hardcoded.tile;

import java.util.UUID;

import com.hardcoded.data.Memory;
import com.hardcoded.error.TileException;
import com.hardcoded.utils.TileUtils;

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
	
	public CellHeader[] headers;
	
	public TileHeader(byte[] bytes) throws TileException {
		this.bytes = bytes;
		
		Memory reader = new Memory(bytes);
		String magic = reader.NextString(4);
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
		headers = new CellHeader[width * height];
		
		cellHeadersOffset = reader.NextInt();
		cellHeadersSize = reader.NextInt();
		reader.move(8);
		// int local_c4 = reader.NextInt();
		// int local_c0 = reader.NextInt();
		
		if(version <= 1000000) {
			type = reader.NextInt() >>> 0x18;
		}
		
		if(reader.index() != cellHeadersOffset) {
			TileUtils.error("reader.index() != header.cellHeadersOffset");
			throw new TileException("reader.index() returned an invalid position");
		}
		
		if(width * height != 0) {
			byte[] headerBytes = new byte[width * height * 0x124];
			
			for(int i = 0; i < width * height; i++) {
				byte[] data = reader.NextBytes(cellHeadersSize);
				System.arraycopy(data, 0, headerBytes, i * 0x124, cellHeadersSize);
			}
			
			fillHeaderBytes(headerBytes);
		}
	}
	
	public byte[] data() {
		return bytes;
	}
	
	private TileHeader fillHeaderBytes(byte[] bytes) {
		for(int i = 0; i < width * height; i++) {
			byte[] bytes_temp = new byte[0x124];
			System.arraycopy(bytes, i * 0x124, bytes_temp, 0, 0x124);
			
			CellHeader part = new CellHeader(new Memory(bytes_temp));
			part.read();
			headers[i] = part;
		}
		
//		try {
//			Field[] array = CellHeader.class.getFields();
//			
//			for(int i = 0; i < headers.length; i++) {
//				CellHeader cell = headers[i];
//				System.out.printf("Header[%d]:\n", i);
//				for(Field f : array) {
//					Object value = f.get(cell);
//					if(value instanceof int[]) {
//						System.out.printf("    : %-30s= %s\n", f.getName(), Arrays.toString((int[])value));
//					} else {
//						System.out.printf("    : %-30s= %s\n", f.getName(), value);
//					}
//				}
//			}
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
		
		return this;
	}
	
	public CellHeader getHeader(int x, int y) {
		return headers[x + y * width];
	}
}
