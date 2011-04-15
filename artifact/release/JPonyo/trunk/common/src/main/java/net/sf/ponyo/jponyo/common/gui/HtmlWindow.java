package net.sf.ponyo.jponyo.common.gui;

import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

public class HtmlWindow extends JDialog {

	private static final long serialVersionUID = -3426364011636153562L;

	public HtmlWindow(String title, String url) {
		this.setTitle(title);
		
		// see http://nadeausoftware.com/articles/2008/11/mac_java_tip_how_create_aqua_small_sized_components
		this.getRootPane().putClientProperty("Window.style", "small");
		
//		final JTextArea text = new JTextArea(20, 40);
//		String initialText = "<html>\n" +
//        "Color and font test:\n" +
//        "<ul>\n" +
//        "<li><font color=red>red</font>\n" +
//        "<li><font color=blue>blue</font>\n" +
//        "<li><font color=green>green</font>\n" +
//        "<li><font size=-2>small</font>\n" +
//        "<li><font size=+2>large</font>\n" +
//        "<li><i>italic</i>\n" +
//        "<li><b>bold</b>\n" +
//        "</ul>\n";
//		text.setText(initialText);
//	    text.setEditable(false);
			
//		String url = "http://josceleton.sourceforge.net/static/midi-prototype-help.html?app_version=" + appVersion;
		try {
			JEditorPane text = new JEditorPane(url);
			text.setEditable(false);
			JScrollPane scrollPane = new JScrollPane(text);
			this.getContentPane().add(scrollPane);
		} catch (IOException e) {
			e.printStackTrace(); // TODO proper error hadnling
			this.getContentPane().add(new JLabel("Error: " + e.getMessage()));
		}

		this.setSize(400, 260);
	}
}