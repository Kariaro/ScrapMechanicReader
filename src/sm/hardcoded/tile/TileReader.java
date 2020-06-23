package sm.hardcoded.tile;

import java.io.File;
import java.io.IOException;

import sm.hardcoded.test.FileUtils;
import sm.hardcoded.tile.TileHeader.Header;

public final class TileReader {
	private static final TestFunction func = new TestFunction();
	
	private TileReader() {
		
	}
	
	public static TileObject read(String path) throws IOException {
		throw new UnsupportedOperationException("Not implemented");
	}
	
	public static void save(TileObject tile, String path) throws IOException {
		throw new UnsupportedOperationException("Not implemented");
	}
	
	public static void main(String[] args) {
		String path = getTile("tile_9");
		// path = getTile("tile_5");
		// path = getTile("tile_1");
		// path = getTile("TESTING_TILE_FLAT");
		// path = getTile("TESTING_TILE_FLAT_MEDIUM");
		// path = getTile("TESTING_TILE_FLAT_MEDIUM_COLORFLAT_2");
		// path = getTile("TESTING_TILE_FLAT_MEDIUM_COLORFLAT");
		
		// path = getGameTile("MEADOW128_09");
		// path = "D:\\Steam\\steamapps\\common\\Scrap Mechanic\\Survival\\Terrain\\Tiles\\start_area\\SurvivalStartArea_BigRuin_01.tile";
		
		try {
			loadTile(path);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		// System.out.println("\nCustomMemory:");
		// System.out.println(StringUtils.getHexString(customMemory.data(), 1024, 32));
	}
	
	
	private static String getTile(String name) {
		File tile_path = new File("C:/Users/Admin/AppData/Roaming/Axolot Games/Scrap Mechanic/User/User_76561198251506208/Tiles/");
		
		for(File dir_file : tile_path.listFiles()) {
			for(File file : dir_file.listFiles()) {
				if(file.getName().equals(name + ".tile")) return file.getAbsolutePath();
			}
		}
		
		return null;
	}
	
	private static String getGameTile(String name) {
		File tile_path = new File("D:/Steam/steamapps/common/Scrap Mechanic/Data/Terrain/Tiles/");
		
		for(File file : tile_path.listFiles()) {
			if(file.getName().equals(name + ".tile")) return file.getAbsolutePath();
		}
		
		return null;
	}
	
	private static final Pointer customMemory = null;
	
	public static TileObject loadTile(String path) throws Exception {
		TileHeader header = new TileHeader(FileUtils.readFileBytes(path));
		
		System.out.printf("TileFileVersion: %d\n", header.version);
		System.out.printf("TileUuid: {%s}\n", header.uuid);
		System.out.printf("CreatorId: %d\n", header.creatorId);
		System.out.printf("Size: %d, %d\n", header.width, header.height);
		System.out.printf("Type: %d\n\n", header.type);
		System.out.println("Header info:");
		System.out.printf("CellHeadersOffset: %d\n", header.cellHeadersOffset);
		System.out.printf("CellHeadersSize: %d\n\n", header.cellHeadersSize);
		System.out.println();
		
		System.out.println("Headers:");
		
		for(int i = 0; i < header.width * header.height; i++) {
			int x = i % header.width;
			int y = i / header.height;
			
			byte[] bytes = header.getHeader(x, y).data();
			System.out.printf("	BLOB(%d, %d):\n", x, y);
			System.out.printf("		%s\n\n", StringUtils.getHexString(bytes, header.cellHeadersSize, 32).replace("\n", "\n		"));
		}
		
		System.out.println();
		System.out.println("Reading header data:");
		
		Pointer reader = new Pointer(header.data());
		Pointer customMemory = new Pointer(1000000);
		
		
		int tileXSize = header.width;
		int tileYSize = header.height;
		
		if(tileYSize > 0) {
			int iVar12 = tileXSize;
			int iVar13 = tileYSize;
			
			int local_118 = 0;
			int local_114 = 0;
			
			for(int y = 0; y < tileYSize; y++) {
				for(int x = 0; x < tileXSize; x++) {
					Header h = header.getHeader(x, y);
					
					// Pointer memory = new Pointer(h.data());
					
					if(header.type == 0) {
						byte[] bytes = CalculateMip(h, reader);
						customMemory.WriteBytes(bytes);
						
						// FUN_00b7f560(*(void **)((int)DAT_0146b85c + 0x9c), _pbVar9, _x, _y, tileVersion);
						
						// CalculateClutter(h, reader, memory);
						// TerrainGrass(*(void **)((int)DAT_0146b85c + 0xa0), _pbVar9, _x, _y, tileVersion);
					}
					
					// CalculateAssetList(h, reader, memory);
					// CalculateNode(h, reader, memory);
					// CalculatePrefab(h, reader, memory);
					// CalculateBlueprintList(h, reader, memory);
					// CalculateDecal(h, reader, memory);
					// CalculateHarvestableList(h, reader, memory);
					
					System.out.println();
					
					local_114 += 0x40;
				}
				
				local_118 += 0x40;
			}
			
		}
		
		System.out.println("\nCustomMemory:");
		System.out.println(StringUtils.getHexString(customMemory.data(), 1024, 32));
		
		return null;
	}
	
	
	// NOTE - CalculateMip
	private static byte[] CalculateMip(Header h, Pointer reader) {
		System.out.printf("  Mip              : %d, %d\n", h.mipCompressedSize, h.mipSize);
		
		byte[] compressed = reader.set(h.mipIndex).Bytes(h.mipCompressedSize);
		byte[] bytes = new byte[h.mipSize];
		
		int debugSize = CalculateCompressedSize(compressed, bytes, h.mipSize);
		Assert(debugSize == h.mipCompressedSize, "debugSize == h.mipCompressedSize[0]", 235);
		
		return bytes;
	}
	
	// NOTE - CalculateClutter
	private static byte[] CalculateClutter(Header h, Pointer reader) {
		System.out.printf("  Clutter          : %d\n", h.clutterCompressedSize);
		
		byte[] compressed = reader.set(h.clutterIndex).Bytes(h.clutterCompressedSize);
		byte[] bytes = new byte[h.clutterSize];
		
		int debugSize = CalculateCompressedSize(compressed, bytes, h.clutterSize);
		Assert(debugSize == h.clutterCompressedSize, "debugSize == h.clutterCompressedSize", 242);
		
		return bytes;
	}
	
	// NOTE - CalculateAssetList
	private static boolean CalculateAssetList(Header h, Pointer reader) {
		for(int i = 0; i < 4; i++) {
			int assetListCompressedSize = h.assetListCompressedSize[i];
			int assetListSize = h.assetListSize[i];
			
			System.out.printf("    Asset[%d]       : %d / %d\n", i, assetListSize, assetListCompressedSize);
			
			if(h.assetListDefined[i]) {
				reader.set(h.assetListIndex[i]);
				
				byte[] bytes = reader.Bytes(assetListCompressedSize);
				int debugSize = CalculateCompressedSize(bytes, customMemory, assetListSize);
				Assert(debugSize == h.assetListCompressedSize[i], "debugSize == h.assetListCompressedSize[" + i + "]", 254);
				
				// TODO
				// FUN_00b8cf70((int *)&local_1a4, (int)_pbVar9, (int *)&local_124, &local_18c, ((uint *)puVar18)[-8], tileVersion);
				// Assert(debugSize == h.assetListSize[i], "debugSize == h.assetListSize[" + i + "]", 256);
			}
		}
		System.out.println();
		
		return true;
	}
	
	// NOTE - CalculateNode
	private static boolean CalculateNode(Header h, Pointer reader) {
		if((h.bytes_a4 == 0) || (h.bytes_a8 == 0)) return false;
		reader.set(h.bytes_a8);
		
		System.out.printf("  Node             : %d / %d\n", h.nodeSize, h.nodeCompressedSize);
		
		byte[] bytes = reader.Bytes(h.nodeCompressedSize);
		int debugSize = CalculateCompressedSize(bytes, customMemory, h.nodeSize);
		Assert(debugSize == h.nodeCompressedSize, "debugSize == h.nodeCompressedSize", 266);
		Assert(debugSize == h.nodeSize, "debugSize == h.nodeSize", 268);
		
		return true;
	}
	
	// NOTE - CalculatePrefab
	private static boolean CalculatePrefab(Header h, Pointer reader) {
		if((h.bytes_c4 == 0) || (h.bytes_c8 == 0)) return false;
		reader.set(h.bytes_c8);
		
		System.out.printf("  Prefab           : %d / %d\n", h.prefabSize, h.prefabCompressedSize);
		
		byte[] bytes = reader.Bytes(h.prefabCompressedSize);
		int debugSize = CalculateCompressedSize(bytes, customMemory, h.prefabSize);
		Assert(debugSize == h.prefabCompressedSize, "debugSize == h.prefabCompressedSize", 277);
		Assert(debugSize == h.prefabSize, "debugSize == h.prefabSize", 279);
		
		return true;
	}
	
	// NOTE - CalculateBlueprintList
	private static boolean CalculateBlueprintList(Header h, Pointer reader) {
		if((h.bytes_94 == 0) || (h.bytes_98 == 0)) return false;
		reader.set(h.bytes_98);
		
		System.out.printf("  BlueprintList    : %d / %d\n", h.blueprintListSize, h.blueprintListCompressedSize);
		
		byte[] bytes = reader.Bytes(h.blueprintListCompressedSize);
		int debugSize = CalculateCompressedSize(bytes, customMemory, h.blueprintListSize);
		Assert(debugSize == h.blueprintListCompressedSize, "debugSize == h.blueprintListCompressedSize", 290);
		Assert(debugSize == h.blueprintListSize, "debugSize == h.blueprintListSize", 292);
		
		return true;
	}
	
	// NOTE - CalculateDecal
	private static boolean CalculateDecal(Header h, Pointer reader) {
		if((h.bytes_d4 == 0) || (h.bytes_d8 == 0)) return false;
		reader.set(h.bytes_d8);
		
		System.out.printf("  Decal            : %d / %d\n", h.decalSize, h.decalCompressedSize);
		
		byte[] bytes = reader.Bytes(h.decalCompressedSize);
		int debugSize = CalculateCompressedSize(bytes, customMemory, h.decalSize);
		Assert(debugSize == h.decalCompressedSize, "debugSize == h.decalCompressedSize", 301);
		Assert(debugSize == h.decalSize, "debugSize == h.decalSize", 303);
		
		return true;
	}
	
	// NOTE - CalculateHarvestableList
	private static boolean CalculateHarvestableList(Header h, Pointer reader) {
		for(int i = 0; i < 4; i++) {
			int harvestableListCompressedSize = h.harvestableListCompressedSize[i];
			int harvestableListSize = h.harvestableListSize[i];
			
			System.out.printf("    Harvestable[%d] : %d / %d\n", i, harvestableListSize, harvestableListCompressedSize);
			
			if(h.harvestableListDefined[i]) {
				reader.set(h.harvestableListIndex[i]);
				
				byte[] bytes = reader.Bytes(harvestableListCompressedSize);
				int debugSize = CalculateCompressedSize(bytes, customMemory, h.harvestableListSize[i]);
				Assert(debugSize == h.harvestableListCompressedSize[i], "debugSize == h.harvestableListCompressedSize[" + i + "]", 314);
				Assert(debugSize == h.harvestableListSize[i], "debugSize == h.harvestableListSize[" + i + "]", 316);
			}
		}
		
		return true;
	}
	
	private static int CalculateCompressedSize(byte[] compressed, Pointer param_2, int size) {
		byte[] bytes = new byte[1000000];
		int result = func.decompress(compressed, bytes, size);
		
		param_2.set(0).WriteBytes(bytes);
		
		return (int)result;
	}
	
	private static int CalculateCompressedSize(byte[] compressed, byte[] bytes, int size) {
		int result = func.decompress(compressed, bytes, size);
		return (int)result;
	}
	
	public static void Assert(boolean value, String message, int lineIndex) {
		if(value) return;
		
		String msg = String.format("ERROR: ASSERT: '%s' : Z:\\Jenkins\\workspace\\sm\\TileEditorCommon\\Tile.cpp:%d\n", message, lineIndex);
		System.err.println(msg);
		throw new AssertionError(msg, null);
	}
}
