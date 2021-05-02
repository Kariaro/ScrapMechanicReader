package com.hardcoded.tile.readers;

import java.util.ArrayList;
import java.util.List;

import com.hardcoded.data.Memory;
import com.hardcoded.tile.CellHeader;
import com.hardcoded.tile.impl.NodeImpl;
import com.hardcoded.tile.impl.TilePart;
import com.hardcoded.utils.TileUtils;

/**
 * <b>This reader is incomplete!</b>
 * 
 * @author HardCoded
 */
public class NodeReader implements TileReaderImpl {
	
	@Override
	public void read(CellHeader h, Memory reader, TilePart part) {
		if((h.nodeCount == 0) || (h.nodeIndex == 0)) return;
		reader.set(h.nodeIndex);
		
		TileUtils.log("  Node             : %d / %d", h.nodeSize, h.nodeCompressedSize);
		
		byte[] compressed = reader.Bytes(h.nodeCompressedSize);
		byte[] bytes = new byte[h.nodeSize];
		
		int debugSize = TileUtils.decompress_data(compressed, bytes, h.nodeSize);
		if(debugSize != h.nodeCompressedSize) {
			TileUtils.error("debugSize != h.nodeCompressedSize: %d != %d", debugSize, h.nodeCompressedSize);
		}
		
		debugSize = read(bytes, h.nodeCount, part);
		if(debugSize != h.nodeSize) {
			TileUtils.error("debugSize != h.nodeSize: %d != %d", debugSize, h.nodeSize);
		}
		
		return;
	}
	
	public int read(byte[] bytes, int nodeDefined, TilePart part) {
		int index = 0;
		Memory memory = new Memory(bytes);
		
		int uVar2 = memory.UnsignedByte(index++);
		List<String> tags = new ArrayList<>();
		if(uVar2 != 0) {
			for(int i = 0; i < uVar2; i++) {
				int size = memory.UnsignedByte(index++);
				tags.add(memory.String(size, index));
				index += size;
			}
		}
		
		if(nodeDefined != 0) {
			for(int i = 0; i < nodeDefined; i++) {
				float[] f_pos = memory.Floats(3, index);
				float[] f_quat = memory.Floats(4, index + 0xc);
				float[] f_size = memory.Floats(3, index + 0x1c);
				
				NodeImpl node = new NodeImpl(tags);
				
				{
					node.setPosition(
						f_pos[0], // + position[0]
						f_pos[1], // + position[1]
						f_pos[2]  // + position[2]
					);
					
					node.setRotation(f_quat[0], f_quat[1], f_quat[2], f_quat[3]);
					node.setSize(f_size[0], f_size[1], f_size[2]);
				}
				
				index += 0x28;
				int bVar2 = memory.UnsignedByte(index++);
				
				if(bVar2 != 0) {
					for(int j = 0; j < bVar2; j++) {
						int idx = memory.UnsignedByte(index++);
						String tag_name = tags.get(idx);
						node.setTagState(tag_name, true);
					}
				}
				
				int uVar3 = memory.Int(index);
				index += 4;
				if(uVar3 != 0) {
					//int local_248 = (uVar3 * 8);
					//byte[] data = memory.Bytes(uVar3, index);
					index += uVar3;
					
					// System.out.printf("Data: [%s]\n", new String(data));
					
					// LuaObjectSerializer.cpp
				}
				
//				{
//					System.out.printf("  pos : %s\n", node.pos);
//					System.out.printf("  rot : %s\n", node.rot);
//					System.out.printf("  size: %s\n", node.size);
//					System.out.printf("  tags:\n");
//					
//					for(String tag : node.getDefinedTags()) {
//						if(node.getTagState(tag)) {
//							System.out.printf("    : %s\n", tag);
//						}
//					}
//					
//					System.out.println();
//				}
				
				part.addNode(node);
			}
		}
		
		TileUtils.log("tags = %s", tags);
		
		return index;
	}
}
