package me.hardcoded.smreader.tile.object;

import java.util.List;

import me.hardcoded.smreader.utils.NotNull;

/**
 * A prefab interface.
 * 
 * @author HardCoded <https://github.com/Kariaro>
 * @since v0.2
 */
public interface Prefab extends TileEntity {
	/**
	 * Returns the path of this prefab.
	 * @return the path of this prefab
	 */
	@NotNull
	String getPath();
	
	/**
	 * Returns the flag of this prefab.
	 * @return the falg of this prefab
	 */
	@NotNull
	String getFlag();
	
	/**
	 * Set the path of this prefab.
	 * @param path the new path
	 */
	void setPath(String path);
	
	/**
	 * Set the flag of this prefab.
	 * @param flag the new flag
	 */
	void setFlag(String flag);
	
	/**
	 * Returns a list of nodes inside this prefab.
	 * @return a list of nodes inside this prefab
	 */
	List<Node> getNodes();
	
	/**
	 * Returns a list of assets inside this prefab.
	 * @return a list of assets inside this prefab
	 */
	List<Asset> getAssets();
	
	/**
	 * Returns a list of harvestables inside this prefab.
	 * @return a list of harvestables inside this prefab
	 * 
	 * @deprecated Not implemented yet
	 */
	List<Harvestable> getHarvestables();
	
	/**
	 * Returns a list of prefabs inside this prefab.
	 * @return a list of prefabs inside this prefab
	 */
	List<Prefab> getPrefabs();
	
	/**
	 * Returns a list of blueprints inside this prefab.
	 * @return a list of blueprints inside this prefab
	 */
	List<Blueprint> getBlueprints();
	
	/**
	 * Returns if this prefab has been loaded by {@code PrefabFileReader} or not.
	 * <p>If this method returns {@code false} any calls to modify or get objects from this prefab will fail.
	 * The only valid call is then {@link #getPath()}
	 * @return if this prefab has been loaded by {@code PrefabFileReader} or not
	 */
	boolean isLoaded();
}
