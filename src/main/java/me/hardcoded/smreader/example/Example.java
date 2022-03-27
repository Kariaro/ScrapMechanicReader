package me.hardcoded.smreader.example;

import java.io.File;
import java.io.IOException;

import me.hardcoded.smreader.error.TileException;
import me.hardcoded.smreader.game.GameContext;
import me.hardcoded.smreader.tile.TileReader;
import me.hardcoded.smreader.tile.Tile;
import me.hardcoded.smreader.tile.TileWriter;

/**
 * An example class with information of how to use this api
 * 
 * @author HardCoded
 */
public class Example {
	/**
	 * To aid finding tiles in your computer you can specify the game's
	 * directory and use the {@link #getTilePath(String)} method.
	 */
	public static final GameContext CONTEXT = new GameContext(/* Absolute game path */);
	
	public static void main(String[] args) throws TileException, IOException {
		// To read a tile you need to get the path to the tile
		String example_tile = "src/main/resources/example.tile";
		
		Tile tile = TileReader.readTile(example_tile, CONTEXT);
		System.out.printf("Loading tile: '%s'\n", example_tile);
		System.out.printf("  TileFileVersion: %d\n", tile.getVersion());
		System.out.printf("  TileUuid: {%s}\n", tile.getUuid());
		System.out.printf("  CreatorId: %d\n", tile.getCreatorId());
		System.out.printf("  Size: %d, %d\n", tile.getWidth(), tile.getHeight());
		System.out.printf("  Type: %d\n", tile.getTileType());
		System.out.println("\n");
		
		// If you want to write a tile you use the TileWriter class
		System.out.println("Writing example output file");
		if (!TileWriter.writeTile(tile, "src/main/resources/output/example_output.tile")) {
			throw new TileException("Failed to write example output tile");
		}
		
		
		String search_name = "MEADOW64_01";
		String tile_path = getGameTile(search_name);
		if (tile_path == null) {
			throw new TileException("GAME_DIRECTORY or tile name was not found!");
		}
		
		tile = TileReader.readTile(tile_path, CONTEXT);
		System.out.printf("Loading tile: '%s'\n", tile_path);
		System.out.printf("  TileFileVersion: %d\n", tile.getVersion());
		System.out.printf("  TileUuid: {%s}\n", tile.getUuid());
		System.out.printf("  CreatorId: %d\n", tile.getCreatorId());
		System.out.printf("  Size: %d, %d\n", tile.getWidth(), tile.getHeight());
		System.out.printf("  Type: %d\n", tile.getTileType());
	}
	
	/**
	 * This method will search though the games tiles to find the tile
	 * with the specified name.
	 * 
	 * @param name the name of the tile
	 * @return the path or {@code null} if no tile was found
	 */
	public static String getGameTile(String name) {
		if (!CONTEXT.isValid()) return null;
		
		File tile_path = new File(CONTEXT.getPath(), "Data/Terrain/Tiles/CreativeTiles/");
		
		File[] array = tile_path.listFiles();
		if (array == null) return null;
		
		for (File file : array) {
			if (file.getName().equals(name + ".tile")) {
				return file.getAbsolutePath();
			}
		}
		
		return null;
	}
}
