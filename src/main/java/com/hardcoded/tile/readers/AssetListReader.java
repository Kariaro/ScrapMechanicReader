package com.hardcoded.tile.readers;

import com.hardcoded.data.Memory;
import com.hardcoded.tile.TileHeader.Header;
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
	public void read(Header h, Memory reader, TilePart part) {
		byte[][] bytes = new byte[4][];
		for(int i = 0; i < 4; i++) {
			int assetListCompressedSize = h.assetListCompressedSize[i];
			int assetListSize = h.assetListSize[i];
			
			TileUtils.log("    Asset[%d]       : %d / %d", i, assetListSize, assetListCompressedSize);
			
			if(h.assetListDefined[i] != 0) {
				reader.set(h.assetListIndex[i]);
				
				byte[] compressed = reader.Bytes(assetListCompressedSize);
				bytes[i] = new byte[assetListSize];
				
				int debugSize = TileUtils.decompress_data(compressed, bytes[i], assetListSize);
				if(debugSize != h.assetListCompressedSize[i]) {
					TileUtils.error("debugSize != h.assetListCompressedSize[%d]", i); // 254
				}
				
				debugSize = read(bytes[i], h.assetListDefined[i], part.parent.getVersion(), part);
				if(debugSize != h.assetListSize[i]) {
					TileUtils.error("debugSize != h.assetListSize[%d]", i); // 256
				}
			}
		}
	}

	public int read(byte[] bytes, int len, int version, TilePart part) {
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

			AssetImpl asset = new AssetImpl();
			
			//float[] local_e8 = new float[4];
			if(version < 4) {
				// int bVar4 = memory.Byte(index++);
				// String str = memory.String(bVar4, index);
				// System.out.printf("  ReadString: [%s]\n", str);
				
				// TODO: ????
				
			} else {
				//local_e8 = memory.Floats(4, index);
				//System.out.printf("local_e8: %.8f, %.8f, %.8f, %.8f\n", local_e8[0], local_e8[1], local_e8[2], local_e8[3]);
				index += 0x10;
			}
			
			int bVar4 = memory.Byte(index++);
			
			// Asset size 0xa0
			if(bVar4 != 0) {
				int length = bVar4;
				for(int local_2f8 = 0; local_2f8 < length; local_2f8++) {
					bVar4 = memory.UnsignedByte(index++) & 0xff;
					String str = memory.String(bVar4, index);
					// System.out.printf("  : [%s]\n", str);
					asset.materials.add(str);
					
					index += bVar4;
					// int local_2b4 = memory.Int(index);
					index += 4;
					
					// local_2f8 += 1;
				}
			}
			
			asset.pos[0] = f_pos[0]; // + position[0]
			asset.pos[1] = f_pos[1]; // + position[1]
			asset.pos[2] = f_pos[2]; // + position[2]
			
			asset.quat[0] = f_quat[0];
			asset.quat[1] = f_quat[1];
			asset.quat[2] = f_quat[2];
			asset.quat[3] = f_quat[3];
			
			asset.size[0] = f_size[0];
			asset.size[1] = f_size[1];
			asset.size[2] = f_size[2];
			
			part.addAsset(asset);
			
//			{
//				float[] pos = asset.pos;
//				float[] size = asset.size;
//				float[] quat = asset.quat;
//				
//				System.out.printf("pos : %.8f, %.8f, %.8f\n", pos[0], pos[1], pos[2]);
//				System.out.printf("quat: %.8f, %.8f, %.8f, %.8f\n", quat[0], quat[1], quat[2], quat[3]);
//				System.out.printf("size: %.8f, %.8f, %.8f\n", size[0], size[1], size[2]);
//				System.out.printf("mats: %s\n", asset.materials);
//				System.out.println();
//			}
		}
		
		return index;
	}
}
