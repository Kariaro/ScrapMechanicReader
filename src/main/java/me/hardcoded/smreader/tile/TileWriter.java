package me.hardcoded.smreader.tile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import me.hardcoded.smreader.data.Memory;
import me.hardcoded.smreader.error.TileException;
import me.hardcoded.smreader.logger.Log;
import me.hardcoded.smreader.tile.impl.TilePart;
import me.hardcoded.smreader.tile.writers.*;
import me.hardcoded.smreader.utils.TileUtils;

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
	
	private static final MipWriter mipWriter = new MipWriter();
	private static final ClutterWriter clutterWriter = new ClutterWriter();
	private static final AssetListWriter assetListWriter = new AssetListWriter();
	private static final NodeWriter nodeWriter = new NodeWriter();
//	private static final ScriptWriter script_writer = new ScriptWriter();
	private static final PrefabWriter prefabWriter = new PrefabWriter();
	private static final BlueprintListWriter blueprintListWriter = new BlueprintListWriter();
	private static final DecalWriter decalWriter = new DecalWriter();
	private static final HarvestableListWriter harvestableListWriter = new HarvestableListWriter();
	
	private TileWriter() {
		
	}
	
	public static boolean writeTile(Tile tile, String path) throws TileException {
		byte[] data = writeTile(tile);
		
		if (TileUtils.isDev()) {
			LOGGER.info("SerializedData: ");
		}
		
		try (FileOutputStream stream = new FileOutputStream(new File(path))) {
			stream.write(data);
		} catch (IOException e) {
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
		for (int i = 0; i < headers.length; i++) {
			CellHeader header_part = new CellHeader(memory);
			memory.WriteByte(0); // Ensure that we have a header at this position
			memory.move(cellHeadersSize);
			headers[i] = header_part;
		}
		
		for (int y = 0; y < tile.getHeight(); y++) {
			for (int x = 0; x < tile.getWidth(); x++) {
				CellHeader header_part = headers[x + y * tile.getWidth()];
				
				TilePart part = tile.getPart(x, y);
				writePart(header_part, memory, part);
			}
		}
		
		for (int i = 0; i < headers.length; i++) {
			headers[i].write();
		}
		
		// FIXME: Why do we need to add this byte?
		memory.WriteByte(0);
		
		byte[] bytes = new byte[memory.getHighestWrittenIndex()];
		System.arraycopy(memory.data(), 0, bytes, 0, bytes.length);
		return bytes;
	}
	
	private static void writePart(CellHeader header, Memory memory, TilePart part) {
		if (part.getParent().getTileType() == 0) {
			mipWriter.write(header, memory, part);
			clutterWriter.write(header, memory, part);
		}
		
		assetListWriter.write(header, memory, part);
		nodeWriter.write(header, memory, part);
//		script_writer.write(header, memory, part);
		prefabWriter.write(header, memory, part);
		blueprintListWriter.write(header, memory, part);
		decalWriter.write(header, memory, part);
		harvestableListWriter.write(header, memory, part);
	}
}
