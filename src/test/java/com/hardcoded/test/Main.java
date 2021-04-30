package com.hardcoded.test;

import java.awt.Window.Type;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.hardcoded.tile.TileReader;
import com.hardcoded.tile.impl.TileImpl;

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
		
		
		TileImpl tile = null;
		try {
			String path = "D:\\Steam\\steamapps\\common\\Scrap Mechanic\\Survival\\Terrain\\Tiles\\start_area\\SurvivalStartArea_BigRuin_01.tile";
			path = getGameTile("GROUND512_01");
			path = "D:/Steam/steamapps/common/Scrap Mechanic/Data/LocalPrefabs/Warehouse_Room_Toilets_2x2x1_04.prefab";
			path = "C:/Users/Admin/Downloads/warehouse3prefabbuttile.tile";
			path = "C:/Users/Admin/Downloads/Warehouse_Interior_EncryptorFloor_01.tile";
			//path = getGameTile("HILLS512_01");
			//path = getGameTile("Road_Dirt_Three-way_01");
			
			tile = TileReader.read(path);
			debug(tile);
			
			if(true) return;
		} catch(Exception e) {
			e.printStackTrace();
			if(true) return;
		}
		
		
		// String str = createJson(path);
		
//		StringSelection stringSelection = new StringSelection(str);
//		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
//		clipboard.setContents(stringSelection, null);
		
		// Enter your scrapmechanic directory
		String scrapdir = "D:/Steam/steamapps/common/Scrap Mechanic/";
		
		
		// Do not change
		File directory = new File(System.getProperty("user.home") + "/Desktop/DumpScrap/");
		if(!directory.exists()) directory.mkdir();
		
		try {
			Files.find(
				Paths.get(new File(scrapdir).toURI()),
				Integer.MAX_VALUE,
				(file, attrib) -> {
					return file.toFile().getName().endsWith(".tile");
				}
			).forEach((a) -> {
				COUNT ++;
				
				File file = a.toFile();
				
				try {
					String name = file.getName() + ".json";
					System.out.println(file);
					saveJson(name, directory, file.getAbsolutePath());
				} catch(Exception e) {
					COUNT--;
					System.out.println("Failed to convert tile : " + file);
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Dumped " + COUNT + " tile files!");
	}
	
	public static volatile int COUNT = 0;
	
	public static void saveJson(String fileName, File directory, String tilePath) {
		try(FileWriter writer = new FileWriter(new File(directory, fileName))) {
			writer.write(createJson(tilePath));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String createJson(String path) {
		// if the value is zero it's grass
		
		// bit 0 -> concrete
		// bit 1 -> sand
		// bit 2 -> stone
		// bit 3 -> dirt
		// bit 4 -> weeds
		// bit 5 -> rough stone
		// bit 6 -> hay
		// bit 7 -> bright grass
		
		// Each number in the column is 4 tiles
		// 32 bits is one number in the column
		// 8 bits -> material for tile 0
		// 8 bits -> material for tile 1
		// 8 bits -> material for tile 2
		// 8 bits -> material for tile 3
		
		
		TileImpl tile = null;
		try {
			tile = TileReader.read(path);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		long[] ground = getObject(tile, "ground");
		int size = (int)Math.sqrt(ground.length);
		// System.out.println(size);
		
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		//sb.append("\"size\":").append(size).append(",");
		sb.append("\"rows\":{");
		
//		for(int y = 1; y < size; y++) {
//			sb.append("\"").append(y).append("\":[");
//			
//			for(int x = 1; x < size; x++) {
//				int pos = x + (y * size);
//				long value = ground[pos];
//				
//				sb.append(createValue(value));
//				sb.append(",");
//			}
//			
//			sb.deleteCharAt(sb.length() - 1);
//			sb.append("],");
//		}
		
		for(int y = 1; y < size; y++) {
			sb.append("\"").append(y).append("\":[");
			
			for(int x = 0; x < (size - 1) / 4; x++) {
				int pos = (x * 4 + 1) + (y * size);
				long[] arr = {
					ground[pos + 0],
					ground[pos + 1],
					ground[pos + 2],
					ground[pos + 3]
				};
				sb.append(createValue2(arr));
				sb.append(",");
			}
			
			sb.deleteCharAt(sb.length() - 1);
			sb.append("],");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("}");
		
		
		// [ 0, 1, 2, 3, 4, 5, 6, 7 ]
		sb.append("}");
		return sb.toString();
	}
	
//	private static String createValue(long value) {
//		int result = 0;
//		for(int i = 0; i < 8; i++) {
//			int op = (int)(value & 0xff);
//			value >>>= 8;
//			
//			if(op > 30) result |= (1 << i);
//		}
//		
//		return Integer.toString(result);
//	}
	
	private static int createInt(long value) {
		int result = 0;
		for(int i = 0; i < 8; i++) {
			int op = (int)(value & 0xff);
			value >>>= 8;
			
			if(op > 30) result |= (1 << i);
		}
		
		return result;
	}
	
	private static String createValue2(long[] values) {
		int result = 0;
		for(int i = 0; i < 4; i++) {
			result |= (createInt(values[i]) << i * 8);
		}
		
		return Integer.toString(result);
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
	
	private static void debug(TileImpl tile) {
		int tileHeight = tile.getHeight();
		int tileWidth = tile.getWidth();
		int m = 2;
		
		BufferedImage bi1 = new BufferedImage(0x20 * tileWidth + 1, 0x20 * tileHeight + 1, BufferedImage.TYPE_INT_ARGB);
		BufferedImage bi2 = new BufferedImage(0x20 * tileWidth + 1, 0x20 * tileHeight + 1, BufferedImage.TYPE_INT_ARGB);
		BufferedImage bi3 = new BufferedImage(0x80 * tileWidth, 0x80 * tileHeight, BufferedImage.TYPE_INT_ARGB);
		
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
	
	@SuppressWarnings("unchecked")
	private static <T> T getObject(Object obj, String fieldName) {
		Class<?> clazz = obj.getClass();
		
		try {
			Field field = clazz.getDeclaredField(fieldName);
			boolean old = field.canAccess(obj);
			field.setAccessible(true);
			Object result = field.get(obj);
			field.setAccessible(old);
			
			return (T) result;
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
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
