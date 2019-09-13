import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;

import javax.swing.*;
import java.awt.*;

public class ChessboardSpin implements GLEventListener {

    /**
     * HelloWorld - this is a simple screen saver that uses JOGL
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

    float rot = 0;
    float vel = 1;

    // set up the OpenGL Panel within a JFrame
    public ChessboardSpin() {
        jf = new JFrame();
        gljpanel = new GLJPanel();
        gljpanel.addGLEventListener(this);
        gljpanel.requestFocusInWindow();
        jf.getContentPane().add(gljpanel);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
        jf.setPreferredSize(dim);
        jf.pack();
        animator = new FPSAnimator(gljpanel, 20);
        animator.start();
    }

    public static void main(String[] args) {
        new ChessboardSpin();
    }

    private void drawSquare(GL2 gl) {
        gl.glPushMatrix();
        gl.glBegin(GL2.GL_POLYGON);
        gl.glVertex2d(0.0, 0.0);
        gl.glVertex2d(0.0, 50.0);
        gl.glVertex2d(50.0, 50.0);
        gl.glVertex2d(50.0, 0.0);
        gl.glVertex2d(0.0, 0.0);
        gl.glEnd();
        gl.glPopMatrix();
    }

    private void drawChessboard(GL2 gl) {
        gl.glPushMatrix();

        float offset = 4 * 50.f;
        boolean black = false;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                gl.glPushMatrix();
                gl.glTranslatef(i * 50f - offset, j * 50.f - offset, 0);

                if (black) gl.glColor3f(0, 0, 0);
                else gl.glColor3f(1, 1, 1);

                black = !black;

                drawSquare(gl);
                gl.glPopMatrix();
            }
            black = !black;
        }

        gl.glPopMatrix();
    }

    public void display(GLAutoDrawable dr) {
        GL2 gl = (GL2) dr.getGL();
        GLU glu = new GLU();
        GLUT glut = new GLUT();

        // clear the screen
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        gl.glPushMatrix();
        gl.glTranslatef(dim.width / 2, dim.height / 2, 0);
        gl.glRotatef(rot, 0, 0, 1);
        drawChessboard(gl);
        gl.glPopMatrix();

        rot += vel;
        if (rot > 360.0f) rot = 0.0f;

        gl.glFlush();
    }

    public void displayChanged(GLAutoDrawable dr, boolean arg1, boolean arg2) {
    }


    // init - set up the opengl context
    public void init(GLAutoDrawable dr) {
        GL2 gl = dr.getGL().getGL2();
        GLU glu = new GLU();
        GLUT glut = new GLUT();
        // make the clear colour white
        gl.glClearColor(0.0f, 0.0f, 1.0f, 0f);


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
