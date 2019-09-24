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

public class LightingDemo implements GLEventListener {

    /**
     * Simple3DOGL - this is a simple 3D scene views a cube from a camera that spins
     * around the scene. Eric McCreath 2009, 2011, 2015, 2019
     *
     *
     */

    JFrame jf;
    GLJPanel gljpanel;
    Dimension dim = new Dimension(800, 600);
    FPSAnimator animator;

    float rotation;
    float speed;

    // set up the OpenGL Panel within a JFrame
    public LightingDemo() {
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
        rotation = 0.0f;
        speed = 2f;
        animator.start();
    }

    public static void main(String[] args) {
        new LightingDemo();
    }

    public void init(GLAutoDrawable dr) { // set up openGL for 2D drawing
        GL2 gl2 = dr.getGL().getGL2();
        GLU glu = new GLU();
        GLUT glut = new GLUT();
        gl2.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
        gl2.glEnable(GL2.GL_DEPTH_TEST);

        gl2.glMatrixMode(GL2.GL_PROJECTION);
        gl2.glLoadIdentity();
        glu.gluPerspective(60.0, 1.0, 100.0, 800.0);
    }

    public void display(GLAutoDrawable dr) { // clear the screen and draw the box

        GL2 gl2 = dr.getGL().getGL2();
        GLU glu = new GLU();
        GLUT glut = new GLUT();

        gl2.glMatrixMode(GL2.GL_MODELVIEW);
        gl2.glLoadIdentity();
        glu.gluLookAt(0, 200.0, 400.0, 0.0, 0.0, 0.0, 0.0,
                1.0, 0.0);

        gl2.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        gl2.glEnable(GL2.GL_LIGHTING);
        gl2.glEnable(GL2.GL_LIGHT0);
        gl2.glEnable(GL2.GL_NORMALIZE);

        float ac[] = { Color.pink.getRed() / 255f, Color.pink.getGreen() / 255f, Color.pink.getBlue() / 255f, 1.0f };
        gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ac, 0);

        float dc[] = { 3.0f, 3.0f, 3.0f, 1.0f };
        float sc[] = { 3.0f, 3.0f, 3.0f, 1.0f };
        float lightpos[] = { 50.0f, 300.0f, -500.0f, 1.0f };
        gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightpos, 0);
        gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, dc, 0);
        gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, sc, 0);

        // draw flower
        int numPetals = 10;
        double angle = 360.0 / numPetals;

        gl2.glPushMatrix();
        gl2.glRotated(rotation, 0, 1, 0);
        for (int i = 0; i < numPetals; i++) {
            gl2.glPushMatrix();
            gl2.glTranslated(0, 100, 0);
            gl2.glRotated(60, 1, 0,0);
            gl2.glRotated(i * angle, 0, 1,0);
            gl2.glScaled(0.5, 0.5, 0.5);
            drawPetal(gl2);
            gl2.glPopMatrix();
        }
        gl2.glPopMatrix();

        float df[] = { Color.pink.brighter().getRed() / 255f, Color.pink.brighter().getGreen() / 255f,
                Color.pink.brighter().getBlue() / 255f, 1.0f };
        gl2.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, df,
                0);
        float sf[] = { 0.8f, 0.8f, 0.8f, 0.0f };
        gl2.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, sf, 0);
        gl2.glMaterialf(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, 60.0f);

        gl2.glDisable(GL2.GL_LIGHT0);
        gl2.glDisable(GL2.GL_LIGHTING);

        // draw the floor

        sideRotatedColorScaled(gl2, 300.0, 0, 0, 0,
                90.0, 1.0, 0.0, 0.0, 50.0);

        gl2.glFlush();
        // dr.swapBuffers();
        rotation += speed;
        if (rotation > 360.9f)
            rotation = 0.0f;
    }

    private void drawPetal(GL2 gl2) {
        petalSideRotatedColorScaled(gl2, 200.0, Color.pink.getRed() / 255f,
                Color.pink.getGreen() / 255f, Color.pink.getBlue() / 255f,
                90, 0.0, 1.0, 0.0, 0);
    }

    // draw a single side with a set color and orientation
    private void sideRotatedColorScaled(GL2 gl2, double s, float r, float g, float b, double a, double ax, double ay,
                                        double az, double zoffset) {
        gl2.glPushMatrix();
        gl2.glRotated(a, ax, ay, az);
        gl2.glColor3f(r, g, b);
        gl2.glTranslated(0.0, 0.0, zoffset);
        gl2.glScaled(s, s, s);

        side(gl2);
        gl2.glPopMatrix();
    }

    // draw a single side with a set color and orientation
    private void petalSideRotatedColorScaled(GL2 gl2, double s, float r, float g, float b, double a, double ax, double ay,
                                             double az, double zoffset) {
        gl2.glPushMatrix();
        gl2.glRotated(a, ax, ay, az);
        gl2.glColor3f(r, g, b);
        gl2.glTranslated(0.0, 0.0, zoffset);
        gl2.glScaled(s, s, s);

        petal(gl2);
        gl2.glPopMatrix();
    }

    private void petal(GL2 gl2) {
        gl2.glBegin(GL2.GL_POLYGON);
        gl2.glNormal3d(0, 1, 0);
        gl2.glVertex3d(0.0, 0.0, 0.0);
        for (double d = 0.1; d < 0.95; d += 0.05) {
            gl2.glVertex3d(Math.sin(Math.PI * d), 0.0, 0.1 * Math.sin(Math.PI * 2.0 * d));
        }
        gl2.glEnd();
    }

    // draw a side
    private void side(GL2 gl2) {
        gl2.glBegin(GL2.GL_POLYGON);
        gl2.glVertex3d(-0.5, -0.5, 0.0);
        gl2.glVertex3d(-0.5, 0.5, 0.0);
        gl2.glVertex3d(0.5, 0.5, 0.0);
        gl2.glVertex3d(0.5, -0.5, 0.0);
        gl2.glEnd();
    }

    public void dispose(GLAutoDrawable glautodrawable) {
    }

    public void reshape(GLAutoDrawable dr, int x, int y, int width, int height) {
    }
}