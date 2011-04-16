package net.sf.ponyo.jponyo.user;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UserFactory {

	private static final Log LOG = LogFactory.getLog(UserFactory.class);
	
	private int userIdSequence = 0;

	public User create(int openniId) {
		LOG.debug("create(openniId=" + openniId + ")");
		return new User(this.userIdSequence++, openniId, 0xFF0000);
	}
}
