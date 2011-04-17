package net.sf.ponyo.jponyo.common.geom;

import net.sf.ponyo.jponyo.common.math.FloatPair;
import net.sf.ponyo.jponyo.common.math.IntPair;

/**
 * @since 0.1
 */
public class RangeScaler {
	
	public static void main(String[] args) {
		System.out.println("80 = " + new RangeScaler().scale(0.2F, new Range(new FloatPair(0.6F, 0.9F), new IntPair(80, 50))));
	}

	/** {@inheritDoc} from {@link RangeScaler} */
	public final int scale(final float coordinate, final Range range) {
		final float f1 = range.getConvertFrom().getLeft();
		final float f2 = range.getConvertFrom().getRight();
		final int t1 = range.getConvertTo().getLeft();
		final int t2 = range.getConvertTo().getRight();
		
        final int diffExpected = Math.abs(t1 - t2);
        final double diffRealPercentExpectedAdjusted =
        	RangeScaler.calcDiffFromPercentExpectedAdjusted(f1, f2, coordinate, t1 < t2);
        
        final int addOn;
        if(f1 > f2 && t1 > t2) {
        	addOn = 0;
		} else if(f1 > f2) {
        	addOn = t1;
        } else if(t1 > t2) {
        	addOn = t2;
        } else {
        	addOn = 0;
        }
        return (int) Math.round(addOn + diffExpected * diffRealPercentExpectedAdjusted);
	}

	private static double calcDiffFromPercentExpectedAdjusted(final float f1, final float f2,
                final float coordinate, final boolean expectedStartLower) {
        final double diffFromPercent = RangeScaler.calcDiffFromPercent(f1, f2, coordinate);
        final double diffFromPercentExpectedAdjusted;
        
        if(expectedStartLower) {
                diffFromPercentExpectedAdjusted = diffFromPercent;
        } else {
                diffFromPercentExpectedAdjusted = 1 - diffFromPercent;
        }
        
        return diffFromPercentExpectedAdjusted;
	}

	private static double calcDiffFromPercent(final float f1, final float f2, final float coordinate) {
        final float diffFrom = Math.abs(f1 - f2);
        final float fromAdjustedValue = RangeScaler.calcFromAdjustedValue(f1, f2, coordinate);
        
        final float fromMin = Math.min(f1, f2);
        
        final double diffFromPercent;
        if(f1 < f2) {
                diffFromPercent = (fromAdjustedValue - fromMin) / ((double) diffFrom);
        } else {
                diffFromPercent = 1.0d - (fromAdjustedValue - fromMin) / ((double) diffFrom);
        }
        
        return diffFromPercent;
	}

	private static float calcFromAdjustedValue(final float f1, final float f2, final float coordinate) {
        final float fromAdjustedValue;
        final float fromMin = Math.min(f1, f2);
        final float fromMax = Math.max(f1, f2);
        
        if(coordinate < fromMin) {
                fromAdjustedValue = fromMin;
        } else if(coordinate > fromMax) {
                fromAdjustedValue = fromMax;
        } else {
                fromAdjustedValue = coordinate;
        }
        return fromAdjustedValue;
	}

}
