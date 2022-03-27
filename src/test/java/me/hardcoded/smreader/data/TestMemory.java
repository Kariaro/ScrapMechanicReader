package me.hardcoded.smreader.data;

import org.junit.Before;
import org.junit.Test;

import org.junit.Assert;


/**
 * A test class used to test if the class {@code Memory} works.
 * 
 * @author HardCoded
 */
public class TestMemory {
	public Memory memory = new Memory(0x1000);
	
	public TestMemory() {
	}
	
	@Before
	public void reset() {
		byte[] bytes = new byte[256];
		for(int i = 0; i < 256; i++) {
			bytes[i] = (byte)i;
		}
		
		memory = new Memory(bytes);
	}
	
	@Test
	public void test_byte_type() {
		Assert.assertEquals(0, memory.NextByte()); // First byte should be zero, index++
		Assert.assertEquals(1, memory.Byte());     // Second byte should be one.
		Assert.assertEquals(1, memory.Byte());     // index should not change
		memory.set(0);
		
		memory.NextWriteByte(12);
		memory.WriteByte(23);
		Assert.assertEquals(1, memory.index());
		memory.set(0);
		Assert.assertEquals(12, memory.Byte(0));
		Assert.assertEquals(23, memory.Byte(1));
	}
	
	@Test
	public void test_short_type() {
		
	}
}
