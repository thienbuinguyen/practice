import java.awt.Dimension;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Random;

import javax.swing.JFrame;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.PMVMatrix;
import com.jogamp.opengl.util.gl2.GLUT;

public class Shader2DColorFan implements GLEventListener {

    /**
     * Shader2DColorFan - this is a simple example for drawing a fan made up up colored triangles
     * using a shader Eric McCreath 2009, 2011, 2015, 2017, 2019
     *
     * When tested on the lab machines I found it worked okay with:
     *    JRE System Library [java-11-openjdk-amd64]
     *    /usr/share/java/gluegen2-rt.jar
     *    /usr/share/java/jogl2.jar
     *
     *    In eclipse to add these go to:
     *       Project->Properties->Java Build Path->Libraries
     *           Added the above external jars and set up the system library if
     *           needed.
     *
     *    From the command line you should be able to run:
     *    $ javac -cp /usr/share/java/jogl2.jar:/usr/share/java/gluegen2-rt.jar:.  Shader2DColorFan.java
     *    $ java -cp /usr/share/java/jogl2.jar:/usr/share/java/gluegen2-rt.jar:.  Shader2DColorFan
     *
     */

    JFrame jf;
    GLProfile profile;
    GLJPanel gljpanel;

    GLCapabilities caps;
    Dimension dim = new Dimension(800, 600);
    FPSAnimator animator;

    float scale;

    PMVMatrix matrix;

    int shaderprogram, vertexshader, fragshader;
    int vertexbuffer[];
    int colorbuffer[];
    int texturebuffer[];

    public Shader2DColorFan() {
        jf = new JFrame("Shader2DColorFan");
        profile = GLProfile.getDefault();
        caps = new GLCapabilities(profile);
        gljpanel = new GLJPanel();
        gljpanel.addGLEventListener(this);
        gljpanel.requestFocusInWindow();
        jf.getContentPane().add(gljpanel);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
        jf.setPreferredSize(dim);
        jf.pack();
        animator = new FPSAnimator(gljpanel, 60);
        scale = 0.1f;

        animator.start();
    }

    public static void main(String[] args) {
        new Shader2DColorFan();
    }

    // this vertex shader basically applies the model view and projections matricies
    static final String vertstr[] = {
            "attribute vec2 aPos;\n" +
            "attribute vec2 texcoord;\n" +
            "attribute vec3 color;\n" +
            "uniform mat4 mvMat, pMat;\n" +
            "varying vec2 tex_coord;\n" +
            "varying vec4 vertex_color;\n" +
            "vec4 mc;\n" +
            "void main() {\n" +
            "    tex_coord = texcoord;\n" +
            "    vertex_color = vec4(color,1.0);\n" +
            "    mc = vec4(aPos.x,aPos.y,0.0,1.0);\n" +
            "    gl_Position = (pMat * mvMat) * mc;\n" +
            "}\n"
    };

    static int vlens[] = new int[1];
    static int flens[] = new int[1];

    // the fragment shader combines the vertex color is a simple pattern based on the texture coordinate.
    static final String fragstr[] = {
            "varying vec4 vertex_color;\n" +
                    "varying vec2 tex_coord;\n" +
                    "void main() {\n" +
                    "   gl_FragColor =  ((2.0 + sin(tex_coord.x*17.0*3.14) + sin(tex_coord.y*11.0*3.14))/4.0) * vertex_color;\n" +
                    "}\n" };

    float[] triangleArray = new float[20 * 3  * 2];
    float[] triangleColorArray = new float[20 * 3 * 3];
    float[] textureCoordArray = new float[20 * 3 * 2];

