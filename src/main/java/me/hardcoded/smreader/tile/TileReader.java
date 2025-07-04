package me.hardcoded.smreader.tile;

import java.io.IOException;
import java.nio.file.*;

import me.hardcoded.smreader.data.Memory;
import me.hardcoded.smreader.error.TileException;
import me.hardcoded.smreader.game.GameContext;
import me.hardcoded.smreader.logger.Log;
import me.hardcoded.smreader.tile.impl.TileImpl;
import me.hardcoded.smreader.tile.impl.TilePart;
import me.hardcoded.smreader.tile.readers.*;
import me.hardcoded.smreader.utils.TileUtils;

/**
 * This class is made for reading {@code .tile} files created by ScrapMechanic.
 * 
 * <p><b><i>This reader is not able to parse all data and will return incomplete results!<i><b>
 * 
 * <p>
 * 
 * @author HardCoded <https://github.com/Kariaro>
 * @since v0.1
 */
public class TileReader {
	private static final Log LOGGER = Log.getLogger();
	
	private static final MipReader mipReader = new MipReader();
	private static final ClutterReader clutterReader = new ClutterReader();
	private static final AssetListReader assetListReader = new AssetListReader();
	private static final NodeReader nodeReader = new NodeReader();
	private static final ScriptReader scriptReader = new ScriptReader();
	private static final PrefabReader prefabReader = new PrefabReader();
	private static final BlueprintListReader blueprintListReader = new BlueprintListReader();
	private static final DecalReader decalReader = new DecalReader();
	private static final HarvestableListReader harvestableListReader = new HarvestableListReader();
	
	private TileReader() {
		
	}
	
	/**
	 * Returns a parsed instance of a {@code .TILE} file.
	 * 
	 * @param path the absolute path of the file
	 * @return a parsed instance of a tile file
	 * 
	 * @throws TileException
	 * @throws IOException
	 */
	public static Tile readTile(String path) throws TileException, IOException {
		return readTile(path, null);
	}
	
	/**
	 * Returns a parsed instance of a {@code .TILE} file.
	 * 
	 * @param path the absolute path of the file
	 * @param context context information about the game or {@code null}
	 * @return a parsed instance of a tile file
	 * 
	 * @throws TileException
	 * @throws IOException
	 */
	public static Tile readTile(String path, GameContext context) throws TileException, IOException {
		if (path == null) {
			throw new NullPointerException("File path was null");
		}
		byte[] bytes = Files.readAllBytes(Path.of(path));
		return loadTile(bytes, context);
	}
	
	public static Tile loadTile(byte[] tile_data, GameContext context) throws TileException {
		TileHeader header = new TileHeader(tile_data);
		
		if (TileUtils.isDev()) {
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
			
			for (int i = 0; i < header.width * header.height; i++) {
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
		tile.setContext(context);
		tile.setVersion(header.version);
		tile.setTileType(header.type);
		tile.setUuid(header.uuid);
		tile.setCreatorId(header.creatorId);
		
		if (tileYSize > 0) {
			for (int y = 0; y < tileYSize; y++) {
				for (int x = 0; x < tileXSize; x++) {
					CellHeader h = header.getHeader(x, y);
					TilePart part = tile.getPart(x, y);
					
					if (header.type == 0) {
						mipReader.read(h, reader, part);
						clutterReader.read(h, reader, part);
					}
					
					assetListReader.read(h, reader, part);
					nodeReader.read(h, reader, part);
					scriptReader.read(h, reader, part);
					prefabReader.read(h, reader, part);
					blueprintListReader.read(h, reader, part);
					decalReader.read(h, reader, part);
					harvestableListReader.read(h, reader, part);
				}
			}
		}
		
		return tile;
	}
	
	private static String getHexString(byte[] bytes, int maxLength, int lineLength) {
		StringBuilder sb = new StringBuilder();
		int a = 1;
		for (int i = 0; i < Math.min(bytes.length, maxLength); i++) {
			sb.append(String.format("%02x", bytes[i]));
			if ((a ++) % lineLength == 0) {
				sb.append('\n');
			}
		}
		
		return sb.toString();
	}
}
