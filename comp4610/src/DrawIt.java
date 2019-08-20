import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;


/*
 * DrawIt - Simple Drawing Program
 * Eric McCreath 2009, 2017, 2019
 */

public class DrawIt  implements Runnable {

	static final Dimension dim = new Dimension(800,600);
	
	JFrame jf;
	DrawArea da;
	JMenuBar bar;
	JMenu jmfile;
	JMenuItem jmiquit, jmiexport;
	ToolBar colorToolbar, penToolbar;
	JSlider thicknessSlider;
	JPanel toolsJPanel;
	
	public DrawIt() {
		SwingUtilities.invokeLater(this);
	}
	
	public void run() {
		jf = new JFrame();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		da = new DrawArea(dim,this);
		//da.setFocusable(true);
		jf.getContentPane().add(da,BorderLayout.CENTER);
		
		
		toolsJPanel = new JPanel();
		toolsJPanel.setLayout(new BoxLayout(toolsJPanel, BoxLayout.PAGE_AXIS));
		
		// create a toolbar
		colorToolbar = new ToolBar(BoxLayout.Y_AXIS);
		colorToolbar.addbutton("Red", Color.RED);
		colorToolbar.addbutton("Blue", Color.BLUE);
		colorToolbar.addbutton("Green", Color.GREEN);
		toolsJPanel.add(colorToolbar);
		
		// make a slider for the line drawing thickness
		toolsJPanel.add(new JLabel("Thickness"));
		thicknessSlider = new JSlider(JSlider.HORIZONTAL,
                1, 30,5);
		toolsJPanel.add(thicknessSlider);
		
		// add a pens tool bar
		penToolbar = new ToolBar(BoxLayout.Y_AXIS);
		penToolbar.addbutton("Pen", Pens.PEN);
		penToolbar.addbutton("Spray", Pens.SPRAY);
		penToolbar.addbutton("Smudge", Pens.SMUDGE);
		toolsJPanel.add(penToolbar);
	
		
		jf.getContentPane().add(toolsJPanel,BorderLayout.LINE_END);
		
		// create some menus
		bar = new JMenuBar();
		jmfile = new JMenu("File");
		jmiexport = new JMenuItem("Export");
		jmfile.add(jmiexport);
		jmiexport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				da.export(new File("export.png"));
			}});
		
		jmiquit = new JMenuItem("Quit");
		jmfile.add(jmiquit);
		jmiquit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}});
		bar.add(jmfile);
		jf.setJMenuBar(bar);
		
		jf.pack();
		jf.setVisible(true);
	}
	
	
	public static void main(String[] args) {
		DrawIt sc = new DrawIt();
	}
}
