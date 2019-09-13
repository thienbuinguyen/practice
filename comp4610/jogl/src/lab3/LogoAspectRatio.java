import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureCoords;
import com.jogamp.opengl.util.texture.TextureIO;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.InputStream;

import static com.jogamp.opengl.GL.*;

public class LogoAspectRatio implements GLEventListener {

    /**
     * ScreenSaverOGL - this is a simple screen saver that uses JOGL
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

    Texture texture;
    TextureCoords textureCoords;

    // set up the OpenGL Panel within a JFrame
    public LogoAspectRatio() {
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
        new LogoAspectRatio();
    }

    public void display(GLAutoDrawable dr) {
        GL2 gl = (GL2) dr.getGL();
        GLU glu = new GLU();
        GLUT glut = new GLUT();

        // clear the screen
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        texture.enable(gl);
        texture.bind(gl);
        gl.glBegin(GL2.GL_QUADS);
        float height = dim.width / texture.getAspectRatio();
        int yOffset = (int) (dim.height - height) / 2;

        gl.glTexCoord2f(textureCoords.left(), textureCoords.bottom());
        gl.glVertex2f(0, yOffset);
        gl.glTexCoord2f(textureCoords.right(), textureCoords.bottom());
        gl.glVertex2f(dim.width, yOffset);
        gl.glTexCoord2f(textureCoords.right(), textureCoords.top());
        gl.glVertex2f(dim.width, height + yOffset);
        gl.glTexCoord2f(textureCoords.left(), textureCoords.top());
        gl.glVertex2f(0, height + yOffset);

        gl.glEnd();
        texture.disable(gl);

        gl.glFlush();
    }

    public void displayChanged(GLAutoDrawable dr, boolean arg1, boolean arg2) {
    }


    // init - set up the opengl context
    public void init(GLAutoDrawable dr) {
        GL2 gl = dr.getGL().getGL2();
        GLU glu = new GLU();
        GLUT glut = new GLUT();
        // make the clear colour black
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        try {
            texture = TextureIO.newTexture(new File("./compgraphicslogo.png"), false);
            textureCoords = texture.getImageTexCoords();
        } catch (Exception e) {
            System.out.println("Failed to load texture!");
            System.exit(1);
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
