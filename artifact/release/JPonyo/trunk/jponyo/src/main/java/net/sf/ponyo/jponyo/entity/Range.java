package net.sf.ponyo.jponyo.entity;


/**
 * @since 0.1
 */
public class Range {

	// LUXURY easeIn/Out-function = { LINEAR, EXPONENTIAL, CUBIC, QUADRATIC }
	// provide interpolation function as well (linear, exponential,... custom "curve-points")
	
	private final FloatPair convertFrom;
	private final IntPair convertTo;
	
	public Range(FloatPair convertFrom, IntPair convertTo) {
		this.convertFrom = convertFrom;
		this.convertTo = convertTo;
	}

	public final FloatPair getConvertFrom() {
		return this.convertFrom;
	}

	public final IntPair getConvertTo() {
		return this.convertTo;
	}

	// TODO equals/hash
	
	@Override public String toString() {
		return "Range[convertFrom=" + this.convertFrom + ", convertTo=" + this.convertTo + "]";
	}
}
