package net.pulseproject.mkinector.debugapp.view;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.pulseproject.commons.util.GuiUtil;
import net.pulseproject.mkinector.debugapp.misc.OscConnectionWindowGlueListener;

public class MainPanel extends JPanel implements OscConnectionWindowGlueListener {

	private static final long serialVersionUID = 5833077387459470873L;

	private final UserPanelsWrapper userPanelsWrapper = new UserPanelsWrapper();

	private final JLabel lblUserCount = new JLabel();
	

	public MainPanel() {
		this.setOpaque(false);
		this.onUserCountChanged(0, 0);
		this.initComponents();
	}
	public interface MainPanelListener {
		void onOpenScenariosClicked();
	}
	
	private void initComponents() {
		final JPanel infoPanel = new JPanel();
		infoPanel.setOpaque(false);
		infoPanel.add(GuiUtil.newJLabel("Tracked Users: ", StyleConstantsPool.FONT_MAIN_INFO_TEXT));
		this.lblUserCount.setFont(StyleConstantsPool.FONT_MAIN_INFO_TEXT);
		infoPanel.add(this.lblUserCount);
		
		final JScrollPane scrollableUserPanelsWrapper = new JScrollPane(this.userPanelsWrapper);
		
		this.setLayout(new BorderLayout());
//		this.add(this.createControlPanel(), BorderLayout.NORTH);
		this.add(scrollableUserPanelsWrapper, BorderLayout.CENTER);
		this.add(infoPanel, BorderLayout.SOUTH);
	}
	
//	private JPanel createControlPanel() {
//		final JPanel panel = new JPanel();
//		
//		final JButton btn = new JButton("Open Scenarios ...");
//		btn.addActionListener(new ActionListener() {
//			@Override public void actionPerformed(ActionEvent e) {
//				listener.onOpenScenariosClicked();
//		}});
//		panel.add(btn);
//		
//		return panel;
//	}

	@Override public final void onAddUserPanel(final UserPanel userPanel) {
		this.userPanelsWrapper.onAddUserPanel(userPanel);
	}

	@Override public final void onRemoveUserPanel(final UserPanel userPanel) {
		this.userPanelsWrapper.onRemoveUserPanel(userPanel);
	}

	@Override public final void onUserCountChanged(final int userReadyCount, final int userWaitingCount) {
		this.userPanelsWrapper.onUserCountChanged(userReadyCount, userWaitingCount);
		
		this.lblUserCount.setText(userReadyCount + " (" + userWaitingCount + " waiting)");
	}
	
}
