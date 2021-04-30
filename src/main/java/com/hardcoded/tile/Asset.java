package com.hardcoded.tile;

import java.util.List;

/**
 * A asset interface.
 * 
 * @author HardCoded <https://github.com/Kariaro>
 */
public interface Asset {
	
	float[] getPosition();
	float[] getQuat();
	float[] getSize();
	
	List<String> getMaterials();
}
