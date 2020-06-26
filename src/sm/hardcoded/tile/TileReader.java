package sm.hardcoded.tile;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

import sm.hardcoded.tile.TileHeader.Header;

public final class TileReader {
	private static final TestFunction2 func = new TestFunction2();
	
	private TileReader() {
		
	}
	
	public static Tile read(String path) throws Exception {
		return loadTile(path);
	}
	
	public static void save(Tile tile, String path) throws IOException {
		throw new UnsupportedOperationException("Not implemented");
	}
	
	private static byte[] readBytesGrr(String path) throws IOException {
		File file = new File(path);
		DataInputStream stream = new DataInputStream(new FileInputStream(file));
		
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		
		byte[] buffer = new byte[4096];
		int readBytes = 0;
		while((readBytes = stream.read(buffer)) != -1) {
			bs.write(buffer, 0, readBytes);
		}
		
		stream.close();
		
		return bs.toByteArray();
	}
	public static Tile loadTile(String path) throws Exception {
		TileHeader header = new TileHeader(readBytesGrr(path));
		
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
		
		int tileXSize = header.width;
		int tileYSize = header.height;

		Tile tile = new Tile(tileXSize, tileYSize);
		if(tileYSize > 0) {
			byte[] bytes;
			
			int iVar12 = tileXSize;
			int iVar13 = tileYSize;
			
			int local_118 = 0;
			int local_114 = 0;
			
			for(int y = 0; y < tileYSize; y++) {
				for(int x = 0; x < tileXSize; x++) {
					Header h = header.getHeader(x, y);
					// Pointer memory = new Pointer(h.data());
					
					if(header.type == 0) {
						bytes = getMip(h, reader);
						createMip(bytes, tile, x, y);
						// FUN_00b7f560(*(void **)((int)DAT_0146b85c + 0x9c), _pbVar9, _x, _y, tileVersion);
						
						bytes = getClutter(h, reader);
						createClutter(bytes, tile, x, y);
						// TerrainGrass(*(void **)((int)DAT_0146b85c + 0xa0), _pbVar9, _x, _y, tileVersion);
					}
					
					// CalculateAssetList(h, reader);
					// CalculateNode(h, reader);
					// CalculatePrefab(h, reader);
					// CalculateBlueprintList(h, reader);
					// bytes = getDecal(h, reader);
					// createDecal(bytes, tile, h.decalSize);
					
					// CalculateHarvestableList(h, reader);
					
					System.out.println();
					
					local_114 += 0x40;
				}
				
				local_118 += 0x40;
			}
		}
		
		// System.out.println("\nCustomMemory:");
		// System.out.println(StringUtils.getHexString(customMemory.data(), 1024, 32));
		
		return tile;
	}
	
	// Prefabs or something.. Uses Matrix4x4
	private static void createDecal(byte[] bytes, Tile tile, int size) {
		if(size == 0) return;
		
		for(int i = 0; i < size; i++) {
		}
	}
	
	private static void createMip(byte[] bytes, Tile tile, int x, int y) {
		int w = 0x21, h = 0x21;
		Pointer memory = new Pointer(bytes);
		System.out.println("Max   : " + (w * h * 8 + 4));
		System.out.println("Length: " + bytes.length);
		
		float[] height = new float[w * h];
		int[] color = new int[w * h];
		for(int i = 0; i < w * h; i++) {
			height[i] = memory.Float(i * 8, false);
			color[i] = memory.Int(i * 8 + 4, false);
		}
		
		memory.set(w * h * 8);
		
		w = 0x41;
		h = 0x41;
		long[] ground = new long[0x41 * 0x41];
		for(int i = 0; i < w * h; i++) {
			ground[i] = memory.NextLong();
		}
		
		tile.writeVertexColor(x, y, color);
		tile.writeVertexHeight(x, y, height);
		tile.writeGroundMaterials(x, y, ground);
	}
	
	private static String getHexString(byte[] bytes, int start, int maxLength, int lineLength) {
		StringBuilder sb = new StringBuilder();
		int a = 1;
		for(int i = start; i < Math.min(bytes.length, start + maxLength); i++) {
			sb.append(String.format("%02x", bytes[i]));
			if((a ++) % lineLength == 0) sb.append('\n');
		}
		
		return sb.toString();
	}
	
