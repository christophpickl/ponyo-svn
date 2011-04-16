package net.sf.ponyo.jponyo.common.exception;

/**
 * Most commonly used to check for constructor arguments, but also for formal parameters of a regular method.
 * 
 * @since 0.1
 */
public class InvalidArgumentException extends PonyoException {

	private static final long serialVersionUID = 1733670933264953331L;
	
	
	private final String argumentName;
	
	private final Object argumentValue;

	
	protected InvalidArgumentException(
			final String argumentName,
			final Object argumentValue,
			final String condition) {
		super("Passed an illegal argument [" + argumentName + "] with value: [" + argumentValue + "]! " +
				"(condition was: " + condition + ")", null);
		
		this.argumentName = argumentName;
		this.argumentValue = argumentValue;
	}
	
	/**
	 * @since 0.1
	 */
	public static InvalidArgumentException newNotNull(final String argumentName) {
		return new InvalidArgumentException(argumentName, null, argumentName + " != null");
	}

	/**
	 * @since 0.1
	 */
	public static InvalidArgumentException newInstance(
			final String argumentName,
			final Object argumentValue,
			final String condition) {
		return new InvalidArgumentException(argumentName, argumentValue, condition);
	}
	
	/**
	 * @since 0.1
	 */
	public final String getArgumentName() {
		return this.argumentName;
	}
	
	/**
	 * @since 0.1
	 */
	public final Object getArgumentValue() {
		return this.argumentValue;
	}
	
}
