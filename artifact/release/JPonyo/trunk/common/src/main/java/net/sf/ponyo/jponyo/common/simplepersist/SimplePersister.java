package net.sf.ponyo.jponyo.common.simplepersist;

public interface SimplePersister {

	public abstract void init(final Object instance, final String persistId);

	public abstract void persist(final Object instance, final String persistId);

}