	private static void createClutter(byte[] bytes, Tile tile, int x, int y) {
		Pointer memory = new Pointer(bytes);
		
		if(memory.Byte() != 0) {
			int length = memory.UnsignedByte();
			int offset = 0;
			
			memory.move(2);
			for(int i = 0; i < length; i++) {
				int uVar7 = 0;
				for(int j = 0; j < 0x10; j++) {
					int read = memory.Byte(offset + j);
					// uVar7 = (uVar7 ^ read) + 0x9e3779b9 + (uVar7 * 0x40) + (uVar7 >> 2);
					
					int a = (uVar7 ^ read);
					int b = 0x9e3779b9;
					int c = (uVar7 << 6);
					int d = (uVar7 >> 2);
					
					uVar7 = a + b + c + d;
				}
				UUID uuid = memory.Uuid(offset, true);
				
				System.out.printf("Clutter: %s    %08x\n", uuid, uVar7);
				//System.out.printf("%08x\n", uVar7);
				
				int iVar8 = 0;
				if(iVar8 != 1) {
					//Log("TerrainGrass", "false && \"Uuid dosen't exist in the clutter manager, defaulting to 0.\"", 149);
				} else {
					// FUN_009b8120(&local_64,local_3c,(byte *)((int)&bytes + 2));
				}
				
				offset += 0x11;
			}
			
			memory.set(offset);
		}
		
		byte[] next = new byte[128 * 128];
		{
			int offset = 0;
			
			for(int i = 0; i < 0x4000; i++) {
				byte read = memory.Byte(1 + i + offset);
				next[i] = (byte)read;
			}
			
			// System.out.println(getHexString(next, 0, 128 * 128, 128));
			tile.writeClutter(x, y, next);
		}
		
		// TODO - This is not complete...
	}
	
	// NOTE - CalculateMip
	private static byte[] getMip(Header h, Pointer reader) {
		System.out.printf("  Mip              : %d, %d\n", h.mipCompressedSize, h.mipSize);
		
		byte[] compressed = reader.set(h.mipIndex).Bytes(h.mipCompressedSize);
		byte[] bytes = new byte[h.mipSize];
		
		int debugSize = CalculateCompressedSize(compressed, bytes, h.mipSize);
		Assert(debugSize == h.mipCompressedSize, "debugSize == h.mipCompressedSize[0]", 235);
		
		return bytes;
	}
	
	// NOTE - CalculateClutter
	private static byte[] getClutter(Header h, Pointer reader) {
		System.out.printf("  Clutter          : %d\n", h.clutterCompressedSize);
		
		byte[] compressed = reader.set(h.clutterIndex).Bytes(h.clutterCompressedSize);
		byte[] bytes = new byte[h.clutterSize];
		
		int debugSize = CalculateCompressedSize(compressed, bytes, h.clutterSize);
		Assert(debugSize == h.clutterCompressedSize, "debugSize == h.clutterCompressedSize", 242);
		
		return bytes;
	}
	
	// NOTE - CalculateAssetList
	private static byte[][] CalculateAssetList(Header h, Pointer reader) {
		byte[][] bytes = new byte[4][];
		for(int i = 0; i < 4; i++) {
			int assetListCompressedSize = h.assetListCompressedSize[i];
			int assetListSize = h.assetListSize[i];
			
			System.out.printf("    Asset[%d]       : %d / %d\n", i, assetListSize, assetListCompressedSize);
			
			if(h.assetListDefined[i]) {
				reader.set(h.assetListIndex[i]);
				
				byte[] compressed = reader.Bytes(assetListCompressedSize);
				bytes[i] = new byte[assetListSize];
				
				int debugSize = CalculateCompressedSize(compressed, bytes[i], assetListSize);
				Assert(debugSize == h.assetListCompressedSize[i], "debugSize == h.assetListCompressedSize[" + i + "]", 254);
				
				// TODO
				// FUN_00b8cf70((int *)&local_1a4, (int)_pbVar9, (int *)&local_124, &local_18c, ((uint *)puVar18)[-8], tileVersion);
				// Assert(debugSize == h.assetListSize[i], "debugSize == h.assetListSize[" + i + "]", 256);
			}
		}
		
		System.out.println();
		
		return bytes;
	}
	
	// NOTE - CalculateNode
	private static byte[] CalculateNode(Header h, Pointer reader) {
		if((h.bytes_a4 == 0) || (h.bytes_a8 == 0)) return null;
		reader.set(h.bytes_a8);
		
		System.out.printf("  Node             : %d / %d\n", h.nodeSize, h.nodeCompressedSize);
		
		byte[] compressed = reader.Bytes(h.nodeCompressedSize);
		byte[] bytes = new byte[h.nodeSize];
		
		int debugSize = CalculateCompressedSize(compressed, bytes, h.nodeSize);
		Assert(debugSize == h.nodeCompressedSize, "debugSize == h.nodeCompressedSize", 266);
		Assert(debugSize == h.nodeSize, "debugSize == h.nodeSize", 268);
		
		return bytes;
	}
	
