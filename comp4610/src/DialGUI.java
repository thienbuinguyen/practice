import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class DialGUI implements Runnable, ChangeObserver {

	private static final Dimension COLORDIM = new Dimension(
			DialRGB.dim.height * 3, DialRGB.dim.height * 3);
	/**
	 * DialGUI - this application enables the users to change the color of a
	 * rectangle by altering 3 dials. The dial alter the RGB components of the
	 * color. Eric McCreath 2015,2017 - GPL
	 */

	JFrame jframe;
	JComponent colorComp;
	DialRGB dialr;
	DialRGB dialg;
	DialRGB dialb;

	// YUV dials
	DialRGB dialy;
	DialRGB dialu;
	DialRGB dialv;

	//CMY dials
	DialRGB dialc;
	DialRGB dialm;
	DialRGB dialy2;

	// HSB dials
	DialRGB dialh;
	DialRGB dials;
	DialRGB dialb2;

	public DialGUI() {
		SwingUtilities.invokeLater(this);
	}

	public static void main(String[] args) {
		new DialGUI();
	}

	// start up the JFrame add the dials and components 
	public void run() {
		jframe = new JFrame("DialGUI");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// RGB dials
		dialr = new DialRGB(Color.RED);
		dialg = new DialRGB( Color.GREEN);
		dialb = new DialRGB( Color.BLUE);
 		dialr.addObserver(this);
		dialg.addObserver(this);
		dialb.addObserver(this);

		// YUV dials
		dialy = new DialRGB(Color.WHITE);
		dialu = new DialRGB(Color.BLUE);
		dialv = new DialRGB(Color.RED);
		dialy.addObserver(this);
		dialu.addObserver(this);
		dialv.addObserver(this);

		// CMY dials
		dialc = new DialRGB(Color.CYAN);
		dialm = new DialRGB(Color.MAGENTA);
		dialy2 = new DialRGB(Color.yellow);
		dialc.addObserver(this);
		dialm.addObserver(this);
		dialy2.addObserver(this);

		// HSB dials
		dialh = new DialRGB(Color.PINK);
		dials = new DialRGB(Color.yellow);
		dialb2 = new DialRGB(Color.white);
		dialh.addObserver(this);
		dials.addObserver(this);
		dialb2.addObserver(this);
		
		colorComp = new JPanel() {
			protected void paintComponent(Graphics g) {
				g.setColor(this.getForeground());
				g.fillRect(0, 0, COLORDIM.width, COLORDIM.height);
			}
		};
		colorComp.setPreferredSize(COLORDIM);
		JPanel dialpanel = new JPanel();
		dialpanel.setLayout(new GridLayout(3, 1));
		// RGB
//		dialpanel.add(dialr);
//		dialpanel.add(dialg);
//		dialpanel.add(dialb);

		// YUV
//		dialpanel.add(dialy);
//		dialpanel.add(dialu);
//		dialpanel.add(dialv);

		// CMY
//		dialpanel.add(dialc);
//		dialpanel.add(dialm);
//		dialpanel.add(dialy2);

		// HSB
		dialpanel.add(dialh);
		dialpanel.add(dials);
		dialpanel.add(dialb2);

		jframe.getContentPane().add(dialpanel, BorderLayout.EAST);
		jframe.getContentPane().add(colorComp, BorderLayout.CENTER);

		update();
		jframe.setVisible(true);
		jframe.pack();
	}

	private float clamp(float n, float min, float max) {
		return Math.max(min, Math.min(max, n));
	}

	// update - get the values from the dials and set the color of 
	//          the panel based on these values.   
	public void update() {
		// convert from YUV to RGB
//		float y = (float) dialy.value();
//		float u = (float) (dialu.value() * (0.436f + 0.436f) - 0.436f); // normalise to appropriate range
//		float v = (float) (dialv.value() * (0.615f + 0.615f) - 0.615f); // normalise to appropriate range
//
//		float r = clamp(y + 1.13983f * v, 0, 1);
//		float g = clamp(y + -0.38465f * u + -0.58060f * v, 0, 1);
//		float b = clamp(y + 2.03211f * u, 0, 1);

		// convert CMY to RGB
//		float r = 1 - (float) dialc.value();
//		float g = 1 - (float) dialm.value();
//		float b = 1 - (float) dialy2.value();

		// convert HSB to RGB
		Color hsb = Color.getHSBColor((float) dialh.value(), (float) dials.value(), (float) dialb2.value());
		float r = hsb.getRed() / 255.f;
		float g = hsb.getGreen() / 255.f;
		float b = hsb.getBlue() / 255.f;

		colorComp.setForeground(new Color(r, g, b));
		colorComp.repaint();
	}
}
