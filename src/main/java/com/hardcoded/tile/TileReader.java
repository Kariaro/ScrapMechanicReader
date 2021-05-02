package com.hardcoded.tile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.hardcoded.data.Memory;
import com.hardcoded.error.TileException;
import com.hardcoded.logger.Log;
import com.hardcoded.tile.impl.TileImpl;
import com.hardcoded.tile.impl.TilePart;
import com.hardcoded.tile.readers.*;
import com.hardcoded.utils.TileUtils;

/**
 * This class is made for reading {@code .tile} files created by ScrapMechanic.
 * 
 * <p><b><i>This reader is not able to parse all data and will return incomplete results!<i><b>
 * 
 * <p>
 * 
 * @author HardCoded <https://github.com/Kariaro>
 */
public class TileReader {
	private static final Log LOGGER = Log.getLogger();
	
	private static final MipReader mip_reader = new MipReader();
	private static final ClutterReader clutter_reader = new ClutterReader();
	private static final AssetListReader assetList_reader = new AssetListReader();
	private static final NodeReader node_reader = new NodeReader();
	private static final ScriptReader script_reader = new ScriptReader();
	private static final PrefabReader prefab_reader = new PrefabReader();
	private static final BlueprintListReader blueprintList_reader = new BlueprintListReader();
	private static final DecalReader decal_reader = new DecalReader();
	private static final HarvestableListReader harvestableList_reader = new HarvestableListReader();
	
	private TileReader() {
		
	}
	
	public static Tile readTile(String path) throws TileException, IOException {
		return loadTile(path);
	}
	
	public static Tile loadTile(String path) throws TileException, IOException {
		byte[] bytes = Files.readAllBytes(Path.of(path));
		return loadTile(bytes);
	}
	
	public static Tile loadTile(byte[] tile_data) throws TileException {
		TileHeader header = new TileHeader(tile_data);
		
		if(TileUtils.isDev()) {
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
				int y = i / header.width;
				
				byte[] bytes = header.getHeader(x, y).data();
				LOGGER.info("    BLOB(%d, %d):", x, y);
				LOGGER.info("        %s\n\n", getHexString(bytes, header.cellHeadersSize, 32).replace("\n", "\n        "));
			}
			
			LOGGER.info();
			LOGGER.info("Reading header data:");
		}
		
		Memory reader = new Memory(header.data());
		
		int tileXSize = header.width;
		int tileYSize = header.height;

		TileImpl tile = new TileImpl(tileXSize, tileYSize);
		tile.setVersion(header.version);
		tile.setTileType(header.type);
		tile.setUUID(header.uuid);
		tile.setCreatorId(header.creatorId);
		
		if(tileYSize > 0) {
			for(int y = 0; y < tileYSize; y++) {
				for(int x = 0; x < tileXSize; x++) {
					CellHeader h = header.getHeader(x, y);
					TilePart part = tile.getPart(x, y);
					
					if(header.type == 0) {
						mip_reader.read(h, reader, part);
						clutter_reader.read(h, reader, part);
					}
					
					assetList_reader.read(h, reader, part);
					node_reader.read(h, reader, part);
					script_reader.read(h, reader, part);
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
}
