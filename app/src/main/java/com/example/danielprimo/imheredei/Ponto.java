package com.example.danielprimo.imheredei;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Daniel Primo on 17/05/2016.
 */
public class Ponto extends GraphicsObject{

    private float x1,y1,z1;
    private FloatBuffer vertexBuffer;
    private ShortBuffer indexBuffer;

    private float[] color = new float[4];
    private short[] indices = {0, 1, 2, 3};

    public Ponto(float x1, float y1, float z1){
        UpdateLocation(x1, y1, z1);

        color[0] = 0;
        color[1] = 0;
        color[2] = 0;
        color[3] = 0;
    }

    @Override
    public void Draw(GL10 gl) {
        gl.glColor4f(color[0], color[1], color[2], color[3]);
        // Counter-clockwise winding.
        gl.glFrontFace(GL10.GL_CCW);
        // Enable face culling.
        gl.glEnable(GL10.GL_CULL_FACE);
        // What faces to remove with the face culling.
        gl.glCullFace(GL10.GL_BACK);

        // Enabled the vertices buffer for writing and to be used during
        // rendering.
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        // Specifies the location and data format of an array of vertex
        // coordinates to use when rendering.
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

        gl.glDrawElements(GL10.GL_LINES, indices.length,
                GL10.GL_UNSIGNED_SHORT, indexBuffer);

        // Disable the vertices buffer.
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        // Disable face culling.
        gl.glDisable(GL10.GL_CULL_FACE);
    }

    public void UpdateLocation(float x, float y, float z){
        x1 = x;
        y1 = y;
        z1 = z;
        System.out.println("POOOOOOOOOOOOOOONTO__"+x1+"___"+y1);
        float vertices[] = {x1-1f, y1+1f,0,
                x1+1f, y1-1f,0,
                x1+1f, y1+1f,0,
                x1-1f, y1-1f,0};


        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        // 	short is 2 bytes, therefore we multiply the number if
        // 	vertices with 2.
        ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
        ibb.order(ByteOrder.nativeOrder());
        indexBuffer = ibb.asShortBuffer();
        indexBuffer.put(indices);
        indexBuffer.position(0);
    }
}
