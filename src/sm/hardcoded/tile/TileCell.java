package sm.hardcoded.tile;

/**
 * All tiles are made up of TileCells.
 * 
 * @author HardCoded
 */
public final class TileCell {
	// 33 * 33
	private final float[] vertexHeight;
	
	// 33 * 33
	private final int[] vertexColor;

	// 65 * 65
	private final long[] ground;
	
	// 128 x 128
	private final byte[] clutter;
	
	// TODO: Decals
	
	TileCell() {
		vertexColor = new int[33 * 33];
		vertexHeight = new float[33 * 33];
		ground = new long[65 * 65];
		clutter = new byte[128 * 128];
	}
	
	/**
	 * Get the vertex color at the specified grid position.<br>
	 * The grid size is 33x33.
	 * 
	 * @param x the grid x position
	 * @param y the grid y position
	 * @return The color of the vertex
	 */
	public int getVertexColor(int x, int y) {
		return vertexColor[x + y * 33];
	}
	
	/**
	 * Get the vertex height at the specified grid position.<br>
	 * The grid size is 33x33.
	 * 
	 * @param x the grid x position
	 * @param y the grid y position
	 * @return The height of the vertex
	 */
	public float getVertexHeight(int x, int y) {
		return vertexHeight[x + y * 33];
	}
	
	/**
	 * Get the opacity of the material at the specified grid position.<br>
	 * The grid size is 65x65.
	 * 
	 * @param x the grid x position
	 * @param y the grid y position
	 * @param id the material id
	 * @return The opacity of the material
	 */
	public float getGroundMaterialOpacity(int x, int y, int id) {
		return ((ground[x + y * 65] >> id * 8) & 0xff) / 255.0f;
	}
}
