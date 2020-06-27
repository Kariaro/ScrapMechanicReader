package sm.hardcoded.tile;

import java.lang.reflect.Array;

public final class Tile {
	private final int width;
	private final int height;
	
	Tile(int width, int height) {
		this.width = width;
		this.height = height;
		
		vertexColor = new int[(width * 32 + 1) * (height * 32 + 1)];
		vertexHeight = new float[(width * 32 + 1) * (height * 32 + 1)];
		ground = new long[(width * 64 + 1) * (height * 64 + 1)];
		clutter = new byte[(width * 128) * (height * 128)];
	}
	
	// 33 * 33
	final float[] vertexHeight;
	
	// 33 * 33
	final int[] vertexColor;

	// 65 * 65
	final long[] ground;
	
	// 128 x 128
	final byte[] clutter;
	
	
	void writeVertexColor(int x, int y, int[] array) {
		writeArray(array, 0x21, vertexColor, 0x20 * x, 0x20 * y, 0x20 * width + 1);
	}
	
	void writeVertexHeight(int x, int y, float[] array) {
		writeArray(array, 0x21, vertexHeight, 0x20 * x, 0x20 * y, 0x20 * width + 1);
	}
	
	void writeGroundMaterials(int x, int y, long[] array) {
		writeArray(array, 0x41, ground, 0x40 * x, 0x40 * y, 0x40 * width + 1);
	}
	
	void writeClutter(int x, int y, byte[] array) {
		writeArray(array, 0x80, clutter, 0x80 * x, 0x80 * y, 0x80 * width);
	}
	
	void writeArray(Object src, int srcSize, Object dst, int dstX, int dstY, int dstSize) {
		for(int y = 0; y < srcSize; y++) {
			for(int x = 0; x < srcSize; x++) {
				Object value = Array.get(src, x + y * srcSize);
				
				int index = (x + dstX) + (y + dstY) * dstSize;
				Array.set(dst, index, value);
			}
		}
	}
	
	/**
	 * @return The width of this tile
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * @return The height of this tile
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Get the vertex color at the specified grid position.<br>
	 * The grid size will be <code>32 * {@link#getWidth} + 1</code>
	 * 
	 * @param x the grid x position
	 * @param y the grid y position
	 * @return The color of the vertex
	 */
	public int getVertexColor(int x, int y) {
		return vertexColor[x + y * (width * 32 + 1)];
	}
	
	/**
	 * Get the vertex height at the specified grid position.<br>
	 * The grid size will be <code>32 * {@link#getWidth} + 1</code>
	 * 
	 * @param x the grid x position
	 * @param y the grid y position
	 * @return The height of the vertex
	 */
	public float getVertexHeight(int x, int y) {
		return vertexHeight[x + y * (width * 32 + 1)];
	}
	
	/**
	 * Get the opacity of the material at the specified grid position.<br>
	 * The grid size will be <code>64 * {@link#getWidth} + 1</code>
	 * 
	 * @param x the grid x position
	 * @param y the grid y position
	 * @param id the material id
	 * @return The opacity of the material
	 */
	public float getGroundMaterialOpacity(int x, int y, int id) {
		return ((ground[x + y * (width * 64 + 1)] >> id * 8) & 0xff) / 255.0f;
	}
	
	public long getGroundMaterial(int x, int y) {
		return ground[x + y * (width * 64 + 1)];
	}
	
	public float[] getVertexHeightData() {
		return vertexHeight;
	}
	
	public int[] getVertexColorData() {
		return vertexColor;
	}
}
