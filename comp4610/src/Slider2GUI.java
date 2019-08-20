
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Slider2GUI extends JComponent implements Runnable, ChangeListener {

	/**
	 * Slider2GUI - a simple swing GUI with a slider. Eric McCreath 2019
	 */

	static final int gap = 30;

	JFrame jframe;
	JSlider jslider;

	public Slider2GUI() {
		SwingUtilities.invokeLater(this);
	}

	public static void main(String[] args) {
		new Slider2GUI();
	}

	public Dimension getPreferredSize() {
		return new Dimension(200, 200);
	}

	public void run() {
		jframe = new JFrame("Slider2GUI");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// set up the slider
		jslider = new JSlider();
		jslider.setMaximum(200);
		jslider.setMinimum(10);
		jslider.addChangeListener(this);

		// make the main panel
		JPanel mainpanel = new JPanel();
		mainpanel.setLayout(new BorderLayout());
		mainpanel.add(this, BorderLayout.CENTER);
		mainpanel.add(jslider, BorderLayout.PAGE_END);

		// add panel to jframe and make viewable
		jframe.getContentPane().add(mainpanel);
		jframe.setVisible(true);
		jframe.pack();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		repaint();
	}

	@Override
	protected void paintComponent(Graphics graph) {
		super.paintComponent(graph);
		Graphics2D g = (Graphics2D) graph;
		Dimension dim = this.getSize();
		// fill the background
		g.setColor(Color.white);
		g.fillRect(0, 0, dim.width, dim.height);


	}
}
