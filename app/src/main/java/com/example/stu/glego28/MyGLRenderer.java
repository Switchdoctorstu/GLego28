package com.example.stu.glego28;

/**
 * Created by Stu on 23/07/2015.
 */
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;

/**
 *  OpenGL Custom renderer used with GLSurfaceView
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {
    Context context;   // Application's context

    Camera mycam;
    public TextureManager texturemanager;
    // displayed objects
    private Submarine mysub;
    private Sphere mysphere;// ( NEW )
    private Pyramid pyramid;    // (NEW)
    InputHandler inputHandler;
    private SkyBox myskybox;
int screenwidth;
    int screenheight;
    private Hud myhud;
    // viewport paramters
    private float nearplaneZ;  // distance cutoff plane
    private float farplaneZ;    // near view cutoff plane
    private static float anglePyramid = 0; // Rotational angle in degree for pyramid (NEW)
    private static float angleCube = 0;    // Rotational angle in degree for cube (NEW)
    private static float speedPyramid = 0.5f; // Rotational speed for pyramid (NEW)
    private static float speedCube = -0.5f;   // Rotational speed for cube (NEW)

    // try some text
    RGB fontRGB;
    // For controlling cube's z-position, x and y angles and speeds (NEW)
    float angleX = 0;   // (NEW)
    float angleY = 0;   // (NEW)
    float speedX = 0;   // (NEW)
    float speedY = 0;   // (NEW)
    float z = -6.0f;    // (NEW)

    // Constructor with global application context
    public MyGLRenderer(Context context) {
        this.context = context;
        // texture management
        texturemanager=new TextureManager();
        // build our objects
        pyramid = new Pyramid();   // (NEW)
        mysub= new Submarine();
        mysphere = new Sphere(0.75f,2);
        myskybox=new SkyBox(); //

        mycam=new Camera(new Vertex3f(0f,0f,2f),new Vertex3f(0f,0f,0f),new Vertex3f(0f,0f,0f));
        nearplaneZ=0.1f;
        farplaneZ=50.0f;
        // control handler
        inputHandler = new InputHandler();
        myhud=new Hud(inputHandler);
    }

    void userinput(){

    }

    // Call back when the surface is first created or re-created
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);  // Set color's clear-value to black
        gl.glClearDepthf(1.0f);            // Set depth's clear-value to farthest
        gl.glEnable(GL10.GL_DEPTH_TEST);   // Enables depth-buffer for hidden surface removal
        gl.glDepthFunc(GL10.GL_LEQUAL);    // The type of depth testing to do
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);  // nice perspective view
        gl.glShadeModel(GL10.GL_SMOOTH);   // Enable smooth shading of color
        gl.glDisable(GL10.GL_DITHER);      // Disable dithering for better performance
        // mycylinder = new Cylinder(gl,1,2,3,6);
        // You OpenGL|ES initialization code here
        // ......
        // load our textures
        texturemanager.add(R.drawable.aboveseabox);
        texturemanager.add(R.drawable.dayskybox);
        texturemanager.add(R.drawable.polly);
        texturemanager.add(R.drawable.water001);
        texturemanager.add(R.drawable.aluminium003);
        texturemanager.add(R.drawable.yellow2);

        texturemanager.loadTextures(gl,context);

        // Setup Textures each time the surface is created
        mysub.setGLTextureID(texturemanager.getTexID(R.drawable.yellow2));
        mysphere.setGLtextureid(texturemanager.getTexID(R.drawable.polly));
        myskybox.setGLtextureID(texturemanager.getTexID(R.drawable.aboveseabox));
        myhud.setGLTextureid(texturemanager.getTexID(R.drawable.aluminium003));

        // position objects in space
        Vertex3f substart = new Vertex3f(0f,0.0f,-8.0f);
        mysub.setTranslation(substart);
        mysub.setRotation(new Vertex3f(0f,0f,0f));
        mysub.setScale(new Vertex3f(0.5f,0.5f,0.5f));
        myskybox.setScale(new Vertex3f(20f,20f,20f));
        myskybox.setTranslation(new Vertex3f(0,0,0));

        gl.glEnable(GL10.GL_TEXTURE_2D);  // Enable texture (NEW)
    }

    // Call back after onSurfaceCreated() or whenever the window's size changes
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (height == 0) height = 1;   // To prevent divide by zero
        float aspect = (float)width / height;
        screenheight=height;
        screenwidth=width;
        // Set the viewport (display area) to cover the entire window
        gl.glViewport(0, 0, width, height);
        myhud.setHW((float) height, (float) width);

        myhud.setTranslation(new Vertex3f(0f, 0f, -0.2f));
        // myhud.setScale(new Vertex3f(aspect,aspect,aspect));
        // Setup perspective projection, with aspect ratio matches viewport
        gl.glMatrixMode(GL10.GL_PROJECTION); // Select projection matrix
        gl.glLoadIdentity();                 // Reset projection matrix
        // Use perspective projection

        GLU.gluPerspective(gl, 45, aspect, nearplaneZ, farplaneZ);

        gl.glMatrixMode(GL10.GL_MODELVIEW);  // Select model-view matrix
        gl.glLoadIdentity();                 // Reset

        // You OpenGL|ES display re-sizing code here
        // ......
    }

    // Call back to draw the current frame.
    @Override
    public void onDrawFrame(GL10 gl) {
        // Clear color and depth buffers using clear-value set earlier
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        // You OpenGL|ES rendering code here
        gl.glLoadIdentity();                 // Reset the model-view matrix
        gl.glTranslatef(-1.5f, 0.0f, -6.0f); // Translate left and into the screen
        gl.glRotatef(anglePyramid, 0.1f, 1.0f, -0.1f); // Rotate (NEW)
        pyramid.draw(gl);                              // Draw the pyramid (NEW)
        anglePyramid += speedPyramid;   // (NEW)

        gl.glLoadIdentity();                // Reset the model-view matrix
        mysub.setRotation(new Vertex3f(angleX,angleY,0f));
        mysub.draw(gl);

        gl.glLoadIdentity();;
        gl.glTranslatef(1.5f, 0.0f, -3.0f); // Translate right and into the screen
        gl.glRotatef(angleCube, 1.0f, 1.0f, 1.0f); // rotate about the axis (1,1,1) (NEW)
        gl.glTranslatef(0.0f, 0.0f, z);   // Translate into the screen

       gl.glRotatef(angleX, 1.0f, 0.0f, 0.0f); // Rotate (NEW)
       gl.glRotatef(angleY, 0.0f, 1.0f, 0.0f);
       mysphere.draw(gl);

        // Update the rotational angle after each refresh (NEW)
        angleCube += speedCube;         // (NEW)
        angleX += speedX;  // (NEW)
        angleY += speedY;  // (NEW)
        // sky box
        gl.glLoadIdentity();
        myskybox.draw(gl);

        // point the camera
        gl.glLoadIdentity();
       // mycam.point(gl);

        //HUD
        //gl.glLoadIdentity();
        myhud.draw(gl);
        gl.glLoadIdentity();
    }

    public float getNearplaneZ() {
        return nearplaneZ;
    }

    public float getFarplaneZ() {
        return farplaneZ;
    }
}
