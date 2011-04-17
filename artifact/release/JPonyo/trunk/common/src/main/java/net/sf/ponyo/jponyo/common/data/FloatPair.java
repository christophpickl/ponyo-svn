package net.sf.ponyo.jponyo.common.data;

/**
 * @since 0.1
 */
public class FloatPair {
	
	private final float left;
	private final float right;
	
	public FloatPair(float left, float right) {
		this.left = left;
		this.right = right;
	}
	
	public final float getLeft() {
		return this.left;
	}
	
	public final float getRight() {
		return this.right;
	}
}
