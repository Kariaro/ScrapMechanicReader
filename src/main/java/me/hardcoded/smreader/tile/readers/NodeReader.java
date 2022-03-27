package me.hardcoded.smreader.tile.readers;

import java.util.ArrayList;
import java.util.List;

import me.hardcoded.smreader.data.Memory;
import me.hardcoded.smreader.utils.TileUtils;
import me.hardcoded.smreader.tile.CellHeader;
import me.hardcoded.smreader.tile.impl.NodeImpl;
import me.hardcoded.smreader.tile.impl.TilePart;

/**
 * <b>This reader is incomplete!</b>
 * 
 * @author HardCoded
 */
public class NodeReader implements TileReaderImpl {
	
	@Override
	public void read(CellHeader h, Memory reader, TilePart part) {
		if ((h.nodeCount == 0) || (h.nodeIndex == 0)) return;
		reader.set(h.nodeIndex);
		
		TileUtils.log("  Node             : %d / %d", h.nodeSize, h.nodeCompressedSize);
		
		byte[] compressed = reader.Bytes(h.nodeCompressedSize);
		byte[] bytes = new byte[h.nodeSize];
		
		int debugSize = TileUtils.decompress_data(compressed, bytes, h.nodeSize);
		if (debugSize != h.nodeCompressedSize) {
			TileUtils.error("debugSize != h.nodeCompressedSize: %d != %d", debugSize, h.nodeCompressedSize);
		}
		
		debugSize = read(bytes, h.nodeCount, part);
		if (debugSize != h.nodeSize) {
			TileUtils.error("debugSize != h.nodeSize: %d != %d", debugSize, h.nodeSize);
		}
		
		return;
	}
	
	public int read(byte[] bytes, int nodeCount, TilePart part) {
		int index = 0;
		Memory memory = new Memory(bytes);
		
		int uVar2 = memory.UnsignedByte(index++);
		List<String> tags = new ArrayList<>();
		if (uVar2 != 0) {
			for (int i = 0; i < uVar2; i++) {
				int size = memory.UnsignedByte(index++);
				tags.add(memory.String(size, index));
				index += size;
			}
		}
		
		if (nodeCount != 0) {
			for (int i = 0; i < nodeCount; i++) {
				float[] f_pos = memory.Floats(3, index);
				float[] f_quat = memory.Floats(4, index + 0xc);
				float[] f_size = memory.Floats(3, index + 0x1c);
				
				NodeImpl node = new NodeImpl(tags);
				node.setPosition(f_pos /* + f_position */);
				node.setRotation(f_quat);
				node.setSize(f_size);
				
				index += 0x28;
				int bVar2 = memory.UnsignedByte(index++);
				
				if (bVar2 != 0) {
					for (int j = 0; j < bVar2; j++) {
						int idx = memory.UnsignedByte(index++);
						String tag_name = tags.get(idx);
						node.setTagState(tag_name, true);
					}
				}
				
				int uVar3 = memory.Int(index);
				index += 4;
				if (uVar3 != 0) {
					@SuppressWarnings("unused")
					Memory blob = new Memory(memory.Bytes(uVar3, index));
					index += uVar3;
					
					//TileUtils.debugPrint("LuaData", blob);
					//Object deserialized = LuaDeserializer.DeserializePure(blob);
				}
				
				part.addNode(node);
			}
		}
		
		// TileUtils.log("tags = %s", tags);
		
		return index;
	}
}
