package net.sf.ponyo.jponyo.stream;

import net.sf.ponyo.jponyo.common.async.AsyncFor;
import net.sf.ponyo.jponyo.user.User;

/**
 * @since 0.1
 */
public interface MotionStream extends AsyncFor<User, MotionStreamListener> {

	// async communication only
	
}
