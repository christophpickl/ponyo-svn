package net.sf.ponyo.jponyo.common.math;

/**
 * @since 0.1
 */
public class IntPair {
	
	private final int left;
	private final int right;
	
	public IntPair(int left, int right) {
		this.left = left;
		this.right = right;
	}
	
	public final int getLeft() {
		return this.left;
	}
	
	public final int getRight() {
		return this.right;
	}
}
