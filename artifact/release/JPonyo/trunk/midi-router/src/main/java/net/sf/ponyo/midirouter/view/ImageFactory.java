package net.sf.ponyo.midirouter.view;

import java.awt.Toolkit;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ImageFactory {

	private static final Log LOG = LogFactory.getLog(ImageFactory.class);

    private final String basePath;

    private final Map<String, ImageIcon> iconsCache = new HashMap<String, ImageIcon>();
    
    public ImageFactory(String basePath) {
		this.basePath = basePath;
	}


	public ImageIcon getImage(final String filename) {
        LOG.debug("Getting image by filename '" + filename + "'.");

        final ImageIcon image;

        final ImageIcon cachedIcon = this.iconsCache.get(filename);
        if (cachedIcon != null) {
            image = cachedIcon;
        } else {
            final String imagePath = this.basePath + filename;
            LOG.debug("Loading and caching image for first time by path '" + imagePath + "'...");
            
            final URL imageUrl = ImageFactory.class.getResource(imagePath);
            if (imageUrl == null) {
                throw new RuntimeException("Could not load image (filename='" + filename + "') by image path '" + imagePath + "'!");
            }
            
            LOG.debug("Loaded image (" + filename + ") by URL '" + imageUrl.getFile() + "'.");
            image = new ImageIcon(Toolkit.getDefaultToolkit().getImage(imageUrl));
            this.iconsCache.put(filename, image);
        }

        return image;
    }
}
