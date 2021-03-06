package net.sf.ponyo.jponyo.common.io;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @since 0.1
 */
public class IoUtil {
	
	private static final Log LOG = LogFactory.getLog(IoUtil.class);

	/**
	 * @since 0.1
	 */
	public static Properties loadPropertiesFromClassPath(final ClassLoader loader, final String fileName) {
		final Properties properties = new Properties();
		InputStream inputStream = null;
		try {
			inputStream = loader.getResourceAsStream(fileName);
			if(inputStream == null) {
				throw new RuntimeException("Could not get resource [" + fileName + "] as stream!");
			}
			properties.load(inputStream);
			return properties;
		} catch(IOException e) {
			throw new RuntimeException("Could not load properties file [" + fileName +"]!", e);
		} finally {
			IoUtil.close(inputStream);
		}
	}

	/**
	 * @since 0.1
	 */
	public static boolean close(final Closeable closeable) {
        if (closeable == null) {
            return false;
        }
        
        try {
	        closeable.close();
	        return true;
        } catch (final IOException e) {
            LOG.warn("Could not close closeable [" + closeable.getClass().getSimpleName() + "]!", e);
            return false;
        }
	}

	/**
	 * @since 0.1
	 */
	public static boolean close(final net.sf.ponyo.jponyo.common.async.Closeable closeable) {
        if (closeable == null) {
            return false;
        }
        
        try {
	        closeable.close();
	        return true;
        } catch (final Exception e) {
            LOG.warn("Could not close closeable [" + closeable.getClass().getSimpleName() + "]!", e);
            return false;
        }
	}
	
}
