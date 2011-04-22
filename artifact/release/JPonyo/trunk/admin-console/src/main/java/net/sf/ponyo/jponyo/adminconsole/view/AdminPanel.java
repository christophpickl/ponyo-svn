package net.sf.ponyo.jponyo.adminconsole.view;

import java.awt.BorderLayout;
import java.util.Collection;

import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.sf.ponyo.jponyo.common.util.LibraryUtil;
import net.sf.ponyo.jponyo.user.ContinuousUserListener;
import net.sf.ponyo.jponyo.user.User;
import net.sourceforge.jpotpourri.tools.PtUserSniffer;

import com.sun.opengl.util.Animator;

public class AdminPanel extends JPanel implements ContinuousUserListener {

	private static final long serialVersionUID = 2357164011556061774L;

	private final Animator animator;
	private final SkeletonGLRenderer skeletonGLRenderer = new SkeletonGLRenderer();
	static {
		System.out.println(AdminPanel.class.getName() + " checking for JOGL libraries ...");
		Collection<String> notFoundLibs = LibraryUtil.checkLibrariesExisting(LibraryUtil.JOGL_LIBS_MANDATORY);
		
		if(notFoundLibs.isEmpty() == false) {
			StringBuilder errorMsg = new StringBuilder();
			errorMsg.append("3D Visualisation won't work, as you are missing some libraries:");
			
			for (String lib : notFoundLibs) {
				errorMsg.append("\n");
				errorMsg.append(lib);
			}
			JOptionPane.showMessageDialog(null, errorMsg.toString(),
					"3D Graphics libraries not found!", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public AdminPanel() {
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
	
}
