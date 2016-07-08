package com.example.danielprimo.imheredei;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Daniel Primo on 03/05/2016.
 */
public class BaseDados extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ap_info.db";
    public static final String AP_TABLE = "access_points";
    public static final String READINGS_TABLE = "readings";
    public static final String POSITIONS = "positions";
    public static final String ROOMS = "rooms";
    public static final String AP_CREATE = "CREATE TABLE 'access_points' "
            + "('building_id' TEXT NOT NULL ,'ssid' TEXT NOT NULL,'mac_id' TEXT NOT NULL )";
    public static final String READINGS_CREATE = "CREATE TABLE 'readings' ('building_id' TEXT NOT NULL , "
            + "'position_id' TEXT NOT NULL ,"
            + " 'ssid' TEXT NOT NULL , 'mac_id' TEXT NOT NULL , 'rssi' INTEGER NOT NULL)";
    public static final String POSITION_READINGS = "CREATE TABLE 'positions' ('building_id' TEXT NOT NULL , "
            + " 'position_id' TEXT NOT NULL ,'posX' TEXT NOT NULL, 'posY' TEXT NOT NULL)";
    public static final String ROOM_POSITIONS = "CREATE TABLE 'rooms' ('building_id' TEXT NOT NULL , "
            + " 'room_id' TEXT NOT NULL ,'posX' TEXT NOT NULL, 'posY' TEXT NOT NULL, "
            +" 'wigth' TEXT NOT NULL, 'height' TEXT NOT NULL)";

    private HashMap hp;

    public BaseDados(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(AP_CREATE);
        db.execSQL(READINGS_CREATE);
        //System.out.println("çççççççççççççççççççççççççççççççççççççççççENTREI FDS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        db.execSQL(POSITION_READINGS);
        db.execSQL(ROOM_POSITIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        //db.execSQL("DROP TABLE IF EXISTS " + AP_CREATE);
        //db.execSQL("DROP TABLE IF EXISTS " + READINGS_CREATE);
        //db.execSQL("DROP TABLE IF EXISTS " + POSITION_READINGS);
        //db.execSQL("DROP TABLE IF EXISTS " + ROOM_POSITIONS);
        //onCreate(db);
    }

    public ArrayList<String> getBuildings() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select distinct building_id from "
                + READINGS_TABLE, null);
        ArrayList<String> result = new ArrayList<String>();
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            result.add(cursor.getString(0));
            cursor.moveToNext();
        }
        return result;

    }

    public ArrayList<AcessPoint> getFriendlyWifis(String building_id) {
        ArrayList<AcessPoint> result = new ArrayList<AcessPoint>();
        System.out.println(building_id);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select ssid,mac_id from " + AP_TABLE
                + " where building_id=?", new String[]{building_id});
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            result.add(new AcessPoint(cursor.getString(0), cursor.getString(1)));
            cursor.moveToNext();
        }
        return result;

    }

    public boolean addFriendlyWifis(String building_id, ArrayList<AcessPoint> wifis) {
        deleteFriendlyWifis(building_id);
        SQLiteDatabase db = getWritableDatabase();
        for (int i = 0; i < wifis.size(); i++) {
            ContentValues cv = new ContentValues();
            cv.put("building_id", building_id);
            cv.put("ssid", wifis.get(i).SSID);
            cv.put("mac_id", wifis.get(i).BSSID);
            db.insert(AP_TABLE, null, cv);
        }
        System.out.println("Adding done");
        return true;
    }

    public int deleteFriendlyWifis(String building_id) {
        SQLiteDatabase db = getWritableDatabase();
        String[] args = new String[] { building_id };
        return db.delete(AP_TABLE, "building_id=?", args);

    }

    public ArrayList<String> getPositions(String building_id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select distinct position_id from "
                        + READINGS_TABLE + " where building_id=?",
                new String[]{building_id});
        ArrayList<String> result = new ArrayList<String>();
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            result.add(cursor.getString(0));
            cursor.moveToNext();
        }
        return result;
    }

    public int deleteReading(String building_id, String position_id) {
        SQLiteDatabase db = getWritableDatabase();
        String[] args = new String[] { building_id, position_id };
        return db.delete(READINGS_TABLE, "building_id=? and position_id=?",
                args);

    }

    public boolean addReadings(String building_id, PositionData positionData) {
        Log.v("Just Before db : ", positionData.toString());
        deleteReading(building_id, positionData.apName);

        SQLiteDatabase db = getWritableDatabase();
        for (Map.Entry<String, Integer> e : positionData.values.entrySet()) {
            ContentValues cv = new ContentValues();
            cv.put("building_id", building_id);
            cv.put("position_id", positionData.apName);
            cv.put("ssid",positionData.routers.get(e.getKey()));
            cv.put("mac_id",e.getKey());
            cv.put("rssi", e.getValue());
            Log.v(e.getKey(), e.getValue().toString());
            db.insert(READINGS_TABLE, null, cv);
        }
        System.out.println("Adding done");
        return true;

    }

    public ArrayList<PositionData> getReadings(String building_id) {
        HashMap<String, PositionData> positions = new HashMap<String, PositionData>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select distinct * from " + READINGS_TABLE
                + " where building_id='" + building_id + "'", null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String position_id = cursor.getString(1);
            AcessPoint router = new AcessPoint(cursor.getString(2), cursor.getString(3));
            Log.v(cursor.getString(2), cursor.getInt(4) + "");
            if (positions.containsKey(position_id)) {

                positions.get(position_id).addValue(router, cursor.getInt(4));
            } else {
                PositionData positionData = new PositionData(
                        cursor.getString(1));
                positionData.addValue(router, cursor.getInt(4));
                positions.put(position_id, positionData);
            }
            cursor.moveToNext();

        }
        System.out.println("Reading done");
        ArrayList<PositionData> result = new ArrayList<PositionData>();
        for (Map.Entry<String, PositionData> e : positions.entrySet())
            result.add(e.getValue());
        return result;

    }

    public int deletePositions(String building_id, String position_id) {
        SQLiteDatabase db = getWritableDatabase();
        System.out.println("-------------------------------------eeeeeeeeeeeeeeeeeeeeeeeeeeeee11111111111111111");
        String[] args = new String[] { building_id, position_id };
        System.out.println("-------------------------------------eeeeeeeeeeeeeeeeeeeeeeeeeeeee22222222222222222222222");
        return db.delete(POSITIONS, "building_id=? and position_id=?",
                args);

    }

    public boolean addPositions(String building_id, PositionData positionData, String posX, String posY) {
        Log.v("Just Before db : ", positionData.toString());
        deletePositions(building_id, positionData.apName);

        //System.out.println("-------------------------------------mmmmmmmmmmmm11111111111111111");
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        //System.out.println("-------------------------------------mmmmmmmmmmmm2222222222222222");
        cv.put("building_id", building_id);
        //System.out.println("-------------------------------------mmmmmmmmmmmm33333333333333");
        cv.put("position_id", positionData.apName);
        //System.out.println("-------------------------------------mmmmmmmmmmmm44444444444444444");
        cv.put("posX", posX);
        //System.out.println("-------------------------------------mmmmmmmmmmmm5555555555555555555555");
        cv.put("posY", posY);
        //System.out.println("-------------------------------------mmmmmmmmmmmm6666666666666666666666");
        db.insert(POSITIONS, null, cv);
        //System.out.println("-------------------------------------mmmmmmmmmmmm777777777777777777");
        System.out.println("Adding done");
        //System.out.println("-------------------------------------mmmmmmmmmmmm888888888888888888888");
        return true;

    }

    public String [] getPosit(String building_id, String position_id) {
        String [] pos= new String[2];
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select distinct * from " + POSITIONS
                + " where building_id='" + building_id + "' and position_id='"+ position_id + "'", null);
        cursor.moveToFirst();
        pos[0]=cursor.getString(2);
        pos[1]=cursor.getString(3);

        System.out.println("Reading Positions done");

        return pos;

    }

    public int deleteRoom(String building_id, String position_id) {
        SQLiteDatabase db = getWritableDatabase();
        System.out.println("-------------------------------------eeeeeeeeeeeeeeeeeeeeeeeeeeeee11111111111111111");
        String[] args = new String[] { building_id, position_id };
        System.out.println("-------------------------------------eeeeeeeeeeeeeeeeeeeeeeeeeeeee22222222222222222222222");
        return db.delete(ROOMS, "building_id=? and room_id=?",
                args);

    }

    public boolean addRoom(String building_id, String roomName, String posX, String posY,String wigth, String height) {
        //Log.v("Just Before db : ", positionData.toString());
        deletePositions(building_id, roomName);

        System.out.println("-------------------------------------mmmmmmmmmmmm11111111111111111");
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        System.out.println("-------------------------------------mmmmmmmmmmmm2222222222222222");
        cv.put("building_id", building_id);
        System.out.println("-------------------------------------mmmmmmmmmmmm33333333333333");
        cv.put("room_id", roomName);
        System.out.println("-------------------------------------mmmmmmmmmmmm44444444444444444");
        cv.put("posX", posX);
        System.out.println("-------------------------------------mmmmmmmmmmmm5555555555555555555555");
        cv.put("posY", posY);
        cv.put("wigth", wigth);
        System.out.println("-------------------------------------mmmmmmmmmmmm5555555555555555555555");
        cv.put("height", height);
        System.out.println("-------------------------------------mmmmmmmmmmmm6666666666666666666666");
        db.insert(ROOMS, null, cv);
        System.out.println("-------------------------------------mmmmmmmmmmmm777777777777777777");
        System.out.println("Adding done");
        System.out.println("-------------------------------------mmmmmmmmmmmm888888888888888888888");
        return true;

    }

    public ArrayList<String> getRoom(String building_id) {
        //String [] pos= new String[5];
        int i;
        ArrayList<String> rooms = new ArrayList<String>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select distinct * from " + ROOMS
                + " where building_id='" + building_id + "'", null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            //System.out.println("ESTOULHE A DAR!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            for(i=1;i<6;i++){
                rooms.add(cursor.getString(i));
            }
            cursor.moveToNext();
        }

        System.out.println("Reading Rooms done");

        return rooms;

    }

    public void criaCenas(){
        SQLiteDatabase db=getWritableDatabase();
        //db.execSQL(POSITION_READINGS);
        db.execSQL(ROOM_POSITIONS);
    }
}
