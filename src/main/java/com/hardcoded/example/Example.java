package com.hardcoded.example;

import java.io.File;
import java.io.IOException;

import com.hardcoded.error.TileException;
import com.hardcoded.tile.Tile;
import com.hardcoded.tile.TileReader;
import com.hardcoded.tile.TileWriter;

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
	public static final String GAME_DIRECTORY = "<game_path>";
	
	public static void main(String[] args) throws TileException, IOException {
		// To read a tile you need to get the path to the tile
		String example_tile = "res/example.tile";
		
		Tile tile = TileReader.readTile(example_tile);
		System.out.printf("Loading tile: '%s'\n", example_tile);
		System.out.printf("  TileFileVersion: %d\n", tile.getVersion());
		System.out.printf("  TileUuid: {%s}\n", tile.getUUID());
		System.out.printf("  CreatorId: %d\n", tile.getCreatorId());
		System.out.printf("  Size: %d, %d\n", tile.getWidth(), tile.getHeight());
		System.out.printf("  Type: %d\n", tile.getTileType());
		System.out.println("\n");
		
		// If you want to write a tile you use the TileWriter class
		
		System.out.println("Writing example output file");
		if(!TileWriter.writeTile(tile, "res/output/example_output.tile")) {
			throw new TileException("Failed to write example output tile");
		}
		
		
		String search_name = "MEADOW64_01";
		String tile_path = getGameTile(search_name);
		if(tile_path == null) {
			throw new TileException("GAME_DIRECTORY or tile name was not found!");
		}
		
		tile = TileReader.readTile(tile_path);
		System.out.printf("Loading tile: '%s'\n", tile_path);
		System.out.printf("  TileFileVersion: %d\n", tile.getVersion());
		System.out.printf("  TileUuid: {%s}\n", tile.getUUID());
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
		File tile_path = new File(GAME_DIRECTORY, "Data/Terrain/Tiles/CreativeTiles/");
		
		File[] array = tile_path.listFiles();
		if(array == null) return null;
		
		for(File file : array) {
			if(file.getName().equals(name + ".tile")) return file.getAbsolutePath();
		}
		
		return null;
	}
}
