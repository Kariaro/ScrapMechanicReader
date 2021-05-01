package com.hardcoded.tile.object;

import java.util.UUID;

import com.hardcoded.math.Quat;
import com.hardcoded.math.Vec3;

/**
 * A tile entity interface.
 * 
 * @author HardCoded
 */
public interface TileEntity {
	static final UUID DEFAULT_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
	
	/**
	 * Returns the position of this tile entity.
	 * @return the position of this tile entity
	 */
	Vec3 getPosition();
	
	/**
	 * Returns the rotation of this tile entity.
	 * @return the rotation of this tile entity
	 */
	Quat getRotation();
	
	/**
	 * Returns the size of this tile entity.
	 * @return the size of this tile entity
	 */
	Vec3 getSize();
	
	/**
	 * Change the position of this tile entity.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 */
	void setPosition(float x, float y, float z);
	
	/**
	 * Change the position of this tile entity.
	 * @param vec the new position
	 */
	void setPosition(Vec3 vec);
	
	/**
	 * Change the rotation of this tile entity.
	 * @param x the x axis
	 * @param y the y axis
	 * @param z the z axis
	 * @param w the w axis
	 */
	void setRotation(float x, float y, float z, float w);
	
	/**
	 * Change the rotation of this tile entity.
	 * @param quat the new rotation
	 */
	void setRotation(Quat quat);
	
	/**
	 * Change the size of this tile entity.
	 * @param x the x size
	 * @param y the y size
	 * @param z the z size
	 */
	void setSize(float x, float y, float z);
	
	/**
	 * Change the size of this tile entity.
	 * @param vec the new size
	 */
	void setSize(Vec3 vec);
}
