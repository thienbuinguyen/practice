import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SliderGUI  implements Runnable, ChangeListener {

	/**
	 * SliderGUI - a simple swing GUI with a slider. Eric McCreath 2019
	 */

	JFrame jframe;
	JSlider jslider;
	SliderCanvas slidercanvas;
	BoxesCanvas boxesCanvas;
	SliderRandomLinesCanvas sliderRandomLinesCanvas;
	HelloWorldCanvas helloWorldCanvas;
	SliderMonochromaticCanvas sliderMonochromaticCanvas;
	CirclePyramidCanvas circlePyramidCanvas;

	public SliderGUI() {
		SwingUtilities.invokeLater(this);
	}

	public static void main(String[] args) {
		new SliderGUI();
	}
	 
	public void run() {
		jframe = new JFrame("SliderGUI");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
		
		// set up the slider
		jslider = new JSlider();
		jslider.setMaximum(700);
		jslider.setMinimum(380);
		slidercanvas = new SliderCanvas(jslider.getValue());
		boxesCanvas = new BoxesCanvas();
		sliderRandomLinesCanvas = new SliderRandomLinesCanvas((jslider.getValue()));
		helloWorldCanvas = new HelloWorldCanvas(jslider.getValue());
		sliderMonochromaticCanvas = new SliderMonochromaticCanvas(jslider.getValue());
		circlePyramidCanvas = new CirclePyramidCanvas();
		jslider.addChangeListener(this);
		
		// make the main panel
		JPanel mainpanel = new JPanel();
		mainpanel.setLayout(new BorderLayout());
//		mainpanel.add(slidercanvas, BorderLayout.CENTER);
//		mainpanel.add(jslider, BorderLayout.PAGE_END);
//		mainpanel.add(boxesCanvas);
//		mainpanel.add(sliderRandomLinesCanvas);
//		mainpanel.add(helloWorldCanvas);
//		mainpanel.add(sliderMonochromaticCanvas);
		mainpanel.add(circlePyramidCanvas);

		// add panel to jframe and make viewable
		jframe.getContentPane().add(mainpanel);
		jframe.setVisible(true);
		jframe.pack();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		sliderMonochromaticCanvas.setValue(jslider.getValue());
	}
}
