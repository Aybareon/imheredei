package com.example.danielprimo.imheredei;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Daniel Primo on 17/05/2016.
 */
public class TexturaSala extends GraphicsObject {
    // The order we like to connect them.
    private short[] indices = { 0, 1, 3, 0, 3, 2 };

    private final int stair = 8000;
    private final int spartys = 6000;

    private int textureNum;
    private int roomNum;

    private float texture[];
    private float landscape[]= {
            //Mapping coordinates for the vertices
            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,
    };
    private float portrait[]= {
            //Mapping coordinates for the vertices
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
    };

    private int[] textures = new int[1];
    private float[] vertices;

    private FloatBuffer textureBuffer;

    // Our vertex buffer.
    private FloatBuffer vertexBuffer;

    // Our index buffer.
    private ShortBuffer indexBuffer;

    public TexturaSala(float left, float right, float top, float bottom) {
        float x = Math.abs(right-left);
        float y = Math.abs(top-bottom);
        if(x > y)
            texture = landscape;
        else
            texture = portrait;

        float sizer = 6.0f;
        vertices = new float[]{
                left+x/sizer, bottom+y/sizer, 0.0f,  // 1, Bottom Left
                right-x/sizer, bottom+y/sizer, 0.0f,  // 2, Bottom Right
                left+x/sizer,  top-y/sizer, 0.0f,  // 0, Top Left
                right-x/sizer, top-y/sizer, 0.0f,  // 3, Top Right
        };

        // a float is 4 bytes, therefore we multiply the number if
        // vertices with 4.
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        //
        vbb = ByteBuffer.allocateDirect(texture.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        textureBuffer = vbb.asFloatBuffer();
        textureBuffer.put(texture);
        textureBuffer.position(0);

        // short is 2 bytes, therefore we multiply the number if
        // vertices with 2.
        ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
        ibb.order(ByteOrder.nativeOrder());
        indexBuffer = ibb.asShortBuffer();
        indexBuffer.put(indices);
        indexBuffer.position(0);
    }

    @Override
    public void Draw(GL10 gl) {
        gl.glColor4f(1, 1, 1, 1);
//		gl.glEnable(GL10.GL_ALPHA_BITS);
//
//		gl.glEnable(GL10.GL_BLEND);
//		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        //Bind our only previously generated texture in this case
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);

        // Counter-clockwise winding.
        gl.glFrontFace(GL10.GL_CCW);
        // Enable face culling.
        gl.glEnable(GL10.GL_CULL_FACE);
        // What faces to remove with the face culling.
        gl.glCullFace(GL10.GL_BACK);

        // Enabled the vertices buffer for writing and to be used during
        // rendering.
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        // Specifies the location and data format of an array of vertex
        // coordinates to use when rendering.
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

        gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
                GL10.GL_UNSIGNED_SHORT, indexBuffer);

        // Disable the vertices buffer.
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        // Disable face culling.
        gl.glDisable(GL10.GL_CULL_FACE);
        gl.glDisable(GL10.GL_ALPHA_BITS);
        gl.glDisable(GL10.GL_BLEND);
    }

    public void setRoomNumber(int i) {
        roomNum = i;
    }

    public int getTextureNum(){
        return textureNum;
    }
}
