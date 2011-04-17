package net.sf.ponyo.jponyo.entity;

import net.sf.ponyo.jponyo.common.math.Array3f;

/**
 * @since 0.1
 */
public enum Direction {

	/**
	 * @since 0.1
	 */
	X { @Override public float extractValue(final Array3f coordinate) { return coordinate.data[0]; }},

	/**
	 * @since 0.1
	 */
	Y { @Override public float extractValue(final Array3f coordinate) { return coordinate.data[1]; }},

	/**
	 * @since 0.1
	 */
	Z { @Override public float extractValue(final Array3f coordinate) { return coordinate.data[2]; }};

	/**
	 * @since 0.1
	 */
	public abstract float extractValue(Array3f coordinate);
	
}
