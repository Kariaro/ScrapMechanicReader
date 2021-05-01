package com.hardcoded.test;

import java.awt.Window.Type;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.hardcoded.tile.Tile;
import com.hardcoded.tile.TileReader;
import com.hardcoded.tile.TileWriter;
import com.hardcoded.utils.TileUtils;

public class Main {
	public static void main(String[] args) {
		// String path = getTile("tile_12");
		
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
		
		
//		{
//			test();
//		}
		
		Tile tile = null;
		try {
			String path = "D:\\Steam\\steamapps\\common\\Scrap Mechanic\\Survival\\Terrain\\Tiles\\start_area\\SurvivalStartArea_BigRuin_01.tile";
			path = getGameTile("GROUND512_01");
			//path = "D:/Steam/steamapps/common/Scrap Mechanic/Data/LocalPrefabs/Warehouse_Room_Toilets_2x2x1_04.prefab";
			//path = "C:/Users/Admin/Downloads/warehouse3prefabbuttile.tile";
			//path = "C:/Users/Admin/Downloads/ChallengeBuilderDefault.tile";
			//path = "C:/Users/Admin/Downloads/Warehouse_Exterior_4Floors_256_01.tile";
			//path = "C:/Users/Admin/Downloads/bbbbbbbbbbbbbbbbbbb.tile.export";
			//path = "C:/Users/Admin/Downloads/Warehouse_Interior_EncryptorFloor_01.tile";
			//path = getGameTile("HILLS512_01");
			//path = getGameTile("Road_Dirt_Three-way_01");
			System.out.println(path);
			
			tile = TileReader.readTileFromPath(path);
			tile.resize(1, 1);
			TileWriter.writeTile(tile, "");
			
			System.out.println("----------------------------------------------");
			System.out.println("----------------------------------------------");
			System.out.println("----------------------------------------------");
			tile = TileReader.readTileFromPath("res/test.tile");
			
			//debug(tile);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		if(true) return;
		
		// String str = createJson(path);
		
//		StringSelection stringSelection = new StringSelection(str);
//		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
//		clipboard.setContents(stringSelection, null);
	}
	
	private static byte[] hex_to_bytes(String str) {
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
			System.out.println("-------------------------");
			
			String msg = test.substring(0, i);
			int size = msg.length();
			
			byte[] compressed = TileUtils.compress_data(msg.getBytes());
			int compressedSize = compressed.length;

			{ for(byte b : compressed) System.out.printf("%02x", b); System.out.println(); }
			
			byte[] bytes = new byte[size];
			int debugSize = TileUtils.decompress_data(compressed, bytes, size);
			System.out.printf("  debugSize == compressed_size: %d == %d\n", debugSize, compressedSize);
			if(debugSize != compressedSize) {
				System.out.println("  [ERROR] debugSize != compressed_size");
			}
			System.out.println();
			
			// { for(byte b : bytes) System.out.printf("%02x", b); System.out.println(); }
			
			String uncompressedMessage = new String(bytes, 0, size);
			System.out.printf("  True Message: [%s]\n", msg);
			System.out.printf("  Read Message: [%s]\n", uncompressedMessage);
			
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
//			for(int i = 0; i < bytes.length; i++) System.out.printf("%02x", bytes[i]);
//			System.out.println();
//			
//		}
//		
//		for(int i = 0; i < compressed.length; i++) System.out.printf("%02x", compressed[i]);
//		System.out.println();
//		
//		byte[] uncompressed = new byte[100000];
//		int debugSize = func.decompress(compressed, uncompressed, compressed_length);
//		
//		System.out.printf("debugSize == compressed_size: %d == %d\n", debugSize, compressed.length);
//		for(int i = 0; i < uncomp_length; i++) System.out.printf("%02x", uncompressed[i]);
//		System.out.println();
//		System.out.println("Output: " + new String(uncompressed, 0, uncomp_length));
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
	
	private static void debug(Tile tile) {
		int th = tile.getHeight();
		int tw = tile.getWidth();
		int m = 2;
		
		BufferedImage bi1 = new BufferedImage(0x20 * tw + 1, 0x20 * th + 1, BufferedImage.TYPE_INT_ARGB);
		BufferedImage bi2 = new BufferedImage(0x20 * tw + 1, 0x20 * th + 1, BufferedImage.TYPE_INT_ARGB);
		BufferedImage bi3 = new BufferedImage(0x80 * tw, 0x80 * th, BufferedImage.TYPE_INT_ARGB);
		
		int[] colors = tile.getVertexColor(); // getObject(tile, "vertexColor");
		bi1.setRGB(0, 0, bi1.getWidth(), bi1.getHeight(),
			colors, 0, bi1.getWidth()
		);
		
		float[] heights = tile.getVertexHeight(); // getObject(tile, "vertexHeight");
		for(int i = 0; i < colors.length; i++) {
			colors[i] = ((int)(0x7f + 0x10 * heights[i]) * 0x010101) | 0xff000000;
		}
		bi2.setRGB(0, 0, bi2.getWidth(), bi2.getHeight(),
			colors, 0, bi2.getWidth()
		);
		
		byte[] clutters = tile.getClutter(); // getObject(tile, "clutter");
		colors = new int[bi3.getWidth() * bi3.getHeight()];
		for(int i = 0; i < colors.length; i++) {
			colors[i] = ((0x3423415 * Byte.toUnsignedInt((byte)clutters[i])) & 0xffffff) | 0xff000000;
			// int value = Byte.toUnsignedInt(clutters[i]);
			// System.out.println(value);
			// colors[i] = (0x010101 * ((value * 0x3423415) & 0xffffff)) | 0xff000000;
		}
		bi3.setRGB(0, 0, bi3.getWidth(), bi3.getHeight(),
			colors, 0, bi3.getWidth()
		);
		
		createWindow(bi1, "ColorMap", m);
		createWindow(bi2, "HeightMap", m);
		createWindow(bi3, "Clutter", m / 2);
	}
	
	private static void createWindow(BufferedImage bi, String name, int m) {
		BufferedImage bi2 = new BufferedImage(bi.getWidth() * m, bi.getWidth() * m, BufferedImage.TYPE_INT_ARGB);
		bi2.createGraphics().drawImage(bi, 0, 0, bi2.getWidth(), bi2.getHeight(), null);
		
		JFrame frame = new JFrame(name);
		frame.setResizable(false);
		frame.setSize(500, 500);
		frame.setType(Type.UTILITY);
		frame.setAlwaysOnTop(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel label = new JLabel(new ImageIcon(bi2));
		label.setBorder(null);
		frame.getContentPane().add(label);
		frame.setVisible(true);
		frame.pack();
	}
}
