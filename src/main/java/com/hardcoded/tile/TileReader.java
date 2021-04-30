package com.hardcoded.tile;

import java.io.*;

import com.hardcoded.data.Memory;
import com.hardcoded.logger.Log;
import com.hardcoded.tile.TileHeader.Header;
import com.hardcoded.tile.impl.TileImpl;
import com.hardcoded.tile.readers.*;

public final class TileReader {
	private static final Log LOGGER = Log.getLogger();
	private static final MipReader             mip_reader = new MipReader();
	private static final ClutterReader         clutter_reader = new ClutterReader();
	private static final AssetListReader       assetList_reader = new AssetListReader();
	private static final NodeReader            node_reader = new NodeReader();
	private static final PrefabReader          prefab_reader = new PrefabReader();
	private static final BlueprintListReader   blueprintList_reader = new BlueprintListReader();
	private static final DecalReader           decal_reader = new DecalReader();
	private static final HarvestableListReader harvestableList_reader = new HarvestableListReader();
	
	private TileReader() {
		
	}
	
	public static TileImpl read(String path) throws Exception {
		return loadTile(path);
	}
	
	public static void save(TileImpl tile, String path) throws IOException {
		throw new UnsupportedOperationException("Not implemented");
	}
	
	private static byte[] readFileBytes(String path) throws IOException {
		File file = new File(path);
		DataInputStream stream = new DataInputStream(new FileInputStream(file));
		byte[] bytes = stream.readAllBytes();
		stream.close();
		return bytes;
	}
	
	public static TileImpl loadTile(String path) throws Exception {
		TileHeader header = new TileHeader(readFileBytes(path));
		
		LOGGER.info("TileFileVersion: %d", header.version);
		LOGGER.info("TileUuid: {%s}", header.uuid);
		LOGGER.info("CreatorId: %d", header.creatorId);
		LOGGER.info("Size: %d, %d", header.width, header.height);
		LOGGER.info("Type: %d", header.type);
		LOGGER.info();
		LOGGER.info("Header info:");
		LOGGER.info("CellHeadersOffset: %d", header.cellHeadersOffset);
		LOGGER.info("CellHeadersSize: %d", header.cellHeadersSize);
		LOGGER.info();
		LOGGER.info("Headers:");
		
		
		for(int i = 0; i < header.width * header.height; i++) {
			int x = i % header.width;
			int y = i / header.height;
			
			byte[] bytes = header.getHeader(x, y).data();
			System.out.printf("	BLOB(%d, %d):\n", x, y);
			System.out.printf("		%s\n\n", getHexString(bytes, header.cellHeadersSize, 32).replace("\n", "\n		"));
		}
		
		System.out.println();
		System.out.println("Reading header data:");
		
		Memory reader = new Memory(header.data());
		
		int tileXSize = header.width;
		int tileYSize = header.height;

		TileImpl tile = new TileImpl(tileXSize, tileYSize);
		if(tileYSize > 0) {
			for(int y = 0; y < tileYSize; y++) {
				for(int x = 0; x < tileXSize; x++) {
					Header h = header.getHeader(x, y);
					TilePart part = tile.getTile(x, y);
					
					if(header.type == 0) {
						mip_reader.read(h, reader, part);
						clutter_reader.read(h, reader, part);
					}
					
					assetList_reader.read(h, reader, part);
					node_reader.read(h, reader, part);
					prefab_reader.read(h, reader, part);
					blueprintList_reader.read(h, reader, part);
					decal_reader.read(h, reader, part);
					harvestableList_reader.read(h, reader, part);
				}
			}
		}
		
		return tile;
	}
	
	private static String getHexString(byte[] bytes, int maxLength, int lineLength) {
		StringBuilder sb = new StringBuilder();
		int a = 1;
		for(int i = 0; i < Math.min(bytes.length, maxLength); i++) {
			sb.append(String.format("%02x", bytes[i]));
			if((a ++) % lineLength == 0) sb.append('\n');
		}
		
		return sb.toString();
	}
	
//	static void Assert(boolean value, String message, int lineIndex) {
//		if(value) return;
//		
//		String msg = String.format("ERROR: ASSERT: '%s' : Z:\\Jenkins\\workspace\\sm\\TileEditorCommon\\Tile.cpp:%d\n", message, lineIndex);
//		System.err.println(msg);
//		throw new AssertionError(msg, null);
//	}
//	
//	static void Assert(boolean value, String name, String message, int lineIndex) {
//		if(value) return;
//		
//		String msg = String.format("ERROR: ASSERT: '%s' : Z:\\Jenkins\\workspace\\sm\\TileEditorCommon\\%s.cpp:%d\n", message, name, lineIndex);
//		System.err.println(msg);
//		throw new AssertionError(msg, null);
//	}
//	
//	static void Log(String name, String message, int lineIndex) {
//		String msg = String.format("INFO: '%s' : Z:\\Jenkins\\workspace\\sm\\TileEditorCommon\\%s.cpp:%d\n", message, name, lineIndex);
//		System.err.println(msg);
//	}
}
