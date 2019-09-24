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

public class OpenglTextureDemo implements GLEventListener {

    /**
     * OpenglTextureDemo - maps a water texture to a quad polygon using OpenGL (no shaders)
     * Thien Bui-Nguyen 14/9/2019
     * Modified from code by Eric McCreath 2019
     */

    JFrame jf;
    GLJPanel gljpanel;
    Dimension dim = new Dimension(800, 600);
    FPSAnimator animator;

    Texture texture;

    // set up the OpenGL Panel within a JFrame
    public OpenglTextureDemo() {
        jf = new JFrame("OpenglTextureDemo");
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
        new OpenglTextureDemo();
    }

    public void display(GLAutoDrawable dr) {
        GL2 gl = (GL2) dr.getGL();
        GLU glu = new GLU();
        GLUT glut = new GLUT();

        // clear the screen
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        gl.glPushMatrix();
        gl.glTranslatef(dim.width / 4f, dim.height / 4f, 0);

        texture.enable(gl);
        texture.bind(gl);
        gl.glBegin(GL2.GL_TRIANGLES);

        TextureCoords textureCoords = texture.getImageTexCoords();
        gl.glTexCoord2f(textureCoords.left(), textureCoords.bottom());
        gl.glVertex2f(0, 0);
        gl.glTexCoord2f(textureCoords.right(), textureCoords.bottom());
        gl.glVertex2f(dim.width / 2f, 0);
        gl.glTexCoord2f(0.5f, textureCoords.top());
        gl.glVertex2f(dim.width / 4f, dim.height / 2f);

        gl.glEnd();
        texture.disable(gl);

        gl.glPopMatrix();
        gl.glFlush();
    }


    // init - set up the opengl context
    public void init(GLAutoDrawable dr) {
        GL2 gl = dr.getGL().getGL2();
        GLU glu = new GLU();
        GLUT glut = new GLUT();
        // make the clear colour black
        gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);

        // set the projection matrix to a simple Orthogonal 2D mapping
        gl.glMatrixMode(GL2.GL_PROJECTION);
        glu.gluOrtho2D(0.0, dim.getWidth(), 0.0, dim.getHeight());

        // any transformation that we do from hear on in will be on the model-view matrix
        gl.glMatrixMode(GL2.GL_MODELVIEW);

        try {
            texture = TextureIO.newTexture(new File("water.jpg"), true);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void reshape(GLAutoDrawable dr, int arg1, int arg2, int arg3,
                        int arg4) {
    }

    @Override
    public void dispose(GLAutoDrawable arg0) {

    }
}
