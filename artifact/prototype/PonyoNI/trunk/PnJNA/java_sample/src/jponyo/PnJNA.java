package jponyo;

import com.sun.jna.Library;

public interface PnJNA extends Library {
	void startup();
	void shutdown();
}
