package com.hardcoded.prefab.readers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.hardcoded.data.BitStream;
import com.hardcoded.data.Memory;
import com.hardcoded.error.TileException;
import com.hardcoded.tile.impl.AssetImpl;
import com.hardcoded.tile.impl.NodeImpl;
import com.hardcoded.tile.impl.PrefabImpl;
import com.hardcoded.tile.object.TilePrefab;

/**
 * This class is used to read {@code .PREFAB} files.
 * 
 * @author HardCoded
 * @since v0.2
 */
public class PrefabFileReader {
	private static final PrefabFileReader reader = new PrefabFileReader();
	
	private PrefabFileReader() {
		
	}
	
	public static TilePrefab readPrefab(String path) throws TileException, IOException {
		if(path == null) throw new NullPointerException("File path was null");
		byte[] bytes = Files.readAllBytes(Path.of(path));
		return reader.readPrefab(bytes);
	}
	
	public static TilePrefab readPrefab(File file) throws TileException, IOException {
		if(file == null) throw new NullPointerException("File path was null");
		byte[] bytes = Files.readAllBytes(Path.of(file.toURI()));
		return reader.readPrefab(bytes);
	}
	
	public TilePrefab readPrefab(byte[] bytes) throws TileException {
		PrefabImpl prefab = new PrefabImpl(true);
		
		Memory reader = new Memory(bytes);
		String magic = reader.NextString(4, true);
		if(!magic.equals("PREF")) {
			throw new TileException("Prefab file magic value was wrong. Should be 'PREF'");
		}
		
		int version = reader.NextInt(true);
		BitStream stream = new BitStream(reader);
		
		PrefabHeader header = new PrefabHeader(stream, version);
		header.read();
		
		if(header.hasBlueprints != 0) {
			read_blueprints(stream, prefab, header.blueprintCount);
		}
		
		if(header.hasPrefabs != 0) {
			read_prefabs(stream, prefab, header.prefabCount);
		}
		
		if(header.hasNodes != 0) {
			read_nodes(stream, prefab, header.nodeCount);
		}
		
		if(header.hasAssets != 0) {
			read_assets(stream, prefab, header.assetCount);
		}
		
		if(header.hasDecals != 0) {
			read_decals(stream, prefab, header.decalsCount);
		}
		
		if(header.has_0x5c != 0) {
			read_248(stream, prefab, header.count_0x54);
		}
		
		return prefab;
	}
	
	private void read_blueprints(BitStream stream, PrefabImpl prefab, int count) {
		// TileUtils.debugPrint("read_blueprints", stream);
		
		for(int i = 0; i < count; i++) {
			int string_length = stream.readInt();
			String path = stream.readString(string_length);
			int bVar4 = 0;
			bVar4 = stream.readInt();
			bVar4 = stream.readInt();
			bVar4 = stream.readInt();
			bVar4 = stream.readInt();
			bVar4 = stream.readInt();
			bVar4 = stream.readInt();
			bVar4 = stream.readInt();
			bVar4 = stream.readInt();
			
			//TileUtils.log("read_blueprints: '%s'", path);
			prefab.blueprint_paths.add(path);
		}
	}
	
	private void read_prefabs(BitStream stream, PrefabImpl prefab, int count) {
		// TileUtils.debugPrint("read_prefabs", stream);
		
		for(int i = 0; i < count; i++) {
			int string_length = stream.readInt();
			String path = stream.readString(string_length);
			
			int bVar4 = 0;
			bVar4 = stream.readInt();
			bVar4 = stream.readInt();
			bVar4 = stream.readInt();
			bVar4 = stream.readInt();
			bVar4 = stream.readInt();
			bVar4 = stream.readInt();
			bVar4 = stream.readInt();
			bVar4 = stream.readInt();
			bVar4 = stream.readInt();
			
			bVar4 = stream.readInt();
			bVar4 = stream.readInt();
			bVar4 = stream.readInt();
			
			//TileUtils.log("read_prefabs: '%s'", path);
			prefab.prefabs_paths.add(path);
		}
	}
	
	private void read_nodes(BitStream stream, PrefabImpl prefab, int count) {
		// TileUtils.debugPrint("read_nodes", stream);
		
		int uVar2 = stream.readByte();
		List<String> tags = new ArrayList<>();
		if(uVar2 != 0) {
			for(int i = 0; i < uVar2; i++) {
				int size = stream.readByte();
				tags.add(stream.readString(size));
			}
		}
		
		for(int i = 0; i < count; i++) {
			float[] f_pos = { stream.readFloat(), stream.readFloat(), stream.readFloat() };
			float[] f_quat = { stream.readFloat(), stream.readFloat(), stream.readFloat(), stream.readFloat() };
			float[] f_size = { stream.readFloat(), stream.readFloat(), stream.readFloat() };

			NodeImpl node = new NodeImpl(tags);
			node.setPosition(f_pos /* + f_position */);
			node.setRotation(f_quat);
			node.setSize(f_size);
			
			int bVar2 = stream.readByte();
			if(bVar2 != 0) {
				for(int j = 0; j < bVar2; j++) {
					int idx = stream.readByte();
					String tag_name = tags.get(idx);
					node.setTagState(tag_name, true);
				}
			}
			
			int uVar3 = stream.readInt();
			if(uVar3 != 0) {
				stream.readBytes(uVar3);
				//Object deserialized = LuaDeserializer.DeserializePure(blob);
			}
			
			prefab.addNode(node);
		}
		
		//TileUtils.log("read_nodes: TAGS=%s", tags);
	}
	
	private void read_assets(BitStream stream, PrefabImpl prefab, int count) {
		// TileUtils.debugPrint("read_assets", stream);
		
		for(int i = 0; i < count; i++) {
			float[] f_pos = { stream.readFloat(), stream.readFloat(), stream.readFloat() };
			float[] f_quat = { stream.readFloat(), stream.readFloat(), stream.readFloat(), stream.readFloat() };
			float[] f_size = { stream.readFloat(), stream.readFloat(), stream.readFloat() };
			
			AssetImpl asset = new AssetImpl();
			
			UUID uuid = stream.readUuid();
			final int materialCount = stream.readByte();
			
			if(materialCount != 0) {
				for(int j = 0; j < materialCount; j++) {
					int length = stream.readByte();
					String str = stream.readString(length);
					asset.materials.put(str, stream.readInt());
				}
			}
			
			asset.setPosition(f_pos);
			asset.setRotation(f_quat);
			asset.setSize(f_size);
			asset.setUuid(uuid);
			
			prefab.addAsset(asset);
		}
	}
	
	private void read_decals(BitStream stream, PrefabImpl prefab, int count) {
		// TileUtils.debugPrint("read_decals", stream);
		
	}
	
	private void read_248(BitStream stream, PrefabImpl prefab, int count) {
		// TileUtils.debugPrint("read_248", stream);
		
	}
}
