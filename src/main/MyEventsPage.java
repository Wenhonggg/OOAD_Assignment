package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class MyEventsPage extends JPanel {
	private JPanel selectedPanel;

	public MyEventsPage(JFrame frame, Participant p) {
		super();
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.setMaximumSize(new Dimension(500, Integer.MAX_VALUE));
		// leftPanel.setMinimumSize(new Dimension(500,
		// leftPanel.getPreferredSize().height));
		// leftPanel.setPreferredSize(new Dimension(500,
		// leftPanel.getPreferredSize().height));
		JPanel ticketPanel = new JPanel();
		ticketPanel.setBackground(new Color(227, 225, 227));
		ticketPanel.setLayout(new GridBagLayout());
		JComponent leftScrollPane;
		Ticket[] tickets = p.getTickets();
		if (tickets == null) {
			leftPanel.setLayout(new GridBagLayout());
			JLabel label = new JLabel(
					"<html><div style='text-align:center;'>You currently do not have any tickets.</div></html>");
			label.setPreferredSize(new Dimension(500, label.getPreferredSize().height));
			label.setBorder(new EmptyBorder(0, 120, 0, 120));
			leftPanel.add(label);
			leftScrollPane = leftPanel;
		} else {
			JPanel[] ticketListPanels = new JPanel[tickets.length];
			for (int i = 0; i < tickets.length; i++) {
				ticketListPanels[i] = tickets[i].ticketListItem(this, ticketPanel);
				leftPanel.add(ticketListPanels[i]);
				JPanel sep = new JPanel();
				sep.setPreferredSize(new Dimension(0, 1));
				sep.setMaximumSize(new Dimension(500, 1));
				sep.setBackground(Color.LIGHT_GRAY);
				leftPanel.add(sep);
			}
			leftScrollPane = new JScrollPane(leftPanel);
			((JScrollPane) leftScrollPane).setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			((JScrollPane) leftScrollPane).setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			leftScrollPane.setPreferredSize(new Dimension(500, frame.getHeight()));
			((JScrollPane) leftScrollPane).getVerticalScrollBar().setUnitIncrement(16);
			((JScrollPane) leftScrollPane).getVerticalScrollBar().setUI(new BasicScrollBarUI() {
				@Override
				protected void configureScrollBarColors() {
					thumbColor = Color.LIGHT_GRAY;
					trackColor = new Color(230, 227, 227);
				}

				@Override
				protected JButton createDecreaseButton(int orientation) {
					return createBtn();
				}

				@Override
				protected JButton createIncreaseButton(int orientation) {
					return createBtn();
				}

				@Override
				protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
					Graphics2D g2 = (Graphics2D) g.create();
					g2.setColor(thumbColor);
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 8, 8);
					g2.dispose();
				}

				@Override
				protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
					Graphics2D g2 = (Graphics2D) g.create();
					g2.setColor(trackColor);
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2.fillRoundRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height, 8, 8);
					g2.dispose();
				}

				private JButton createBtn() {
					JButton btn = new JButton();
					btn.setPreferredSize(new Dimension(0, 0));
					return btn;
				}
			});
			((JScrollPane) leftScrollPane).getVerticalScrollBar().setPreferredSize(new Dimension(10, 10));
		}
		JLabel placeholder = new JLabel("Select an event to view your ticket.");
		placeholder.setFont(new Font("Calibri", Font.PLAIN, 20));
		ticketPanel.add(placeholder);
		add(leftScrollPane);
		add(ticketPanel);
	}

	public void setSelectedPanel(JPanel panel) {
		if (selectedPanel != null && selectedPanel != panel)
			selectedPanel.setBackground(Color.WHITE);
		selectedPanel = panel;
		selectedPanel.setBackground(new Color(204, 204, 204));
	}

	public JPanel getSelectedPanel() {
		return selectedPanel;
	}
}