    public void init(GLAutoDrawable dr) { // set up openGL for 2D drawing
        GL2 gl2 = dr.getGL().getGL2();
        GLU glu = new GLU();
        GLUT glut = new GLUT();

        System.out.println("GL_VERSION : " + gl2.glGetString(GL2.GL_VERSION));
        System.out.println("GL_SHADING_LANGUAGE_VERSION : " + gl2.glGetString(GL2.GL_SHADING_LANGUAGE_VERSION));

        matrix = new PMVMatrix();
        // setup and load the vertex and fragment shader programs

        matrix.glMatrixMode(GL2.GL_PROJECTION);
        matrix.glOrthof(0.0f, (float) dim.getWidth(), 0.0f, (float) dim.getHeight(), -1.0f, 1.0f);

        // matrix.glOrthof(-2.0f, 2.0f, -2.0f, 2.0f, -1.0f, 1.0f);
        // matrix.glFrustumf(-2.0f, 2.0f, -2.0f, 2.0f, 1.0f, 10.0f);
        matrix.glMatrixMode(GL2.GL_MODELVIEW);

        // setup and load the vertex and fragment shader programs

        shaderprogram = gl2.glCreateProgram();

        vertexshader = gl2.glCreateShader(GL2.GL_VERTEX_SHADER);
        vlens[0] = vertstr[0].length();
        gl2.glShaderSource(vertexshader, 1, vertstr, vlens, 0);

        gl2.glCompileShader(vertexshader);

        checkcompileok(gl2, vertexshader, GL2.GL_COMPILE_STATUS);
        gl2.glAttachShader(shaderprogram, vertexshader);

        fragshader = gl2.glCreateShader(GL2.GL_FRAGMENT_SHADER);
        flens[0] = fragstr[0].length();
        gl2.glShaderSource(fragshader, 1, fragstr, flens, 0);
        gl2.glCompileShader(fragshader);

        checkcompileok(gl2, fragshader, GL2.GL_COMPILE_STATUS);
        gl2.glAttachShader(shaderprogram, fragshader);

        gl2.glLinkProgram(shaderprogram);

        checkok(gl2, shaderprogram, GL2.GL_LINK_STATUS);

        gl2.glValidateProgram(shaderprogram);
        checkok(gl2, shaderprogram, GL2.GL_VALIDATE_STATUS);

        // load the vertex, texture coordinates and color data for drawing the triangles
        Random rand = new Random(0);
        for (int p = 0; p < 20; p++) {
            float xs = p * 10.0f - 100.0f;
            triangleArray[6 * p + 0] = 0.0f;
            triangleArray[6 * p + 1] = 0.0f;
            triangleArray[6 * p + 2] = xs;
            triangleArray[6 * p + 3] = 100.0f;
            triangleArray[6 * p + 4] = xs + 10.0f;
            triangleArray[6 * p + 5] = 100.0f;

            float cr = rand.nextFloat();
            float cg = rand.nextFloat();
            float cb = rand.nextFloat();
            for (int ci = 0; ci < 9; ci++) {
                triangleColorArray[9 * p + 0] = cr;
                triangleColorArray[9 * p + 1] = cg;
                triangleColorArray[9 * p + 2] = cb;
                triangleColorArray[9 * p + 3] = cr;
                triangleColorArray[9 * p + 4] = cg;
                triangleColorArray[9 * p + 5] = cb;
                triangleColorArray[9 * p + 6] = cr;
                triangleColorArray[9 * p + 7] = cg;
                triangleColorArray[9 * p + 8] = cb;
            }
            for (int ti = 0; ti < 6; ti++) {
                textureCoordArray[6 * p + 0] = 0.0f;
                textureCoordArray[6 * p + 1] = 0.0f;
                textureCoordArray[6 * p + 2] = 0.0f;
                textureCoordArray[6 * p + 3] = 1.0f;
                textureCoordArray[6 * p + 4] = 1.0f;
                textureCoordArray[6 * p + 5] = 0.0f;
            }
        }

        FloatBuffer triangleVertexBuffer = Buffers.newDirectFloatBuffer(triangleArray);

        FloatBuffer textureCoordBuffer = Buffers.newDirectFloatBuffer(textureCoordArray);

        FloatBuffer triangleColorBuffer = Buffers.newDirectFloatBuffer(triangleColorArray);

        // set up the buffers on the GPU
        vertexbuffer = new int[1];
        gl2.glGenBuffers(1, vertexbuffer, 0);
        gl2.glBindBuffer(GL2.GL_ARRAY_BUFFER, vertexbuffer[0]);
        gl2.glBufferData(GL2.GL_ARRAY_BUFFER, (long) triangleArray.length * 4, triangleVertexBuffer,
                GL2.GL_STATIC_DRAW);

        texturebuffer = new int[1];
        gl2.glGenBuffers(1, texturebuffer, 0);
        gl2.glBindBuffer(GL2.GL_ARRAY_BUFFER, texturebuffer[0]);
        gl2.glBufferData(GL2.GL_ARRAY_BUFFER, (long) textureCoordArray.length * 4, textureCoordBuffer,
                GL2.GL_STATIC_DRAW);

        colorbuffer = new int[1];
        gl2.glGenBuffers(1, colorbuffer, 0);
        gl2.glBindBuffer(GL2.GL_ARRAY_BUFFER, colorbuffer[0]);
        gl2.glBufferData(GL2.GL_ARRAY_BUFFER, (long) triangleColorArray.length * 4, triangleColorBuffer,
                GL2.GL_STATIC_DRAW);

        gl2.glUseProgram(shaderprogram);

        // make the background blue
        gl2.glClearColor(0.0f, 0.0f, 1.0f, 0.0f);

    }

