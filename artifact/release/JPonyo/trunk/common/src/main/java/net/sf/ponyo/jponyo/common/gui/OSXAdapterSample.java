package net.sf.ponyo.jponyo.common.gui;

/**
 * @since 0.1
 */
public class OSXAdapterSample {
	
	public static void main(String[] args) throws Exception {
		new OSXAdapterSample().doit();
	}

	private void doit() throws Exception {
		OSXAdapter.setPreferencesHandler(this, this.getClass().getDeclaredMethod("onOsxPreferences", (Class[]) null));
		OSXAdapter.setQuitHandler(this, this.getClass().getDeclaredMethod("onOsxQuit", (Class[]) null));
	}
	
	public void onOsxPreferences() {
		System.out.println("onOsxPreferences()");
	}

	public void onOsxQuit() {
		System.out.println("onOsxQuit()");
	}
}
