package net.sf.ponyo.jponyo.adminconsole.gl;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;

import javax.media.opengl.GL;
import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.ponyo.jponyo.common.util.LibraryUtil;
import net.sf.ponyo.jponyo.entity.Joint;
import net.sf.ponyo.jponyo.entity.Skeleton;

public final class GLUtil {
	
	private static final Log LOG = LogFactory.getLog(GLUtil.class);
	
	private GLUtil() { /* utility class */ }

	public static void checkJoglLibs() {
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
	
	public static void setColor(GL gl, Color c) {
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
//		System.out.println("r / g / b = " + r + " / " + g + " / " + b);
		float rf = r / 255.0f;
		float gf = g / 255.0f;
		float bf = b / 255.0f;
//		System.out.println("FLOAT r / g / b = " + rf + " / " + gf + " / " + bf);
		gl.glColor3f(rf, gf, bf);
	}
	
	public static void translate(GL gl, Skeleton skeleton, Joint joint) {
		final float[] xyz = skeleton.getCoordinates(joint).data;
		final float x = xyz[0] / 100.0f;
		final float y = xyz[1] / 100.0f;
		final float z = xyz[2] / 100.0f - 30;
//		System.out.println("translated x/y/z: "+x+"/"+y+"/"+z);
		gl.glTranslatef(x, y, z);
	}
}