    private void checkok(GL2 gl2, int program, int type) {
        IntBuffer intBuffer = IntBuffer.allocate(1);
        gl2.glGetProgramiv(program, type, intBuffer);
        if (intBuffer.get(0) != GL.GL_TRUE) {
            int[] len = new int[1];
            gl2.glGetProgramiv(program, GL2.GL_INFO_LOG_LENGTH, len, 0);
            if (len[0] != 0) {
                byte[] errormessage = new byte[len[0]];
                gl2.glGetProgramInfoLog(program, len[0], len, 0, errormessage, 0);
                System.err.println("problem\n" + new String(errormessage));
                gljpanel.destroy();
                jf.dispose();
                System.exit(0);
            }
        }
    }

    private void checkcompileok(GL2 gl2, int program, int type) {
        IntBuffer intBuffer = IntBuffer.allocate(1);
        gl2.glGetShaderiv(program, GL2.GL_COMPILE_STATUS, intBuffer);
        if (intBuffer.get(0) == GL.GL_FALSE) {
            int[] len = new int[1];
            gl2.glGetShaderiv(program, GL2.GL_INFO_LOG_LENGTH, len, 0);
            if (len[0] != 0) {
                byte[] errormessage = new byte[len[0]];
                gl2.glGetShaderInfoLog(program, len[0], len, 0, errormessage, 0);
                System.err.println("problem\n" + new String(errormessage));
                gljpanel.destroy();
                jf.dispose();
                System.exit(0);
            }
        }
    }

    public void display(GLAutoDrawable dr) {
        GL2 gl2 = dr.getGL().getGL2();
        GLU glu = new GLU();
        GLUT glut = new GLUT();

        gl2.glUseProgram(shaderprogram);

        gl2.glClear(GL.GL_COLOR_BUFFER_BIT);

        // draw the triangle

        // connect the buffers with input attributes within the shader
        int posVAttrib = gl2.glGetAttribLocation(shaderprogram, "aPos");
        gl2.glEnableVertexAttribArray(posVAttrib);
        gl2.glBindBuffer(GL2.GL_ARRAY_BUFFER, vertexbuffer[0]);
        gl2.glVertexAttribPointer(posVAttrib, 2, GL2.GL_FLOAT, false, 0, 0);

        int texcoordAttrib = gl2.glGetAttribLocation(shaderprogram, "texcoord");
        gl2.glEnableVertexAttribArray(texcoordAttrib);
        gl2.glBindBuffer(GL2.GL_ARRAY_BUFFER, texturebuffer[0]);
        gl2.glVertexAttribPointer(texcoordAttrib, 2, GL2.GL_FLOAT, false, 0, 0);

        int posCAttrib = gl2.glGetAttribLocation(shaderprogram, "color");
        gl2.glEnableVertexAttribArray(posCAttrib);
        gl2.glBindBuffer(GL2.GL_ARRAY_BUFFER, colorbuffer[0]);
        gl2.glVertexAttribPointer(posCAttrib, 3, GL2.GL_FLOAT, false, 0, 0);

        //  set up the matricies
        matrix.glPushMatrix();
        matrix.glTranslatef(dim.width / 2.0f, 0.0f, 0.0f);
        matrix.glScalef(scale, scale, scale);

        int mvMatrixID = gl2.glGetUniformLocation(shaderprogram, "mvMat");
        gl2.glUniformMatrix4fv(mvMatrixID, 1, false, matrix.glGetMvMatrixf());

        int pMatrixID = gl2.glGetUniformLocation(shaderprogram, "pMat");
        gl2.glUniformMatrix4fv(pMatrixID, 1, false, matrix.glGetPMatrixf());

        // do the actual drawing
        gl2.glDrawArrays(GL2.GL_TRIANGLES, 0, 20*3);

        matrix.glPopMatrix();

        gl2.glDisableVertexAttribArray(posVAttrib);
        gl2.glDisableVertexAttribArray(texcoordAttrib);
        gl2.glDisableVertexAttribArray(posCAttrib);

        gl2.glFlush();

        scale *= 1.02;
        if (scale > 360.0)
            scale = 0.1f;
    }

    public void dispose(GLAutoDrawable glautodrawable) {
    }

    public void reshape(GLAutoDrawable dr, int x, int y, int width, int height) {
    }
}