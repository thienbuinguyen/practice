import java.awt.*;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.gl2.GLUT;

public class StarWarsCrawl implements GLEventListener {

    /**
     * StarWarsCrawl - this is a simple screen saver that uses JOGL
     * Eric McCreath 2009,2011,2017,2019
     *
     * You need to include the jogl jar files (gluegen2-rt.jar and jogl2.jar). In
     * eclipse use "add external jars" in Project->Properties->Java Build Path->Libraries
     * otherwise make certain they are in the class path.  In the current linux
     * computers there files are in the /usr/share/java directory.
     *
     * If you are executing from the command line then something like:
     *   javac -cp .:/usr/share/java/jogl2.jar:/usr/share/java/gluegen2-rt.jar ScreenSaverOGL.java
     *   java -cp .:/usr/share/java/jogl2.jar:/usr/share/java/gluegen2-rt.jar ScreenSaverOGL
     * should work.
     *
     * You may also need set up the LD_LIBRARY_PATH environment variable. It should point to a
     * directory that contains: libgluegen-rt.so, libjogl_cg.so, libjogl_awt.so,
     * and libjogl.so. In eclipse this can be done in the "Run Configurations.."
     * by adding an environment variable.  In the current linux set up th LD_LIBRARY_PATH
     * does not need to change.
     *
     * I found java 11 to work rather than java 8.
     *
     */

    JFrame jf;
    //GLCanvas canvas;
    //GLCapabilities caps;
    GLJPanel gljpanel;
    Dimension dim = new Dimension(800, 600);
    FPSAnimator animator;
    TextRenderer renderer;

    float maxTexLen = 0;

    float ypos;
    float yvel;

    private int textVertGap = 60;
    private int fontSize = 40;
    private String txt[] = { "Not so long ago", "in a garage far, far away ...", "BAR WARS", "A New Soap",
            "It was a period of blocked pores", "the revlon allience...", "I am running out of things",
            "to say about soap", "so I will just repeat some text.", "I am running out of things", "to say about soap",
            "so I will just repeat some text.", "I am running out of things", "to say about soap",
            "so I will just repeat some text.", "I am running out of things", "to say about soap",
            "so I will just repeat some text.", "I am running out of things", "to say about soap",
            "so I will just repeat some text." };


    // set up the OpenGL Panel within a JFrame
    public StarWarsCrawl() {
        jf = new JFrame();
        gljpanel = new GLJPanel();
        gljpanel.addGLEventListener(this);
        gljpanel.requestFocusInWindow();
        jf.getContentPane().add(gljpanel);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
        jf.setPreferredSize(dim);
        jf.pack();

        renderer = new TextRenderer(new Font("SansSerif", Font.BOLD, fontSize));

        animator = new FPSAnimator(gljpanel, 20);
        ypos = 0f;
        yvel = 1.0f;
        animator.start();
    }

    public static void main(String[] args) {
        new StarWarsCrawl();
    }

    public void display(GLAutoDrawable dr) {
        GL2 gl = (GL2) dr.getGL();
        GLU glu = new GLU();
        GLUT glut = new GLUT();

        // clear the screen
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        final float height = 100f;
        float scaleFactor = dim.width / maxTexLen;

        gl.glColor3f(1, 1,0);
        for (int i = 0; i < txt.length; i++) {
            float len = glut.glutStrokeLengthf(GLUT.STROKE_ROMAN, txt[i]);
            float newWidth = scaleFactor * len;
            float newHeight = scaleFactor * height;
            gl.glPushMatrix();
            gl.glTranslatef((dim.width - newWidth) / 2, (ypos - i * (newHeight * 3f)) - newHeight, 0);
            gl.glScalef(scaleFactor, scaleFactor, 0);
            glut.glutStrokeString(GLUT.STROKE_ROMAN, txt[i]);
            gl.glPopMatrix();
        }

        ypos += yvel;
        gl.glFlush();

//        if ((ypos - txt.length * textVertGap) - fontSize < dr.getSurfaceHeight()) {
//            // draw red text at (xpos,300) on the screen
//            renderer.beginRendering(dr.getSurfaceWidth(), dr.getSurfaceHeight());
//            renderer.setColor(Color.yellow);
//
//            for (int i = 0; i < txt.length; i++) {
//                Rectangle2D r = renderer.getBounds(txt[i]);
//                renderer.draw(txt[i], (int) ((dr.getSurfaceWidth() - r.getWidth()) / 2), (int) (ypos - i * textVertGap) - fontSize);
//            }
//
//            ypos += yvel;
//            renderer.flush();
//        }

//        gl.glFlush();
    }

    public void displayChanged(GLAutoDrawable dr, boolean arg1, boolean arg2) {
    }


    // init - set up the opengl context
    public void init(GLAutoDrawable dr) {
        GL2 gl = dr.getGL().getGL2();
        GLU glu = new GLU();
        GLUT glut = new GLUT();
        // make the clear colour black
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        for (String s : txt) {
            float len = glut.glutStrokeLengthf(GLUT.STROKE_ROMAN, s);
            if (len > maxTexLen) maxTexLen = len;
        }


        // set the projection matrix to a simple Orthogonal 2D mapping
        gl.glMatrixMode(GL2.GL_PROJECTION);
        glu.gluOrtho2D(0.0, dim.getWidth(), 0.0, dim.getHeight());

        // any transformation that we do from hear on in will be on the model-view matrix
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    public void reshape(GLAutoDrawable dr, int arg1, int arg2, int arg3,
                        int arg4) {
    }

    @Override
    public void dispose(GLAutoDrawable arg0) {

    }
}
