import java.awt.Dimension;
import java.io.File;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

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
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

public class OpenglShaderTextureDemo implements GLEventListener {

    /**
     * OpenglShaderTextureDemo - maps a water texture to a square polygon using OpenGL (using shaders)
     * Thien Bui-Nguyen 14/9/2019
     * Modified from code by Eric McCreath 2019
     **/

    JFrame jf;
    GLProfile profile;
    GLJPanel gljpanel;

    GLCapabilities caps;
    Dimension dim = new Dimension(800, 600);
    FPSAnimator animator;

    PMVMatrix matrix;
    Texture texture;

    int shaderprogram, vertexshader, fragshader;
    int vertexbuffer[];
    int texturebuffer[];

    public OpenglShaderTextureDemo() {
        jf = new JFrame("OpenglShaderTextureDemo");
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
        animator.start();
    }

    public static void main(String[] args) {
        new OpenglShaderTextureDemo();
    }

    // this vertex shader basically applies the model view and projections matricies
    static final String vertstr[] = {
            "#version 330\n" +
            "in vec2 aPos;\n" +
                    "in vec2 texcoord;\n" +
                    "uniform mat4 mvMat,pMat;\n" +
                    "in vec4 mc;\n" +
                    "out vec2 texCoord;\n"+
                    "void main() {\n" +
                    "   texCoord = texcoord;\n"+
                    "    vec4 mce = vec4(aPos.x,aPos.y,0.0,1.0);\n" +
                    "    gl_Position = (pMat * mvMat) * mce;\n" +
                    "}\n"
    };

    static int vlens[] = new int[1];
    static int flens[] = new int[1];

    // the fragment shader combines the vertex color is a simple pattern based on the texture coordinate.
    static final String fragstr[] = {
            "#version 330\n" +
            "in vec2 texCoord;\n" +
                    "uniform float sinVal;\n" +
                    "uniform sampler2D tex;\n"+
                    "out vec4 color;\n" +
                    "void main() {\n" +
//                     "   color = vec4(1,0.5,0,0) * texture(tex, texCoord);\n"+
//                      "   color = vec4(1.0f,sinVal,1.0f,1.0f);\n"+
                    "   color = vec4(1.0f,sinVal,1.0f,1.0f) * texture(tex, texCoord);\n"+
//                    "   color = texture(tex, texCoord);\n"+
                    "}\n" };

    float[] triangleArray = {
        -0.5f, -0.5f,
        0.5f, -0.5f,
        0f, 0.5f,
    };

    float[] textureCoordsArray = {
            0f, 0f,
            1f, 0f,
            0.5f, 1f,
    };

    public void init(GLAutoDrawable dr) { // set up openGL for 2D drawing
        GL2 gl2 = dr.getGL().getGL2();
        GLU glu = new GLU();
        GLUT glut = new GLUT();

        System.out.println("GL_VERSION : " + gl2.glGetString(GL2.GL_VERSION));
        System.out.println("GL_SHADING_LANGUAGE_VERSION : " + gl2.glGetString(GL2.GL_SHADING_LANGUAGE_VERSION));

        try {
            texture = TextureIO.newTexture(new File("water.jpg"), false);
        } catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

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

        FloatBuffer triangleVertexBuffer = Buffers.newDirectFloatBuffer(triangleArray);
        FloatBuffer textureCoordBuffer = Buffers.newDirectFloatBuffer(textureCoordsArray);

        // set up the buffers on the GPU
        vertexbuffer = new int[1];
        gl2.glGenBuffers(1, vertexbuffer, 0);
        gl2.glBindBuffer(GL2.GL_ARRAY_BUFFER, vertexbuffer[0]);
        gl2.glBufferData(GL2.GL_ARRAY_BUFFER, (long) triangleArray.length * 4, triangleVertexBuffer,
                GL2.GL_STATIC_DRAW);

        texturebuffer = new int[1];
        gl2.glGenBuffers(1, texturebuffer, 0);
        gl2.glBindBuffer(GL2.GL_ARRAY_BUFFER, texturebuffer[0]);
        gl2.glBufferData(GL2.GL_ARRAY_BUFFER, (long) textureCoordsArray.length * 4, textureCoordBuffer,
                GL2.GL_STATIC_DRAW);

        gl2.glActiveTexture(GL.GL_TEXTURE0);
        texture.enable(gl2);
        texture.bind(gl2);

        gl2.glUseProgram(shaderprogram);

        // make the background white
        gl2.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
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

        int valAttrib = gl2.glGetUniformLocation(shaderprogram, "sinVal");
        gl2.glUniform1f(valAttrib, (float) Math.sin((double) System.currentTimeMillis() / 1000L));

        //  set up the matricies
        matrix.glPushMatrix();

        int mvMatrixID = gl2.glGetUniformLocation(shaderprogram, "mvMat");
        matrix.glTranslatef(dim.width / 2f, dim.height / 2f, 0);
        matrix.glScalef(dim.width / 2f, dim.height / 2f, 0);

        gl2.glUniformMatrix4fv(mvMatrixID, 1, false, matrix.glGetMvMatrixf());

        int pMatrixID = gl2.glGetUniformLocation(shaderprogram, "pMat");
        gl2.glUniformMatrix4fv(pMatrixID, 1, false, matrix.glGetPMatrixf());

        // do the actual drawing
        texture.enable(gl2);
        texture.bind(gl2);
        gl2.glDrawArrays(GL2.GL_TRIANGLES, 0, triangleArray.length * 2);

        matrix.glPopMatrix();

        gl2.glDisableVertexAttribArray(posVAttrib);
        gl2.glDisableVertexAttribArray(texcoordAttrib);

        gl2.glFlush();
    }

    public void dispose(GLAutoDrawable glautodrawable) {
    }

    public void reshape(GLAutoDrawable dr, int x, int y, int width, int height) {
    }
}