import java.awt.*;

import javax.swing.JComponent;


public class HelloWorldCanvas extends JComponent {

    // SliderCanvas - a simple jcomponent which draws a boarder and a line of length value

    static final int gap = 30;

    int value;

    public HelloWorldCanvas(int v) {
        value = v;
    }

    public Dimension getPreferredSize() {
        return new Dimension(600, 600);
    }

    void setValue(int v) {
        value = v;
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension dim = this.getSize();

        // fill the background
        g.setColor(Color.white);
        g.fillRect(0, 0, dim.width, dim.height);

        Graphics2D g2 = (Graphics2D) g;
        Font font = new Font("TimesRoman", Font.PLAIN, value);
        FontMetrics fm = g2.getFontMetrics(font);
        String text = "Hello World";

        g2.setFont(font);
        g2.setColor(Color.black);

        int strWidth = fm.stringWidth(text);
        int strHeight = fm.getHeight();

        if (strWidth > dim.width || strHeight > dim.height) {
            g2.setColor(Color.red);
            g2.fillRect(0, 0, dim.width, dim.height);
        } else {
            g2.drawString("Hello World", (dim.width  - strWidth) / 2, (dim.height + strHeight) / 2);
            g2.drawRect((dim.width  - strWidth) / 2, (dim.height - strHeight) / 2, strWidth, strHeight);
        }

//		// draw box stacks
//		int size = 200;
//		int x = gap;
//		int y = dim.height - size - 1;
//
//		while (size >= 1) {
//			g.drawRect(x, y, size, size);
//			size = size / 2;
//			y -= size - 1;
//			x += size / 2;
//		}
//
//		// draw pyramid
//		int offset = 400;
//		int pyramidWdith = 200;
//		g.drawLine(offset, 0, offset, dim.height);
//		g.drawLine(offset, 0, offset - pyramidWdith / 2, dim.height);
//		g.drawLine(offset, 0, offset + pyramidWdith / 2, dim.height);
    }
}
