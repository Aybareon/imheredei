package com.example.danielprimo.imheredei;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Daniel Primo on 17/05/2016.
 */
public class MapaConstructor implements GLSurfaceView.Renderer {
    private ConstructionMap mapa;

    public static float posX = 150;
    public static float posY = 100;
    private float posZ = 0;
    private float xOffset = 0;
    private float yOffset = 0;
    private float zoom = -400;
    private Mapa mid;

    Mapa auxmap;



    public MapaConstructor(Mapa id, String positionX, String positionY) {
        mid=id;
        posX=Float.parseFloat(positionX);
        posY=Float.parseFloat(positionY);
        System.out.println("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq"+posX+"qqqqq"+posY);
        mapa=new ConstructionMap(mid,posX,posY);
        if(posX!=0 && posY!=0){
            zoom=-100;
        }
        else{
            System.out.println("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
            posX = 100;
            posY = 100;
            zoom = -400;
        }
        auxmap=id;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //gl.glEnable(GL10.GL_TEXTURE_2D);
        //mapa.loadTexture(gl, auxmap);
        // Set the background color to black ( rgba ).
        gl.glClearColor(255.0f, 255.0f, 255.0f, 0.5f);
        // Enable Smooth Shading, default not really needed.
        gl.glShadeModel(GL10.GL_SMOOTH);
        // Depth buffer setup.
        gl.glClearDepthf(1.0f);
        // Enables depth testing.
        gl.glEnable(GL10.GL_DEPTH_TEST);
        // The type of depth testing to do.
        gl.glDepthFunc(GL10.GL_LEQUAL);
        // Really nice perspective calculations.
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // Sets the current view port to the new size.
        gl.glViewport(0, 0, width, height);
        // Select the projection matrix
        gl.glMatrixMode(GL10.GL_PROJECTION);
        // Reset the projection matrix
        gl.glLoadIdentity();
        // Calculate the aspect ratio of the window
        GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f,
                400.0f);
        // Select the modelview matrix
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        // Reset the modelview matrix
        gl.glLoadIdentity();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // Clears the screen and depth buffer.
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        // Replace the current matrix with the identity matrix
        gl.glLoadIdentity();

        //draw frame at right zoom and temporary translation

        System.out.println("////////////////////////////////////"+(-(posX+xOffset))+"///////////////"+-(posY +yOffset));
        gl.glTranslatef(-(posX + xOffset), -(posY +yOffset), zoom);


        //gl.glTranslatef(0,0,-400);
        mapa.Draw(gl);
        /*if(routeSet)
            dMap.Draw(gl);
        */
        // Replace the current matrix with the identity matrix
        //gl.glLoadIdentity(); // OpenGL docs
    }
}
