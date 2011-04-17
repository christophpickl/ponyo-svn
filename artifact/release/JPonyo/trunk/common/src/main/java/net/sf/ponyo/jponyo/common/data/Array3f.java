package net.sf.ponyo.jponyo.common.data;

public class Array3f {
	
	public final float[] data;
	
	public Array3f(float x, float y, float z) {
		this.data = new float[] { x, y, z};
	}
	@Override
	public String toString() {
		return "Array3f[x=" + this.data[0] + ", y=" + this.data[1] + ", z=" + this.data[2] + "]";
	}
}
