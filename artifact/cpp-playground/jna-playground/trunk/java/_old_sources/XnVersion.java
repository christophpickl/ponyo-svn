package pnj;

import com.sun.jna.Structure;

public class XnVersion extends Structure {
	
	public static class ByReference extends XnVersion implements Structure.ByReference { }
    public int nMajor, nMinor, nMaintenance, nBuild;
    
}
