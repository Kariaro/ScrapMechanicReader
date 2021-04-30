package com.hardcoded.test;

import java.awt.Window.Type;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Field;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.hardcoded.tile.Tile;
import com.hardcoded.tile.TileReader;

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
		
		
		Tile tile = null;
		try {
			String path = "D:\\Steam\\steamapps\\common\\Scrap Mechanic\\Survival\\Terrain\\Tiles\\start_area\\SurvivalStartArea_BigRuin_01.tile";
			path = getGameTile("GROUND512_01");
			path = "D:/Steam/steamapps/common/Scrap Mechanic/Data/LocalPrefabs/Warehouse_Room_Toilets_2x2x1_04.prefab";
			path = "C:/Users/Admin/Downloads/warehouse3prefabbuttile.tile";
			//path = "C:/Users/Admin/Downloads/Warehouse_Interior_EncryptorFloor_01.tile";
			//path = getGameTile("HILLS512_01");
			
			//path = getGameTile("Road_Dirt_Three-way_01");
			
			tile = TileReader.readTileFromPath(path);
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
