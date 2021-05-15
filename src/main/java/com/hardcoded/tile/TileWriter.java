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
//	private static final ScriptWriter script_writer = new ScriptWriter();
//	private static final PrefabWriter prefab_writer = new PrefabWriter();
//	private static final BlueprintListWriter blueprintList_writer = new BlueprintListWriter();
//	private static final DecalWriter decal_writer = new DecalWriter();
//	private static final HarvestableListWriter harvestableList_writer = new HarvestableListWriter();
	
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
		
		memory.NextWriteUuid(tile.getUuid(), true);
		memory.NextWriteLong(tile.getCreatorId());
		
		memory.NextWriteInt(tile.getWidth());
		memory.NextWriteInt(tile.getHeight());
		
		int cellHeadersOffset_index = memory.index();
		memory.NextWriteInt(0); // cellHeadersOffset
		// int cellHeadersSize_index = memory.index();
		int cellHeadersSize = 0x124;
		memory.NextWriteInt(cellHeadersSize);
		memory.NextWriteInt(0); // int local_c4 = reader.NextInt();
		memory.NextWriteInt(0); // int local_c0 = reader.NextInt();
		memory.NextWriteInt(tile.getTileType() << 0x18);
		memory.WriteInt(memory.index(), cellHeadersOffset_index - memory.index());
		
		CellHeader[] headers = new CellHeader[tile.getWidth() * tile.getHeight()];
		for(int i = 0; i < headers.length; i++) {
			CellHeader header_part = new CellHeader(memory);
			memory.WriteByte(0); // Ensure that we have a header at this position
			memory.move(cellHeadersSize);
			headers[i] = header_part;
		}
		
		for(int y = 0; y < tile.getHeight(); y++) {
			for(int x = 0; x < tile.getWidth(); x++) {
				CellHeader header_part = headers[x + y * tile.getWidth()];
				
				TilePart part = tile.getPart(x, y);
				writePart(header_part, memory, part);
			}
		}
		
		for(int i = 0; i < headers.length; i++) {
			headers[i].write();
		}
		
		// FIXME: Why do we need to add this byte?
		memory.WriteByte(0);
		
		byte[] bytes = new byte[memory.getHighestWrittenIndex()];
		System.arraycopy(memory.data(), 0, bytes, 0, bytes.length);
		return bytes;
	}
	
	private static void writePart(CellHeader header, Memory memory, TilePart part) {
		if(part.getParent().getTileType() == 0) {
			mip_writer.write(header, memory, part);
			clutter_writer.write(header, memory, part);
		}
		
		assetList_writer.write(header, memory, part);
		node_writer.write(header, memory, part);
//		scirpt_writer.write(header, memory, part);
//		prefab_writer.write(header, memory, part);
//		blueprintList_writer.write(header, memory, part);
//		decal_writer.write(header, memory, part);
//		harvestableList_writer.write(header, memory, part);
	}
}
