package me.hardcoded.smreader.math;

import me.hardcoded.smreader.error.TileException;
import me.hardcoded.smreader.utils.NotNull;

/**
 * A simple abstract vector class.
 * 
 * @author HardCoded
 * @since v0.2
 */
public abstract class FloatVec<T extends FloatVec<T>> {
	protected float[] array;
	protected int dimensions;
	
	protected FloatVec(int dimensions) {
		this.dimensions = dimensions;
		this.array = new float[dimensions];
	}
	
	/**
	 * Returns this vector as an array.
	 * @return this vector as an array
	 */
	@NotNull
	public float[] toArray() {
		return array;
	}
	
	/**
	 * Set the value of this vector.
	 * @param elements the elements
	 * @return this vector
	 */
	@SuppressWarnings("unchecked")
	public T set(float... elements) {
		if(elements.length != dimensions) throw new TileException("Failed to set FloatVec: Expected '" + dimensions + "' elements but got '" + elements.length + "'");
		System.arraycopy(elements, 0, array, 0, dimensions);
		return (T)this;
	}
	
	/**
	 * Set the value of this vector.
	 * @param vec the vector to get values from
	 * @return this vector
	 */
	public T set(T vec) {
		return set(vec.array);
	}
	
	/**
	 * Returns the number of dimensions this vector has.
	 * @return the number of dimensions this vector has
	 */
	public int getNumDimensions() {
		return dimensions;
	}
	
	/**
	 * Returns the length of this vector.
	 * {@code sqrt(x^2 + y^2 + z^2 ... ) }
	 * @return the length of this vector
	 */
	public float getLength() {
		float total = 0;
		
		for(float f : array) {
			total += f * f;
		}
		
		return (float)Math.sqrt(total);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(float f : array) {
			sb.append(String.format(", %.4f", f));
		}
		sb.delete(0, 2);
		return String.format("{ %s }", sb.toString());
	}
}
