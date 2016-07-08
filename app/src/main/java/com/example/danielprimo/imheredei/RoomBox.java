package com.example.danielprimo.imheredei;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Daniel Primo on 17/05/2016.
 */
public class RoomBox extends GraphicsObject{

    // The order we like to connect them.
    private short[] indices = { 0, 1, 3, 0, 3, 2 };

    // Our vertex buffer.
    private FloatBuffer vertexBuffer;

    // Our index buffer.
    private ShortBuffer indexBuffer;
    private float[] color = {0,0,0,0};
    private float[] selectedColor = {1,0,0,0.25f};
    private Linha[] bound;

    private int semContorno;
    private Mapa miD;

    private float[] vertices;


    private boolean selected = false;

    public RoomBox(float pointX, float pointY, float width,float height,float pointDoorX,float pointDoorY ,String door,String colorString, Mapa iD) {

        semContorno=0;
        miD = iD;
        System.out.println("ooooooooooooooooooooooooooooooooADGSYUDVSYUDVSYDVSDVoooooooooooooooooo");
        //set the color
        color[3] = 0.25f;
        if(colorString.toLowerCase().equals("blue"))
        {
            color[2] = 1.0f;
        }
        else if(colorString.toLowerCase().equals("red"))
        {
            color[0] = 1.0f;
        }
        else if(colorString.toLowerCase().equals("green"))
        {
            color[1] = 1.0f;
        }
        else if(colorString.toLowerCase().equals("yellow"))
        {
            color[0] = color[1] = 1.0f;
        }
        else if(colorString.toLowerCase().equals("white"))
        {
            color[0] = color[1] = color[2] = 1;
        }
        else if(colorString.toLowerCase().equals("black"))
        {
            color[0] = color[1] = color[2] = 0;
            color[3]=1;
        }
        else if(colorString.toLowerCase().equals("purple"))
        {
            color[0] = color[2] = 1;
        }
        else if(colorString.toLowerCase().equals("grey"))
        {
            color[0] = color[1] = color[2] = 0;
        }
        System.out.println("ooooooooooooooooooooooooooooooooADGSYUDVSYUDVSYDVSDVoooooooooooooooooo22222");
        vertices = new float[]{
                pointX, pointY, 0.0f,  // 0, Bottom Left
                pointX+width,pointY , 0.0f,  // 1, Bottom Right
                pointX,pointY+height  , 0.0f,  // 2, Top Left
                pointX+width,pointY+height , 0.0f,  // 3, Top Right
        };
        System.out.println("ooooooooooooooooooooooooooooooooADGSYUDVSYUDVSYDVSDVoooooooooooooooooo3333");

        if(door.toLowerCase().equals("top")){
            bound = new Linha[5];
            bound[0] = new Linha(pointX, pointY+height, pointX, pointY,"black");//TL to BL
            bound[1] = new Linha(pointX, pointY, pointX+width, pointY,"black");//BL to BR
            bound[2] = new Linha(pointX+width, pointY, pointX+width, pointY+height,"black");//BR to TR
            bound[3] = new Linha(pointX, pointY+height, pointDoorX, pointDoorY,"black");//TL to DOOR
            bound[4] = new Linha(pointDoorX+2, pointDoorY,pointX+width, pointY+height,"black");//DOOR to TR

        }
        else if(door.toLowerCase().equals("left")){
            bound = new Linha[5];
            bound[0] = new Linha(pointX, pointY, pointDoorX, pointDoorY,"black");//BL to DOOR
            bound[1] = new Linha(pointDoorX, pointDoorY+2,pointX,pointY+height,"black");//DOOR to TL
            bound[2] = new Linha(pointX, pointY, pointX+width, pointY,"black");//BL to BR
            bound[3] = new Linha(pointX+width, pointY, pointX+width, pointY+height,"black");//BR to TR
            bound[4] = new Linha(pointX, pointY+height, pointX+width, pointY+height,"black");//TL to TR

        }
        else if(door.toLowerCase().equals("bottom")){
            bound = new Linha[5];
            bound[0] = new Linha(pointX, pointY+height, pointX, pointY,"black");//TL to BL
            bound[1] = new Linha(pointX, pointY, pointDoorX, pointDoorY,"black");//BL to DOOR
            bound[2] = new Linha(pointDoorX+2, pointDoorY,pointX+width,pointY,"black");//DOOR to BR
            bound[3] = new Linha(pointX+width, pointY, pointX+width, pointY+height,"black");//BR to TR
            bound[4] = new Linha(pointX, pointY+height, pointX+width, pointY+height,"black");//TL to TR

        }
        else if(door.toLowerCase().equals("right")){
            bound = new Linha[5];
            bound[0] = new Linha(pointX, pointY+height, pointX, pointY,"black");//TL to BL
            bound[1] = new Linha(pointX, pointY, pointX+width, pointY,"black");//BL to BR
            bound[2] = new Linha(pointX+width, pointY, pointDoorX, pointDoorY,"black");//BR to DOOR
            bound[3] = new Linha(pointDoorX, pointDoorY+2,pointX+width,pointY+height,"black");//BR to DOOR
            bound[4] = new Linha(pointX, pointY+height, pointX+width, pointY+height,"black");//TL to TR

        }
        else if(door.equals("allT")) {
            bound = new Linha[3];
            bound[0] = new Linha(pointX, pointY+height, pointX, pointY,"black");//TL to BL
            bound[1] = new Linha(pointX, pointY, pointX+width, pointY,"black");//BL to BR
            bound[2] = new Linha(pointX+width, pointY, pointX+width, pointY+height,"black");//BR to TR
        }
        else if(door.equals("allB")) {
            bound = new Linha[3];
            bound[0] = new Linha(pointX, pointY+height, pointX, pointY,"black");//TL to BL
            bound[1] = new Linha(pointX+width, pointY, pointX+width, pointY+height,"black");//BR to TR
            bound[2] = new Linha(pointX, pointY+height, pointX+width, pointY+height,"black");//TL to TR
        }
        else if(door.equals("allL")) {
            bound = new Linha[3];
            bound[0] = new Linha(pointX, pointY, pointX+width, pointY,"black");//BL to BR
            bound[1] = new Linha(pointX+width, pointY, pointX+width, pointY+height,"black");//BR to TR
            bound[2] = new Linha(pointX, pointY+height, pointX+width, pointY+height,"black");//TL to TR
        }
        else if(door.equals("allR")) {
            bound = new Linha[3];
            bound[0] = new Linha(pointX, pointY+height, pointX, pointY,"black");//TL to BL
            bound[1] = new Linha(pointX, pointY, pointX+width, pointY,"black");//BL to BR
            bound[2] = new Linha(pointX, pointY+height, pointX+width, pointY+height,"black");//TL to TR
        }
        else if(door.equals("all")) {
            bound = new Linha[4];
            bound[0] = new Linha(pointX, pointY+height, pointX, pointY,"black");//TL to BL
            bound[1] = new Linha(pointX, pointY, pointX+width, pointY,"black");//BL to BR
            bound[2] = new Linha(pointX+width, pointY, pointX+width, pointY+height,"black");//BR to TR
            bound[3] = new Linha(pointX, pointY+height, pointX+width, pointY+height,"black");//TL to TR
        }
        else{
            semContorno=1;
        }
        System.out.println("ooooooooooooooooooooooooooooooooADGSYUDVSYUDVSYDVSDVoooooooooooooooooo4444");
        // a float is 4 bytes, therefore we multiply the number if
        // vertices with 4.
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        // short is 2 bytes, therefore we multiply the number if
        // vertices with 2.
        ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
        ibb.order(ByteOrder.nativeOrder());
        indexBuffer = ibb.asShortBuffer();
        indexBuffer.put(indices);
        indexBuffer.position(0);
    }

    /**
     * This function draws our square on screen.
     * @param gl
     */
    public void Draw(GL10 gl) {
        gl.glEnable(GL10.GL_ALPHA_BITS);

        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        gl.glColor4f(color[0], color[1], color[2], color[3]);

        if(selected)
            gl.glColor4f(selectedColor[0], selectedColor[1], selectedColor[2], selectedColor[3]);

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

        gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
                GL10.GL_UNSIGNED_SHORT, indexBuffer);

        // Disable the vertices buffer.
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        // Disable face culling.
        gl.glDisable(GL10.GL_CULL_FACE);
        gl.glDisable(GL10.GL_ALPHA_BITS);
        gl.glDisable(GL10.GL_BLEND);

        if(semContorno!=1){
            for(Linha l : bound)
            {
                l.Draw(gl);
            }
        }


        selected = false;
    }


}
