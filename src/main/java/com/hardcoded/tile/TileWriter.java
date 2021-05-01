package com.hardcoded.tile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.hardcoded.data.Memory;
import com.hardcoded.error.TileException;
import com.hardcoded.logger.Log;
import com.hardcoded.tile.impl.TilePart;
import com.hardcoded.tile.writers.*;
import com.hardcoded.utils.TileUtils;

/**
 * This class is made for writing {@code .tile} files.
 * 
 * <p><b><i>This writer is not able to save all data and will return incomplete results!<i><b>
 * 
 * <p>
 * 
 * @author HardCoded <https://github.com/Kariaro>
 */
public class TileWriter {
	private static final Log LOGGER = Log.getLogger();
	
	private static final MipWriter mip_writer = new MipWriter();
	private static final ClutterWriter clutter_writer = new ClutterWriter();
	private static final AssetListWriter assetList_writer = new AssetListWriter();
	private static final NodeWriter node_writer = new NodeWriter();
	//private static final PrefabReader prefab_reader = new PrefabReader();
	//private static final BlueprintListReader blueprintList_reader = new BlueprintListReader();
	//private static final DecalReader decal_reader = new DecalReader();
	//private static final HarvestableListReader harvestableList_reader = new HarvestableListReader();
	
	private TileWriter() {
		
	}
	
	public static boolean writeTile(Tile tile, String path) throws TileException {
		byte[] data = writeTile(tile);
		
		if(TileUtils.isDev()) {
			LOGGER.info("SerializedData: ");
		}
		
		try(FileOutputStream stream = new FileOutputStream(new File(path))) {
			stream.write(data);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	public static final int LATEST_VERSION = 9;
	
	public static byte[] writeTile(Tile tile) throws TileException {
		Memory memory = new Memory(0x1000000);
		memory.NextWriteString("TILE"); // Magic
		memory.NextWriteInt(LATEST_VERSION);
		
		memory.NextWriteUUID(tile.getUUID(), true);
		memory.NextWriteLong(tile.getCreatorId());
		
		memory.NextWriteInt(tile.getWidth());
		memory.NextWriteInt(tile.getHeight());
		
		//headers = new Header[width * height];
		
		int cellHeadersOffset_index = memory.index();
		memory.NextWriteInt(0); // cellHeadersOffset
		// int cellHeadersSize_index = memory.index();
		int cellHeadersSize = 0x124;
		memory.NextWriteInt(cellHeadersSize);
		memory.NextWriteInt(0); // int local_c4 = reader.NextInt();
		memory.NextWriteInt(0); // int local_c0 = reader.NextInt();
		memory.NextWriteInt(tile.getTileType() << 0x18);
		memory.WriteInt(memory.index(), cellHeadersOffset_index - memory.index());
		
		HeaderPart[] headers = new HeaderPart[tile.getWidth() * tile.getHeight()];
		for(int i = 0; i < headers.length; i++) {
			HeaderPart header_part = new HeaderPart(memory);
			memory.WriteByte(0); // Ensure that we have a header at this position
			memory.move(cellHeadersSize);
			headers[i] = header_part;
		}
		
		for(int y = 0; y < tile.getHeight(); y++) {
			for(int x = 0; x < tile.getWidth(); x++) {
				HeaderPart header_part = headers[x + y * tile.getWidth()];
				
				TilePart part = tile.getPart(x, y);
				writePart(header_part, memory, part);
			}
		}
		
		for(int i = 0; i < headers.length; i++) {
			headers[i].write();
		}
		
		// TODO: Error buffer
		memory.WriteByte(0);
		//memory.WriteBytes(new byte[0x10000]);
		
		byte[] bytes = new byte[memory.getHighestWrittenIndex()];
		System.arraycopy(memory.data(), 0, bytes, 0, bytes.length);
		return bytes;
	}
	
	private static void writePart(HeaderPart header, Memory memory, TilePart part) {
		if(part.parent.getTileType() == 0) {
			mip_writer.write(header, memory, part);
			clutter_writer.write(header, memory, part);
		}
		
		assetList_writer.write(header, memory, part);
		node_writer.write(header, memory, part);
//		prefab_writer.write(header, memory, part);
//		blueprintList_writer.write(header, memory, part);
//		decal_writer.write(header, memory, part);
//		harvestableList_writer.write(header, memory, part);
	}
	
//	private static String getHexString(byte[] bytes, int maxLength, int lineLength) {
//		StringBuilder sb = new StringBuilder();
//		int a = 1;
//		for(int i = 0; i < Math.min(bytes.length, maxLength); i++) {
//			sb.append(String.format("%02x", bytes[i]));
//			if((a ++) % lineLength == 0) sb.append('\n');
//		}
//		
//		return sb.toString();
//	}
}
