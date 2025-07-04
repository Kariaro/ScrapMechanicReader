package me.hardcoded.smreader.tile.readers;

import java.util.UUID;

import me.hardcoded.smreader.data.Memory;
import me.hardcoded.smreader.utils.TileUtils;
import me.hardcoded.smreader.tile.CellHeader;
import me.hardcoded.smreader.tile.impl.AssetImpl;
import me.hardcoded.smreader.tile.impl.TilePart;

/**
 * <b>This reader is incomplete!</b>
 * 
 * @author HardCoded
 */
public class AssetListReader implements TileReaderImpl {
	
	@Override
	public void read(CellHeader h, Memory reader, TilePart part) {
		for (int i = 0; i < 4; i++) {
			int assetListCompressedSize = h.assetListCompressedSize[i];
			int assetListSize = h.assetListSize[i];
			
			TileUtils.log("    Asset[%d]       : %d / %d / %d", i, assetListSize, assetListCompressedSize, h.assetListCount[i]);
			
			if (h.assetListCount[i] != 0) {
				reader.set(h.assetListIndex[i]);
				
				byte[] compressed = reader.Bytes(assetListCompressedSize);
				byte[] bytes = new byte[assetListSize];
				
				int debugSize = TileUtils.decompress_data(compressed, bytes, assetListSize);
				if (debugSize != h.assetListCompressedSize[i]) {
					TileUtils.error("debugSize != h.assetListCompressedSize[%d]: %d != %d", i, debugSize, h.assetListCompressedSize[i]);
				}
				
				debugSize = read(bytes, i, h.assetListCount[i], part.getParent().getVersion(), part);
				if (debugSize != h.assetListSize[i]) {
					TileUtils.error("debugSize != h.assetListSize[%d]: %d != %d", i, debugSize, h.assetListSize[i]);
				}
			}
		}
	}

	public int read(byte[] bytes, int asset_index, int len, int version, TilePart part) {
		Memory memory = new Memory(bytes);
		
		int index = 0;
		for (int i = 0; i < len; i++) {
			float[] f_pos = memory.Floats(3, index);
			float[] f_quat = memory.Floats(4, index + 0xc);
			float[] f_size;
			
			if (version < 5) {
				float dim = memory.Float(index + 0x1c);
				f_size = new float[] { dim, dim, dim };
				index += 0x20;
			} else {
				f_size = memory.Floats(3, index + 0x1c);
				index += 0x28;
			}
			
			UUID uuid = null;
			AssetImpl asset = new AssetImpl();
			
			if (version < 4) {
				// int bVar4 = memory.Byte(index++);
				// String str = memory.String(bVar4, index);
				// System.out.printf("  ReadString: [%s]\n", str);
				
				// TODO: ????
				
			} else {
				uuid = memory.Uuid(index, true);
				index += 0x10;
			}
			
			int bVar4 = memory.UnsignedByte(index++);
			
			if (bVar4 != 0) {
				int length = bVar4;
				for (int j = 0; j < length; j++) {
					bVar4 = memory.UnsignedByte(index++) & 0xff;
					String str = memory.String(bVar4, index);
					
					index += bVar4;
					asset.materials.put(str, memory.Int(index, true));
					index += 4;
				}
			}
			
			asset.setPosition(f_pos /* + f_position */);
			asset.setRotation(f_quat);
			asset.setSize(f_size);
			
			if (uuid != null) {
				asset.setUuid(uuid);
			}
			
			part.addAsset(asset, asset_index);
		}
		
		return index;
	}
}
