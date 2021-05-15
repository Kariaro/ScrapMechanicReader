package com.hardcoded.tile.readers;

import java.util.UUID;

import com.hardcoded.data.Memory;
import com.hardcoded.tile.CellHeader;
import com.hardcoded.tile.impl.AssetImpl;
import com.hardcoded.tile.impl.TilePart;
import com.hardcoded.utils.TileUtils;

/**
 * <b>This reader is incomplete!</b>
 * 
 * @author HardCoded
 */
public class AssetListReader implements TileReaderImpl {
	
	@Override
	public void read(CellHeader h, Memory reader, TilePart part) {
		byte[][] bytes = new byte[4][];
		for(int i = 0; i < 4; i++) {
			int assetListCompressedSize = h.assetListCompressedSize[i];
			int assetListSize = h.assetListSize[i];
			
			TileUtils.log("    Asset[%d]       : %d / %d", i, assetListSize, assetListCompressedSize);
			
			if(h.assetListCount[i] != 0) {
				reader.set(h.assetListIndex[i]);
				
				byte[] compressed = reader.Bytes(assetListCompressedSize);
				bytes[i] = new byte[assetListSize];
				
				int debugSize = TileUtils.decompress_data(compressed, bytes[i], assetListSize);
				if(debugSize != h.assetListCompressedSize[i]) {
					TileUtils.error("debugSize != h.assetListCompressedSize[%d]: %d != %d", i, debugSize, h.assetListCompressedSize[i]);
				}
				
				debugSize = read(bytes[i], i, h.assetListCount[i], part.getParent().getVersion(), part);
				if(debugSize != h.assetListSize[i]) {
					TileUtils.error("debugSize != h.assetListSize[%d]: %d != %d", i, debugSize, h.assetListSize[i]);
				}
			}
		}
	}

	public int read(byte[] bytes, int asset_index, int len, int version, TilePart part) {
		Memory memory = new Memory(bytes);
		// System.out.println(new String(bytes));
		
		int index = 0;
		for(int i = 0; i < len; i++) {
			float[] f_pos = memory.Floats(3, index); // 12 bytes
			float[] f_quat = memory.Floats(4, index + 0xc); // 16 bytes
			float[] f_size;
			
			if(version < 5) {
				float dim = memory.Float(index + 0x1c);
				f_size = new float[] { dim, dim, dim };
				index += 0x20;
			} else {
				f_size = memory.Floats(3, index + 0x1c);
				index += 0x28;
			}
			
			UUID uuid = null;
			AssetImpl asset = new AssetImpl();
			
			//float[] local_e8 = new float[4];
			if(version < 4) {
				// int bVar4 = memory.Byte(index++);
				// String str = memory.String(bVar4, index);
				// System.out.printf("  ReadString: [%s]\n", str);
				
				// TODO: ????
				
			} else {
				//local_e8 = memory.Floats(4, index);
				uuid = memory.Uuid(index, true);
				// UUID???
				//System.out.printf("local_e8: %.8f, %.8f, %.8f, %.8f\n", local_e8[0], local_e8[1], local_e8[2], local_e8[3]);
				index += 0x10;
			}
			
			int bVar4 = memory.UnsignedByte(index++);
			
			// Asset size 0xa0
			if(bVar4 != 0) {
				int length = bVar4;
				for(int j = 0; j < length; j++) {
					bVar4 = memory.UnsignedByte(index++) & 0xff;
					String str = memory.String(bVar4, index);
					
					index += bVar4;
					asset.materials.put(str, memory.Int(index));
					index += 4;
				}
			}
			
			
			asset.setPosition(f_pos /* + f_position */);
			asset.setRotation(f_quat);
			asset.setSize(f_size);
			
			if(uuid != null) {
				asset.setUuid(uuid);
			}
			
			part.addAsset(asset, asset_index);
			
//			{
//				System.out.printf("  pos : %s\n", asset.pos);
//				System.out.printf("  rot : %s\n", asset.rot);
//				System.out.printf("  size: %s\n", asset.size);
//				System.out.printf("  mats: %s\n", asset.materials);
//				System.out.printf("  uuid: %s\n", asset.uuid);
//				System.out.println();
//			}
		}
		
		return index;
	}
}
