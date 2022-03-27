package me.hardcoded.smreader.test;

import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import me.hardcoded.smreader.game.GameContext;
import me.hardcoded.smreader.logger.Log;
import me.hardcoded.smreader.prefab.readers.PrefabFileReader;
import me.hardcoded.smreader.tile.TileReader;
import me.hardcoded.smreader.tile.Tile;
import me.hardcoded.smreader.tile.impl.NodeImpl;
import me.hardcoded.smreader.tile.impl.PrefabImpl;
import me.hardcoded.smreader.tile.impl.TilePart;
import me.hardcoded.smreader.tile.object.Prefab;
import me.hardcoded.smreader.utils.TileUtils;

public class Main {
	private static final Log LOGGER = Log.getLogger();
	private static final GameContext CONTEXT = new GameContext("D:\\Steam\\steamapps\\common\\Scrap Mechanic\\");
	
	public static void main(String[] args) {
		// String path = getTile("tile_12");
		
		System.out.println(Arrays.toString(tryCompress("LUA")));
		// path = getTile("tile_9");
		// path = getTile("tile_5");
		// path = getTile("tile_1");
		// path = getTile("TESTING_TILE_FLAT");
		// path = getTile("TESTING_TILE_FLAT_MEDIUM");
		// path = getTile("TESTING_TILE_FLAT_MEDIUM_COLORFLAT_2");
		// path = getTile("TESTING_TILE_FLAT_MEDIUM_COLORFLAT");
		
		//path = getGameTile("MEADOW128_09");
		//path = getGameTile("MEADOW256_01");
		//path = getGameTile("GROUND512_01");
		
		Tile tile = null;
		try {
			String path = "D:\\Steam\\steamapps\\common\\Scrap Mechanic\\Survival\\Terrain\\Tiles\\start_area\\SurvivalStartArea_BigRuin_01.tile";
			path = getGameTile("GROUND512_01");
			path = getGameTile("FOREST256_01");
			//path = "D:/Steam/steamapps/common/Scrap Mechanic/Data/LocalPrefabs/Warehouse_Room_Toilets_2x2x1_04.prefab";
			//path = "C:/Users/Admin/Downloads/warehouse3prefabbuttile.tile";
			//path = "C:/Users/Admin/Downloads/ChallengeBuilderDefault.tile";
			//path = "C:/Users/Admin/Downloads/Warehouse_Exterior_4Floors_256_01.tile";
			//path = "C:/Users/Admin/Downloads/bbbbbbbbbbbbbbbbbbb.tile.export";
			//path = "C:/Users/Admin/Downloads/Warehouse_Interior_EncryptorFloor_01.tile";
			//path = getGameTile("HILLS512_01");
			//path = getGameTile("Road_Dirt_Three-way_01");
			//path = getGameTile("Road_Dirt_Three-way_01");
			//path = "D:\\Steam\\steamapps\\common\\Scrap Mechanic\\Survival\\DungeonTiles\\Warehouse_Interior_StorageFloor_02.tile";
			//path = "D:\\Steam\\steamapps\\common\\Scrap Mechanic\\Survival\\Terrain\\Tiles\\start_area\\SurvivalStartArea_BigRuin_01.tile";
			path = "Color_Map_Test.tile";
			//path = "output/Ruin_Forest_64_01.tile";
			LOGGER.info(path);
			
			tile = readResourceTile(path, CONTEXT);
			{
				TilePart part = null;
				for(int y = 0; y < tile.getHeight(); y++) {
					if(part != null) break;
					
					for(int x = 0; x < tile.getWidth(); x++) {
						TilePart p = tile.getPart(x, y);
						
						if(!p.prefabs.isEmpty()) {
							part = p;
							break;
						}
					}
				}
				
				if(part != null) {
					//PrefabImpl prefab = (PrefabImpl)PrefabFileReader.readPrefab(CONTEXT.resolve("$SURVIVAL_DATA/LocalPrefabs/Ruins_Foundation_Medium_Concrete_03.prefab"));
					
					for(Prefab tile_prefab : part.prefabs) {
						LOGGER.info("READING PREFAB");
						System.out.println("path: " + tile_prefab.getPath());
						PrefabImpl prefab = (PrefabImpl)PrefabFileReader.readPrefab(CONTEXT.resolve(tile_prefab.getPath()));
						
//						for(String str : prefab.blueprint_paths) {
//							LOGGER.info("Blueprint: '%s'", str);
//						}
//						
//						for(String str : prefab.prefabs_paths) {
//							LOGGER.info("Prefab: '%s'", str);
//						}
					}
				}
			}
			//debug(tile);
			//if(true) return;
			tile.resize(1, 1);
			
			{
				TilePart part = tile.getPart(0, 0);
				/*
				int[] new_array = {
					  0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,
					  0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,
					  0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,
					  0xffffff,0xffffff,0xffffff,0xdfdfdf,0xd3d3d3,0xe0e0e0,0xd0d0d0,0xe2e2e2,0xd0d0d0,0xe3e3e3,0xcfcfcf,0xe4e4e4,0xcfcfcf,0xe4e4e4,0xcfcfcf,0xe5e5e5,0xcfcfcf,0xe5e5e5,0xcfcfcf,0xe4e4e4,0xcfcfcf,0xe4e4e4,0xcfcfcf,0xe3e3e3,0xd0d0d0,0xe2e2e2,0xd0d0d0,0xe0e0e0,0xd3d3d3,0xdfdfdf,0xffffff,0xffffff,0xffffff,
					  0xffffff,0xffffff,0xffffff,0xa0a0a0,0x7b7b7b,0xa2a2a2,0x707070,0xa3a3a3,0x737373,0xa4a4a4,0x777777,0xa4a4a4,0x797979,0xa4a4a4,0x7d7d7d,0xa4a4a4,0x7e7e7e,0xa4a4a4,0x7d7d7d,0xa4a4a4,0x797979,0xa4a4a4,0x777777,0xa4a4a4,0x737373,0xa3a3a3,0x707070,0xa2a2a2,0x7b7b7b,0xa0a0a0,0xffffff,0xffffff,0xffffff,
					  0xffffff,0xffffff,0xffffff,0xa1a1a1,0x7c7c7c,0xa3a3a3,0x717171,0xa3a3a3,0x747474,0xa4a4a4,0x787878,0xa4a4a4,0x7a7a7a,0xa4a4a4,0x7d7d7d,0xa5a5a5,0x7f7f7f,0xa5a5a5,0x7d7d7d,0xa4a4a4,0x7a7a7a,0xa4a4a4,0x787878,0xa4a4a4,0x747474,0xa3a3a3,0x717171,0xa3a3a3,0x7c7c7c,0xa1a1a1,0xffffff,0xffffff,0xffffff,
					  0xffffff,0xefefef,0x8c8c8c,0x505050,0x3c3c3c,0x515151,0x363636,0x515151,0x383838,0x525252,0x3a3a3a,0x525252,0x3b3b3b,0x525252,0x3d3d3d,0x525252,0x3e3e3e,0x525252,0x3d3d3d,0x525252,0x3b3b3b,0x525252,0x3a3a3a,0x525252,0x383838,0x515151,0x363636,0x515151,0x3c3c3c,0x505050,0x8c8c8c,0xefefef,0xffffff,
					  0xffffff,0xcdcdcd,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0xcdcdcd,0xffffff,
					  0xffffff,0xcdcdcd,0x000000,0x000000,0x0e3b00,0x103f00,0x103f00,0x103f00,0x103f00,0x103f00,0x103f00,0x103f00,0x103f00,0x103f00,0x103f00,0x103f00,0x103f00,0x103f00,0x103f00,0x103f00,0x103f00,0x103f00,0x103f00,0x103f00,0x103f00,0x103f00,0x103f00,0x103f00,0x0e3b00,0x000000,0x000000,0xcdcdcd,0xffffff,
					  0xffffff,0xcdcdcd,0x000000,0x000000,0x1f6500,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x1f6500,0x000000,0x000000,0xcdcdcd,0xffffff,
					  0xffffff,0xcdcdcd,0x000000,0x000000,0x1f6500,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x1f6500,0x000000,0x000000,0xcdcdcd,0xffffff,
					  0xffffff,0xcdcdcd,0x000000,0x000000,0x1f6500,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x1f6500,0x000000,0x000000,0xcdcdcd,0xffffff,
					  0xffffff,0xcdcdcd,0x000000,0x000000,0x1f6500,0x216b00,0x216b00,0x1f6301,0x174c02,0x164504,0x174a03,0x1a5502,0x216900,0x216b00,0x216b00,0x1f6400,0x1f6601,0x216b00,0x216b00,0x216b00,0x216a00,0x1b5901,0x144601,0x184905,0x174c02,0x1e5f02,0x216b00,0x216b00,0x1f6500,0x000000,0x000000,0xcdcdcd,0xffffff,
					  0xffffff,0xcdcdcd,0x000000,0x000000,0x1f6500,0x216b00,0x1c5c01,0x0a2a01,0x154801,0x0f3602,0x216702,0x1e6101,0x164902,0x206900,0x216b00,0x103d00,0x164c01,0x216b00,0x216b00,0x216b00,0x184c03,0x082401,0x164b02,0x154702,0x216801,0x1b5602,0x1a5402,0x216b00,0x1f6500,0x000000,0x000000,0xcdcdcd,0xffffff,
					  0xffffff,0xcdcdcd,0x000000,0x000000,0x1f6500,0x216b00,0x216a00,0x164a02,0x1a5402,0x0f3702,0x206601,0x216b00,0x216a00,0x216b00,0x216b00,0x103d00,0x113d02,0x206701,0x216b00,0x216b00,0x216901,0x103702,0x1b5802,0x134202,0x216b00,0x216b00,0x216a00,0x216b00,0x1f6500,0x000000,0x000000,0xcdcdcd,0xffffff,
					  0xffffff,0xcdcdcd,0x000000,0x000000,0x1f6500,0x216b00,0x216b00,0x216901,0x195401,0x1d5f01,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x1b5901,0x103b01,0x0d3301,0x216a00,0x216b00,0x216b00,0x1e6101,0x195302,0x206700,0x216b00,0x216b00,0x216b00,0x216b00,0x1f6500,0x000000,0x000000,0xcdcdcd,0xffffff,
					  0xffffff,0xcdcdcd,0x000000,0x000000,0x1f6500,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x206502,0x010401,0x206402,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x1f6500,0x000000,0x000000,0xcdcdcd,0xffffff,
					  0xffffff,0xcdcdcd,0x000000,0x000000,0x1f6500,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216a00,0x1d5e02,0x206801,0x206402,0x030c01,0x206800,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x1f6500,0x000000,0x000000,0xcdcdcd,0xffffff,
					  0xffffff,0xcdcdcd,0x000000,0x000000,0x1f6500,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216a00,0x0e3701,0x0c3301,0x0c2f01,0x134301,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x1f6500,0x000000,0x000000,0xcdcdcd,0xffffff,
					  0xffffff,0xcdcdcd,0x000000,0x000000,0x1f6500,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x1c5a01,0x216901,0x1d5f01,0x1e6101,0x216b00,0x206701,0x1b5901,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x1f6500,0x000000,0x000000,0xcdcdcd,0xffffff,
					  0xffffff,0xcdcdcd,0x000000,0x000000,0x1f6500,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x1e6101,0x164903,0x184c03,0x194d04,0x174a03,0x174c03,0x206700,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x1f6500,0x000000,0x000000,0xcdcdcd,0xffffff,
					  0xffffff,0xcdcdcd,0x000000,0x000000,0x1f6500,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216a01,0x216902,0x216a00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x1f6500,0x000000,0x000000,0xcdcdcd,0xffffff,
					  0xffffff,0xcdcdcd,0x000000,0x000000,0x1f6500,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x1f6500,0x000000,0x000000,0xcdcdcd,0xffffff,
					  0xffffff,0xcdcdcd,0x000000,0x000000,0x1f6500,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x216b00,0x1f6500,0x000000,0x000000,0xcdcdcd,0xffffff,
					  0xffffff,0xcdcdcd,0x000000,0x000000,0x0e3b00,0x103f00,0x103f00,0x103f00,0x103f00,0x103f00,0x103f00,0x103f00,0x103f00,0x103f00,0x103f00,0x103f00,0x103f00,0x103f00,0x103f00,0x103f00,0x103f00,0x103f00,0x103f00,0x103f00,0x103f00,0x103f00,0x103f00,0x103f00,0x0e3b00,0x000000,0x000000,0xcdcdcd,0xffffff,
					  0xffffff,0xcdcdcd,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0x000000,0xcdcdcd,0xffffff,
					  0xffffff,0xefefef,0x8c8c8c,0x505050,0x3c3c3c,0x515151,0x363636,0x515151,0x383838,0x525252,0x3a3a3a,0x525252,0x3b3b3b,0x525252,0x3d3d3d,0x525252,0x3e3e3e,0x525252,0x3d3d3d,0x525252,0x3b3b3b,0x525252,0x3a3a3a,0x525252,0x383838,0x515151,0x363636,0x515151,0x3c3c3c,0x505050,0x8c8c8c,0xefefef,0xffffff,
					  0xffffff,0xffffff,0xffffff,0xa1a1a1,0x7c7c7c,0xa3a3a3,0x717171,0xa3a3a3,0x747474,0xa4a4a4,0x787878,0xa4a4a4,0x7a7a7a,0xa4a4a4,0x7d7d7d,0xa5a5a5,0x7f7f7f,0xa5a5a5,0x7d7d7d,0xa4a4a4,0x7a7a7a,0xa4a4a4,0x787878,0xa4a4a4,0x747474,0xa3a3a3,0x717171,0xa3a3a3,0x7c7c7c,0xa1a1a1,0xffffff,0xffffff,0xffffff,
					  0xffffff,0xffffff,0xffffff,0xa0a0a0,0x7b7b7b,0xa2a2a2,0x707070,0xa3a3a3,0x737373,0xa4a4a4,0x777777,0xa4a4a4,0x797979,0xa4a4a4,0x7d7d7d,0xa4a4a4,0x7e7e7e,0xa4a4a4,0x7d7d7d,0xa4a4a4,0x797979,0xa4a4a4,0x777777,0xa4a4a4,0x737373,0xa3a3a3,0x707070,0xa2a2a2,0x7b7b7b,0xa0a0a0,0xffffff,0xffffff,0xffffff,
					  0xffffff,0xffffff,0xffffff,0xdfdfdf,0xd3d3d3,0xe0e0e0,0xd0d0d0,0xe2e2e2,0xd0d0d0,0xe3e3e3,0xcfcfcf,0xe4e4e4,0xcfcfcf,0xe4e4e4,0xcfcfcf,0xe5e5e5,0xcfcfcf,0xe5e5e5,0xcfcfcf,0xe4e4e4,0xcfcfcf,0xe4e4e4,0xcfcfcf,0xe3e3e3,0xd0d0d0,0xe2e2e2,0xd0d0d0,0xe0e0e0,0xd3d3d3,0xdfdfdf,0xffffff,0xffffff,0xffffff,
					  0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,
					  0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,
					  0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,0xffffff,
				};
				int[] colors = part.vertexColor;
				
				for(int i = 0; i < colors.length; i++) {
					colors[i] = new_array[i] | 0xff000000;
				}
				*/
				
//				float[] heights = part.vertexHeight;
//				
//				for(int i = 0; i < heights.length; i++) {
//					heights[i] = 10;
//				}
				
				tile.setUuid(UUID.fromString("a31e714b-d3a8-409a-8cdb-aecae26755a3"));
				tile.setTileType(0);
				tile.setCreatorId(44123434);
				if(part.nodes.size() > 0) {
					List<String> tags = part.nodes.get(0).getDefinedTags();
					
					for(int i = 0; i < tags.size(); i++) {
						NodeImpl node_test = new NodeImpl(tags);
						String tag = tags.get(i);
//						switch(tag) {
//							case "CAMERA": continue;
//							default:
//						}
						
						node_test.setTagState(tag, true);
						
						node_test.setPosition(i * 2, 0, 0);
						node_test.setSize(1, 1, 1);
						node_test.setRotation(0, 0, 0, 1); // Identity
						part.nodes.add(node_test);
					}
					
					//part.nodes.clear();
				}
				
				/*{
					int xp = 0;
					for(int i = 0; i < part.assets.length; i++) {
						List<Asset> list = part.assets[i];
						
						for(Asset asset : list) {
							asset.setPosition((xp++) * 4 + 4, 0, 4);
						}
					}
				}*/
			}
//			TileWriter.writeTile(tile, "res/output/test.tile");
//			
//			LOGGER.info("----------------------------------------------");
//			LOGGER.info("----------------------------------------------");
//			LOGGER.info("----------------------------------------------");
//			tile = TileReader.readTile("res/output/test.tile", CONTEXT);
			
			debug(tile);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
//		String str = createJson(path);
//		StringSelection stringSelection = new StringSelection(str);
//		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
//		clipboard.setContents(stringSelection, null);
	}
	
	private static Tile readResourceTile(String path, GameContext context) throws IOException  {
		return TileReader.readTile("src/test/resources/" + path, CONTEXT);
	}
	
	static byte[] hex_to_bytes(String str) {
		byte[] result = new byte[str.length() / 2];
		for(int i = 0; i < str.length(); i += 2) {
			String sub = str.substring(i, i + 2);
			result[i / 2] = (byte)Integer.parseInt(sub, 16);
		}
		
		return result;
	}
	
	static void test() {
		String test = "0123456789abcdef>hijklmnopqrstuvwxyz";
		
		for(int i = 1; i < 17; i++) {
			LOGGER.info("-------------------------");
			
			String msg = test.substring(0, i);
			int size = msg.length();
			
			byte[] compressed = TileUtils.compress_data(msg.getBytes());
			int compressedSize = compressed.length;

			{ for(byte b : compressed) System.out.printf("%02x", b); System.out.println(); }
			
			byte[] bytes = new byte[size];
			int debugSize = TileUtils.decompress_data(compressed, bytes, size);
			LOGGER.info("  debugSize == compressed_size: %d == %d", debugSize, compressedSize);
			if(debugSize != compressedSize) {
				LOGGER.error("debugSize != compressed_size");
			}
			LOGGER.info();
			
			// { for(byte b : bytes) System.out.printf("%02x", b); System.out.println(); }
			
			String uncompressedMessage = new String(bytes, 0, size);
			LOGGER.info("  True Message: [%s]", msg);
			LOGGER.info("  Read Message: [%s]", uncompressedMessage);
			
		}
//		String message = "Hello this is a message compressed with some random compression system?";
//		int uncomp_length = message.length();
//		int compressed_length = message.getBytes().length;
//		
//		byte[] compressed = tryCompress(message);
//		{
//			byte[] bytes = //hex_to_bytes("f0099a9981400000004000005040f304353f279446b2279446320c00440000803e0400f0029b555ab0485e4b2b9809e41d83bbf7fe00");
//			hex_to_bytes("9a9981400000004000005040f304353f279446b227944632f304353f0000803e0000803e0000803e9b555ab0485e4b2b9809e41d83bb");
//					//    9a9981400000004000005040f304353f279446b227944632f304353f0000803e0000803e0000803e9b555ab0485e4b2b9809e41d83bb
//			
//			uncomp_length = bytes.length;
//			compressed = tryCompress(bytes);
//			compressed_length = bytes.length;
//			
//			LOGGER.info();
//			for(int i = 0; i < bytes.length; i++) System.out.printf("%02x", bytes[i]);
//			System.out.println();
//			
//		}
//		
//		LOGGER.info();
//		for(int i = 0; i < compressed.length; i++) System.out.printf("%02x", compressed[i]);
//		System.out.println();
//		
//		byte[] uncompressed = new byte[100000];
//		int debugSize = func.decompress(compressed, uncompressed, compressed_length);
//		
//		LOGGER.info("debugSize == compressed_size: %d == %d\n", debugSize, compressed.length);
//		LOGGER.info();
//		for(int i = 0; i < uncomp_length; i++) System.out.printf("%02x", uncompressed[i]);
//		System.out.println();
//		LOGGER.info("Output: %s", new String(uncompressed, 0, uncomp_length));
//		
		System.exit(0);
	}
	
	static byte[] tryCompress(String str) {
		return TileUtils.compress_data(str.getBytes());
	}
	
	static byte[] tryCompress(byte[] input) {
		return TileUtils.compress_data(input);
	}
	
	static String getTile(String name) {
		File tile_path = new File("C:/Users/Admin/AppData/Roaming/Axolot Games/Scrap Mechanic/User/User_76561198251506208/Tiles/");
		
		for(File dir_file : tile_path.listFiles()) {
			for(File file : dir_file.listFiles()) {
				if(file.getName().equals(name + ".tile")) return file.getAbsolutePath();
			}
		}
		
		return null;
	}
	
	static String getGameTile(String name) {
		File tile_path = new File("D:/Steam/steamapps/common/Scrap Mechanic/Data/Terrain/Tiles/CreativeTiles/");
		
		for(File file : tile_path.listFiles()) {
			if(file.getName().equals(name + ".tile")) return file.getAbsolutePath();
		}
		
		return null;
	}
	
	static void testFull() {
		File tile_path = new File("D:/Steam/steamapps/common/Scrap Mechanic/Data/Terrain/Tiles/");
		
		for(File file : tile_path.listFiles()) {
			if(file.getName().endsWith(".tile")) {
				try {
					// loadTile(file.getAbsolutePath());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	static void debug(Tile tile) {
		int th = tile.getHeight();
		int tw = tile.getWidth();
		int m = 10;
		
		BufferedImage bi1 = new BufferedImage(0x20 * tw + 1, 0x20 * th + 1, BufferedImage.TYPE_INT_ARGB);
		BufferedImage bi2 = new BufferedImage(0x20 * tw + 1, 0x20 * th + 1, BufferedImage.TYPE_INT_ARGB);
		BufferedImage bi3 = new BufferedImage(0x80 * tw, 0x80 * th, BufferedImage.TYPE_INT_ARGB);
		
		int[] colors = tile.getVertexColor(); // getObject(tile, "vertexColor");
		for(int i = 0; i < colors.length; i++) {
			colors[i] = colors[i] | 0xff000000;
		}
		bi1.setRGB(0, 0, bi1.getWidth(), bi1.getHeight(),
			colors, 0, bi1.getWidth()
		);
		
		float[] heights = tile.getVertexHeight(); // getObject(tile, "vertexHeight");
		for(int i = 0; i < colors.length; i++) {
			colors[i] = ((int)(0x7f + 0x10 * heights[i]) * 0x010101) | 0xff000000;
		}
		bi2.setRGB(0, 0, bi2.getWidth(), bi2.getHeight(), colors, 0, bi2.getWidth());
		
		byte[] clutters = tile.getClutter(); // getObject(tile, "clutter");
		colors = new int[bi3.getWidth() * bi3.getHeight()];
		for(int i = 0; i < colors.length; i++) {
			colors[i] = ((0x3423415 * Byte.toUnsignedInt((byte)clutters[i])) & 0xffffff) | 0xff000000;
			// int value = Byte.toUnsignedInt(clutters[i]);
			// LOGGER.info(value);
			// colors[i] = (0x010101 * ((value * 0x3423415) & 0xffffff)) | 0xff000000;
		}
		bi3.setRGB(0, 0, bi3.getWidth(), bi3.getHeight(),
			colors, 0, bi3.getWidth()
		);
		
		System.out.println(tile.getVertexColor().length);
		createWindow(bi1, "ColorMap", m);
		createWindow(bi2, "HeightMap", m);
		createWindow(bi3, "Clutter", m / 2);
		
		//createWindowDebug(tile.getPart(0, 0).test, "TestingDebug", m);
	}
	
	private static void createWindow(BufferedImage bi, String name, int m) {
		BufferedImage bi2 = new BufferedImage(bi.getWidth() * m, bi.getWidth() * m, BufferedImage.TYPE_INT_ARGB);
		bi2.createGraphics().drawImage(bi, 0, 0, bi2.getWidth(), bi2.getHeight(), null);
		
		JFrame frame = new JFrame(name);
		frame.setResizable(false);
		frame.setSize(500, 500);
		frame.setType(java.awt.Window.Type.UTILITY);
		frame.setAlwaysOnTop(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel label = new JLabel(new ImageIcon(bi2));
		label.setBorder(null);
		frame.getContentPane().add(label);
		frame.setVisible(true);
		frame.pack();
	}
	
	private static BufferedImage createImage(byte[] bytes, int width) {
		int h = (bytes.length / width) + 1;
		if(h > 1000) h = 1000;
		System.out.println(width * h + ", " + bytes.length);
		BufferedImage image = new BufferedImage(width, h, BufferedImage.TYPE_INT_ARGB);
		
		for(int i = 0; i < bytes.length; i++) {
			int v = (int)(bytes[i] & 0xff);
			int c = (v * 0x010101) | 0xff000000;
			
			int xp = i % width;
			int yp = i / width;
			//if(yp > 1000) break;
			
			try {
				image.setRGB(xp, yp, c);
			} catch(Exception e) {
				break;
			}
		}
		
		return image;
	}
	
	@SuppressWarnings("deprecation")
	static void createWindowDebug(final byte[] array, String name, int m) {
		final int[] storage = new int[] {
			(int)Math.ceil(Math.sqrt(array.length))
		};
		
		BufferedImage bi = createImage(array, storage[0]);
		BufferedImage bi2 = new BufferedImage(bi.getWidth() * m, bi.getWidth() * m, BufferedImage.TYPE_INT_ARGB);
		bi2.createGraphics().drawImage(bi, 0, 0, bi2.getWidth(), bi2.getHeight(), null);
		
		final JFrame frame = new JFrame(name);
		frame.setResizable(false);
		frame.setSize(Math.max(500, bi.getWidth()), Math.max(500, bi.getHeight()));
		frame.setType(java.awt.Window.Type.UTILITY);
		frame.setAlwaysOnTop(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final JLabel label = new JLabel(new ImageIcon(bi2));
		label.setBorder(null);
		frame.setLayout(null);
		frame.getContentPane().add(label);
		frame.setVisible(true);
		frame.pack();
		KeyAdapter adapter = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int tmp = storage[0];
				if(e.getKeyCode() == KeyEvent.VK_LEFT) {
					tmp++;
				} else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
					tmp--;
					if(tmp < 10) tmp = 10;
				}
				
				
				if(storage[0] != tmp) {
					storage[0] = tmp;
					
					int h = (array.length / tmp) + 1;
					if(h > 1000) h = 1000;
					
					BufferedImage bi = createImage(array, tmp);
					BufferedImage bi2 = new BufferedImage(tmp * m, bi.getWidth() * m, BufferedImage.TYPE_INT_ARGB);
					bi2.createGraphics().drawImage(bi, 0, 0, bi2.getWidth(), bi2.getHeight(), null);
					System.out.println(m);
					
					label.setIcon(new ImageIcon(bi2));
					
					Dimension dim = new Dimension(bi2.getWidth(), bi2.getHeight());
					label.setPreferredSize(dim);
					label.setBounds(0, 0, dim.width, dim.height);
					label.setLayout(null);
					label.setMinimumSize(dim);
					label.setMinimumSize(dim);
					label.setSize(dim);
					frame.setSize(Math.max(500, dim.width + 40), Math.max(500, dim.height + 40));
				}
			}
		};
		frame.addKeyListener(adapter);
		adapter.keyPressed(new KeyEvent(frame, 0, 0, 0, KeyEvent.VK_LEFT));
	}
}
