package com.example.danielprimo.imheredei;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Daniel Primo on 04/05/2016.
 */
public class PositionData implements Serializable {
    public String apName;
    public HashMap<String, Integer> values;
    public HashMap<String,String> routers;
    public static final int MAX_DISTANCE=99999999;
    public static final int MINIMUM_COMMON_ROUTERS=3;
    public PositionData(String name) {
        // TODO Auto-generated constructor stub
        apName=name;
        values = new HashMap<String, Integer>();
        routers = new HashMap<String, String>();

    }

    public void addValue(AcessPoint ap,int strength){

        values.put(ap.BSSID, strength);
        routers.put(ap.BSSID,ap.SSID);

    }

    public int uDistance(PositionData arg,ArrayList<AcessPoint> friendlyWifis){
        int sum=0;
        int count=0;
        //System.out.println("ooooooooooooooooo>"+arg.values);
        //System.out.println(".................>");
        //System.out.println("--------->"+arg.apName+"-->"+arg.values);

        for(Map.Entry<String, Integer> e: this.values.entrySet()){
            int v;
            //Log.v("Key : ",arg.values.get(e.getKey()).toString());
            if(isFriendlyWifi(friendlyWifis,e.getKey()) && arg.values.containsKey(e.getKey()))
            {
                //System.out.println("--------->"+this.values);
                v=arg.values.get(e.getKey());
                sum+=Math.pow((v-e.getValue()),2);
                count++;
            }
        }
        if(count<MINIMUM_COMMON_ROUTERS){
            sum=MAX_DISTANCE;
        }

        return sum;
    }

    private boolean isFriendlyWifi(ArrayList<AcessPoint> wifis,String bssid){
        for(int i=0;i<wifis.size();i++){
            if(wifis.get(i).BSSID.equals(bssid))
                //System.out.println("oooooooooo>"+bssid);
                return true;

        }
        return false;

    }
}