	// NOTE - CalculatePrefab
	private static byte[] CalculatePrefab(Header h, Pointer reader) {
		if((h.bytes_c4 == 0) || (h.bytes_c8 == 0)) return null;
		reader.set(h.bytes_c8);
		
		System.out.printf("  Prefab           : %d / %d\n", h.prefabSize, h.prefabCompressedSize);
		
		byte[] compressed = reader.Bytes(h.prefabCompressedSize);
		byte[] bytes = new byte[h.prefabSize];
		
		int debugSize = CalculateCompressedSize(compressed, bytes, h.prefabSize);
		Assert(debugSize == h.prefabCompressedSize, "debugSize == h.prefabCompressedSize", 277);
		Assert(debugSize == h.prefabSize, "debugSize == h.prefabSize", 279);
		
		return bytes;
	}
	
	// NOTE - CalculateBlueprintList
	private static byte[] CalculateBlueprintList(Header h, Pointer reader) {
		if((h.bytes_94 == 0) || (h.bytes_98 == 0)) return null;
		reader.set(h.bytes_98);
		
		System.out.printf("  BlueprintList    : %d / %d\n", h.blueprintListSize, h.blueprintListCompressedSize);
		
		byte[] compressed = reader.Bytes(h.blueprintListCompressedSize);
		byte[] bytes = new byte[h.blueprintListSize];
		
		int debugSize = CalculateCompressedSize(compressed, bytes, h.blueprintListSize);
		Assert(debugSize == h.blueprintListCompressedSize, "debugSize == h.blueprintListCompressedSize", 290);
		Assert(debugSize == h.blueprintListSize, "debugSize == h.blueprintListSize", 292);
		
		return bytes;
	}
	
	// NOTE - CalculateDecal
	private static byte[] getDecal(Header h, Pointer reader) {
		if((h.bytes_d4 == 0) || (h.bytes_d8 == 0)) return null;
		reader.set(h.bytes_d8);
		
		System.out.printf("  Decal            : %d / %d\n", h.decalSize, h.decalCompressedSize);
		
		byte[] compressed = reader.Bytes(h.decalCompressedSize);
		byte[] bytes = new byte[h.decalSize];
		
		int debugSize = CalculateCompressedSize(compressed, bytes, h.decalSize);
		Assert(debugSize == h.decalCompressedSize, "debugSize == h.decalCompressedSize", 301);
		Assert(debugSize == h.decalSize, "debugSize == h.decalSize", 303);
		
		return bytes;
	}
	
	// NOTE - CalculateHarvestableList
	private static byte[][] CalculateHarvestableList(Header h, Pointer reader) {
		byte[][] bytes = new byte[4][];
		
		for(int i = 0; i < 4; i++) {
			int harvestableListCompressedSize = h.harvestableListCompressedSize[i];
			int harvestableListSize = h.harvestableListSize[i];
			
			System.out.printf("    Harvestable[%d] : %d / %d\n", i, harvestableListSize, harvestableListCompressedSize);
			
			if(h.harvestableListDefined[i]) {
				reader.set(h.harvestableListIndex[i]);
				
				byte[] compressed = reader.Bytes(harvestableListCompressedSize);
				bytes[i] = new byte[harvestableListSize];
				
				int debugSize = CalculateCompressedSize(compressed, bytes[i], h.harvestableListSize[i]);
				Assert(debugSize == h.harvestableListCompressedSize[i], "debugSize == h.harvestableListCompressedSize[" + i + "]", 314);
				Assert(debugSize == h.harvestableListSize[i], "debugSize == h.harvestableListSize[" + i + "]", 316);
			}
		}
		
		return bytes;
	}
	
	private static int CalculateCompressedSize(byte[] compressed, byte[] bytes, int size) {
		int result = func.decompress(compressed, bytes, size);
		//TestFunction FUNC = new TestFunction();
		//int result = (int)FUNC.decompress(compressed, bytes, size);
		return (int)result;
	}
	
	public static void Assert(boolean value, String message, int lineIndex) {
		if(value) return;
		
		String msg = String.format("ERROR: ASSERT: '%s' : Z:\\Jenkins\\workspace\\sm\\TileEditorCommon\\Tile.cpp:%d\n", message, lineIndex);
		System.err.println(msg);
		throw new AssertionError(msg, null);
	}
	
	public static void Assert(boolean value, String name, String message, int lineIndex) {
		if(value) return;
		
		String msg = String.format("ERROR: ASSERT: '%s' : Z:\\Jenkins\\workspace\\sm\\TileEditorCommon\\%s.cpp:%d\n", message, name, lineIndex);
		System.err.println(msg);
		throw new AssertionError(msg, null);
	}
	
	public static void Log(String name, String message, int lineIndex) {
		String msg = String.format("INFO: '%s' : Z:\\Jenkins\\workspace\\sm\\TileEditorCommon\\%s.cpp:%d\n", message, name, lineIndex);
		System.err.println(msg);
	}
}
