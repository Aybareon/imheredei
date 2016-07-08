package com.example.danielprimo.imheredei;

import java.util.HashMap;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Daniel Primo on 16/05/2016.
 */
public class ConstructionMap extends GraphicsObject{

    private Vector<GraphicsObject> floor4;

    private HashMap<Integer, Integer> roomSquarePos = new HashMap<Integer, Integer>();

    public static float posX;
    public static float posY;
    private float posZ;

    private Ponto ponto;

    public ConstructionMap(Mapa mid,float positionX, float positionY){
        floor4 = new Vector<GraphicsObject>();
        posX=positionX;
        posY=positionY;
        /*if(positionX!=0 && positionY!=0){
            System.out.println("-------------------------------------ENTREI!!!!!!!!!!!!!!---------------------");
            ponto= new Ponto(positionX,positionY,0);
        }*/
        ponto= new Ponto(positionX,positionY,0);
        int pos1 = 0;
        //System.out.println("oooooooooooooooooooooooooooooooooooo>Estou de saida!!");

        //floor4.add(new Linha(6,67,30,67,"black"));pos1++;
        floor4.add(new Linha(6,67,6,152,"black"));pos1++;
        floor4.add(new Linha(6,82,10,82,"black"));pos1++;
        floor4.add(new Linha(6,152,27,152,"black"));pos1++;
        floor4.add(new Linha(16.5f,152,16.5f,149,"black"));pos1++;
        floor4.add(new Linha(16.5f,120,16.5f,147,"black"));pos1++;
        floor4.add(new Linha(27,136.5f,27,147,"black"));pos1++;
        floor4.add(new Linha(27,149,27,152,"black"));pos1++;
        floor4.add(new Linha(18.5f,136.5f,27.5f,136.5f,"black"));pos1++;
        floor4.add(new Linha(18.5f,144.5f,27,144.5f,"black"));pos1++;
        floor4.add(new Linha(15f,155f,15,160f,"black"));pos1++;
        floor4.add(new Linha(15f,160f,30,160f,"black"));pos1++;
        floor4.add(new Linha(23f,155f,27,155f,"black"));pos1++;
        floor4.add(new Linha(30f,89f,30,160f,"black"));pos1++;

        floor4.add(new Linha(30f,155.5f,246,155.5f,"black"));pos1++;
        floor4.add(new Linha(246f,155.5f,246,66f,"black"));pos1++;
        floor4.add(new Linha(30f,153.5f,54,153.5f,"black"));pos1++;
        floor4.add(new Linha(71f,153.5f,246,153.5f,"black"));pos1++;
        floor4.add(new Linha(30f,89f,96f,89f,"black"));pos1++;
        floor4.add(new Linha(126f,89f,156f,89f,"black"));pos1++;
        floor4.add(new Linha(186f,89f,216f,89f,"black"));pos1++;


        //add rooms
        String elevatorStair = "gray", garden = "green", classroom = "red", wc = "yellow",
         unknown = "black", bar = "purple", office= "blue", nothing="white";


        floor4.add(new RoomBox(10f, 76f,5f,6f,11.5f,82f,"top",elevatorStair, mid));
        floor4.add(new RoomBox(14.5f, 89f,2f,1f,0f,0f,"not",unknown, mid));
        floor4.add(new RoomBox(14.5f, 96.5f,2f,1f,0f,0f,"not",unknown, mid));
        floor4.add(new RoomBox(14.5f, 104f,2f,9f,0f,0f,"not",unknown, mid));
        floor4.add(new RoomBox(14.5f, 120f,2f,1f,0f,0f,"not",unknown, mid));
        floor4.add(new RoomBox(14.5f, 128f,2f,1f,0f,0f,"not",unknown, mid));
        floor4.add(new RoomBox(14.5f, 136f,2f,1f,0f,0f,"not",unknown, mid));
        floor4.add(new RoomBox(14.5f, 144f,2f,1f,0f,0f,"not",unknown, mid));
        floor4.add(new RoomBox(6f, 89f,4f,25f,0f,0f,"allT",elevatorStair, mid));
        floor4.add(new RoomBox(6f, 120f,5f,5f,11f,122f,"right",office, mid));
        floor4.add(new RoomBox(6f, 125f,5f,11f,11f,129f,"right",wc, mid));
        floor4.add(new RoomBox(6f, 136f,5f,8.5f,11f,140f,"right",wc, mid));
        floor4.add(new RoomBox(15f, 152f,8f,3f,16f,155f,"top",nothing, mid));
        floor4.add(new RoomBox(20.5f, 155f,4f,2.5f,0f,0f,"allR",elevatorStair, mid));
        floor4.add(new RoomBox(16.5f, 128f,13.5f,8.5f,0f,0f,"not",bar, mid));
        floor4.add(new RoomBox(54f, 145f,17f,10.5f,0f,0f,"allB",nothing, mid));
        floor4.add(new RoomBox(42f, 99f,42f,37f,0f,0f,"not",garden, mid));
        floor4.add(new RoomBox(71f, 136f,13f,11.5f,0f,0f,"not",garden, mid));
        floor4.add(new RoomBox(88f, 99f,8f,48.5f,0f,0f,"not",garden, mid));
        floor4.add(new RoomBox(96f, 89f,13f,19.5f,109f,91f,"right",classroom, mid));
        floor4.add(new RoomBox(96f, 108.5f,13f,19.5f,109f,110f,"right",classroom, mid));
        floor4.add(new RoomBox(96f, 128f,13f,19.5f,109f,129.5f,"right",classroom, mid));
        floor4.add(new RoomBox(113f, 89f,13f,19.5f,113f,91f,"left",classroom, mid));
        floor4.add(new RoomBox(113f, 108.5f,13f,19.5f,113f,110f,"left",classroom, mid));
        floor4.add(new RoomBox(113f, 128f,13f,19.5f,113f,129.5f,"left",classroom, mid));
        floor4.add(new RoomBox(126f, 89f,12f,58.5f,0f,0f,"not",garden, mid));
        floor4.add(new RoomBox(144f, 89f,12f,58.5f,0f,0f,"not",garden, mid));
        floor4.add(new RoomBox(156f, 89f,13f,29f,169f,106f,"right",classroom, mid));
        floor4.add(new RoomBox(156f, 118f,13f,14.75f,169f,120f,"right",classroom, mid));
        floor4.add(new RoomBox(156f, 132.75f,13f,14.75f,169f,135f,"right",classroom, mid));
        floor4.add(new RoomBox(173f, 89f,13f,14.5f,173f,91f,"left",classroom, mid));
        floor4.add(new RoomBox(173f, 103.5f,13f,14.5f,173f,110f,"left",classroom, mid));
        floor4.add(new RoomBox(173f, 118f,13f,14.75f,173f,120f,"left",classroom, mid));
        floor4.add(new RoomBox(173f, 132.75f,13f,14.75f,173f,135f,"left",classroom, mid));
        floor4.add(new RoomBox(186f, 89f,12f,58.5f,0f,0f,"not",garden, mid));
        floor4.add(new RoomBox(204f, 89f,12f,58.5f,0f,0f,"not",garden, mid));
        floor4.add(new RoomBox(216f, 89f,13f,19.5f,229f,91f,"right",classroom, mid));
        floor4.add(new RoomBox(216f, 108.5f,13f,19.5f,229f,110f,"right",classroom, mid));
        floor4.add(new RoomBox(216f, 128f,13f,19.5f,229f,129.5f,"right",classroom, mid));
        floor4.add(new RoomBox(233f, 89f,13f,19.5f,233f,91f,"left",classroom, mid));
        floor4.add(new RoomBox(233f, 108.5f,13f,19.5f,233f,110f,"left",classroom, mid));
        floor4.add(new RoomBox(233f, 128f,13f,19.5f,233f,129.5f,"left",classroom, mid));
        floor4.add(new RoomBox(116.5f, 147.5f,4f,2.5f,0f,0f,"allL",elevatorStair, mid));
        floor4.add(new RoomBox(176.5f, 147.5f,4f,2.5f,0f,0f,"allL",elevatorStair, mid));
        floor4.add(new RoomBox(236.5f, 147.5f,4f,2.5f,0f,0f,"allL",elevatorStair, mid));
        floor4.add(new RoomBox(6f, 67f,24f,9f,0f,0f,"not",unknown, mid));
        floor4.add(new RoomBox(6f, 76f,4f,6f,0f,0f,"not",unknown, mid));
        floor4.add(new RoomBox(15f, 76f,15f,4f,0f,0f,"not",unknown, mid));
        floor4.add(new RoomBox(30f, 69f,66f,11f,0f,0f,"not",unknown, mid));
        floor4.add(new RoomBox(96f, 69f,30f,7f,0f,0f,"not",unknown, mid));
        floor4.add(new RoomBox(126f, 69f,30f,11f,0f,0f,"not",unknown, mid));
        floor4.add(new RoomBox(156f, 69f,30f,7f,0f,0f,"not",unknown, mid));
        floor4.add(new RoomBox(186f, 69f,30f,11f,0f,0f,"not",unknown, mid));
        floor4.add(new RoomBox(216f, 69f,30f,7f,0f,0f,"not",unknown, mid));

        floor4.add(new RoomBox(96f, 76f,8f,7f,0f,0f,"allR",nothing, mid));
        floor4.add(new RoomBox(104f, 76f,4f,3.5f,0f,0f,"allR",elevatorStair, mid));
        floor4.add(new RoomBox(104f, 79.5f,4f,3.5f,0f,0f,"allR",elevatorStair, mid));
        floor4.add(new RoomBox(114f, 76f,12f,2f,0f,0f,"allL",nothing, mid));
        floor4.add(new RoomBox(114f, 78f,6f,5f,114f,80.5f,"left",elevatorStair, mid));
        floor4.add(new RoomBox(120f, 78f,6f,5f,0f,0f,"allL",nothing, mid));

        floor4.add(new RoomBox(156f, 76f,8f,7f,0f,0f,"allR",nothing, mid));
        floor4.add(new RoomBox(164f, 76f,4f,3.5f,0f,0f,"allR",elevatorStair, mid));
        floor4.add(new RoomBox(164f, 79.5f,4f,3.5f,0f,0f,"allR",elevatorStair, mid));
        floor4.add(new RoomBox(174f, 76f,12f,2f,0f,0f,"allL",nothing, mid));
        floor4.add(new RoomBox(174f, 78f,6f,5f,174f,80.5f,"left",elevatorStair, mid));
        floor4.add(new RoomBox(180f, 78f,6f,5f,0f,0f,"allL",nothing, mid));

        floor4.add(new RoomBox(216f, 76f,8f,7f,0f,0f,"allR",nothing, mid));
        floor4.add(new RoomBox(224f, 76f,4f,3.5f,0f,0f,"allR",elevatorStair, mid));
        floor4.add(new RoomBox(224f, 79.5f,4f,3.5f,0f,0f,"allR",elevatorStair, mid));
        floor4.add(new RoomBox(234f, 76f,12f,2f,0f,0f,"allL",nothing, mid));
        floor4.add(new RoomBox(234f, 78f,6f,5f,234f,80.5f,"left",elevatorStair, mid));
        floor4.add(new RoomBox(240f, 78f,6f,5f,0f,0f,"allL",nothing, mid));

        //Desenha Points
        //BAR
        floor4.add(new RoomBox(18f,126f,1f,1f,0f,0f,"all",office, mid));
        floor4.add(new RoomBox(28f,126f,1f,1f,0f,0f,"all",office, mid));
        floor4.add(new RoomBox(13f,119f,1f,1f,0f,0f,"all",office, mid));
        floor4.add(new RoomBox(23f,119f,1f,1f,0f,0f,"all",office, mid));
        floor4.add(new RoomBox(18f,109f,1f,1f,0f,0f,"all",office, mid));
        floor4.add(new RoomBox(28f,109f,1f,1f,0f,0f,"all",office, mid));
        floor4.add(new RoomBox(13f,103f,1f,1f,0f,0f,"all",office, mid));
        floor4.add(new RoomBox(23f,103f,1f,1f,0f,0f,"all",office, mid));
        floor4.add(new RoomBox(13f,93f,1f,1f,0f,0f,"all",office, mid));
        floor4.add(new RoomBox(23f,93f,1f,1f,0f,0f,"all",office, mid));
        floor4.add(new RoomBox(23f,84f,1f,1f,0f,0f,"all",office, mid));
        //WC
        floor4.add(new RoomBox(9f,141f,1f,1f,0f,0f,"all",office, mid));
        floor4.add(new RoomBox(9f,129f,1f,1f,0f,0f,"all",office, mid));
        floor4.add(new RoomBox(13f,135f,1f,1f,0f,0f,"all",office, mid));
        //ELEVADOR
        floor4.add(new RoomBox(13f,84f,1f,1f,0f,0f,"all",office, mid));





    }

    @Override
    public void Draw(GL10 GL) {
        for(GraphicsObject i:floor4){
            i.Draw(GL);
            //System.out.println("____________________________________________CTYVBUNI_________________");

        }
        /*if(posX!=0 && posY!=0) {
            ponto.Draw(GL);
        }*/
        ponto.Draw(GL);
    }

    public void loadTexture(GL10 gl, Mapa miD) {
        for (GraphicsObject r : floor4) {
            /*if (r instanceof RoomSquare)
                ((RoomSquare) r).loadGLTexture(gl, miD);*/
        }
    }

}
