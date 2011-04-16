package net.sf.ponyo.jponyo.exception;

/**
 * @since 0.1
 */
public abstract class PonyoException extends RuntimeException {

	private static final long serialVersionUID = 322428616175111188L;

    protected PonyoException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
