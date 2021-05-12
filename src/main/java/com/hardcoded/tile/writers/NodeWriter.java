package com.hardcoded.tile.writers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.hardcoded.data.Memory;
import com.hardcoded.tile.CellHeader;
import com.hardcoded.tile.impl.TilePart;
import com.hardcoded.tile.object.Node;
import com.hardcoded.utils.TileUtils;

/**
 * <b>This writer is incomplete!</b>
 * 
 * @author HardCoded
 */
public class NodeWriter implements TileWriterImpl {
	
	@Override
	public void write(CellHeader header, Memory memory, TilePart part) {
		List<Node> nodes = part.nodes;
		if(nodes.isEmpty()) {
			header.nodeCount = 0;
			return;
		}
		
		header.nodeCount = nodes.size();
		
		byte[] data = write(nodes, part);
		byte[] compressed = TileUtils.compress_data(data);
		header.nodeCompressedSize = compressed.length;
		header.nodeSize = data.length;
		header.nodeIndex = memory.index();
		memory.NextWriteBytes(compressed);
	}
	
	public byte[] write(List<Node> nodes, TilePart part) {
		Memory memory = new Memory(0x10000);
		
		List<String> tags = new ArrayList<>();
		if(!nodes.isEmpty()) {
			tags.addAll(nodes.get(0).getDefinedTags());
		}
		
		memory.NextWriteByte(tags.size());
		for(String tag : tags) {
			memory.NextWriteByte(tag.length());
			memory.NextWriteString(tag);
		}
		
		for(Node node : nodes) {
			memory.NextWriteFloats(node.getPosition().toArray());
			memory.NextWriteFloats(node.getRotation().toArray());
			memory.NextWriteFloats(node.getSize().toArray());
			
			Set<String> active_tags = node.getActiveTags();
			memory.NextWriteByte(active_tags.size());
			
			for(String tag : active_tags) {
				int index = tags.indexOf(tag);
				memory.NextWriteByte(index);
			}
			
			int lua_data_size = 0;
			memory.NextWriteInt(lua_data_size);
			
			if(lua_data_size != 0) {
				// TODO: Write serialized lua data
			}
		}
		
		byte[] bytes = new byte[memory.getHighestWrittenIndex()];
		System.arraycopy(memory.data(), 0, bytes, 0, bytes.length);
		return bytes;
	}
}
