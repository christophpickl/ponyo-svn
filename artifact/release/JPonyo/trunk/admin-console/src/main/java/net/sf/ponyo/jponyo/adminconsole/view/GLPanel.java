package net.sf.ponyo.jponyo.adminconsole.view;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.Collection;

import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.ponyo.jponyo.common.util.LibraryUtil;
import net.sf.ponyo.jponyo.user.ContinuousUserListener;
import net.sf.ponyo.jponyo.user.User;

import com.sun.opengl.util.Animator;

public class GLPanel extends JPanel implements ContinuousUserListener {

	private static final long serialVersionUID = 2357164011556061774L;
	private static final Log LOG = LogFactory.getLog(GLPanel.class);
	
	private final Animator animator;
	private final GLRenderer skeletonGLRenderer = new GLRenderer();
	
	static {
		GLPanel.checkJoglLibs();
	}
	
	public GLPanel() {
		super(new BorderLayout());

		GLCapabilities glCapabilities = new GLCapabilities();
		glCapabilities.setDoubleBuffered(true);
		glCapabilities.setHardwareAccelerated(true);
		
		GLCanvas canvas = new GLCanvas(glCapabilities);
		
		canvas.addGLEventListener(this.skeletonGLRenderer);
		this.animator = new Animator(canvas);
		
		this.add(canvas, BorderLayout.CENTER);
	}

	public void start() {
		this.animator.start();
	}
	
	public void stop() {
		this.animator.stop();
	}

	public void onCurrentUserChanged(User user) {
		this.skeletonGLRenderer.setUser(user);
	}
	
	private static void checkJoglLibs() {
		LOG.info("Checking for mandatory JOGL libraries ...");
		Collection<String> notFoundLibs = LibraryUtil.checkLibrariesExisting(LibraryUtil.JOGL_LIBS_MANDATORY);
		
		if(notFoundLibs.isEmpty() == false) {
			LOG.error("Some mandatory JOGL libraries could not be found: " + Arrays.toString(notFoundLibs.toArray()));
			
			StringBuilder errorMsg = new StringBuilder();
			errorMsg.append("3D Visualisation won't work, as you are missing some libraries:");
			
			for (String lib : notFoundLibs) {
				errorMsg.append("\n");
				errorMsg.append(lib);
			}
			
			JOptionPane.showMessageDialog(null, errorMsg.toString(),
					"3D Graphics libraries not found!", JOptionPane.ERROR_MESSAGE);
		}
		

		LOG.debug("Checking for optional JOGL libraries ...");
		Collection<String> notFoundOptionalLibs = LibraryUtil.checkLibrariesExisting(LibraryUtil.JOGL_LIBS_OPTIONAL);
		if(notFoundOptionalLibs.isEmpty() == false) {
			LOG.warn("Some optional JOGL libraries could not be found: " + Arrays.toString(notFoundOptionalLibs.toArray()));
		}
	}
	
}
