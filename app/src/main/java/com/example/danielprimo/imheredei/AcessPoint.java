package com.example.danielprimo.imheredei;

import java.io.Serializable;

/**
 * Created by Daniel Primo on 04/05/2016.
 */
public class AcessPoint implements Serializable{
    public String BSSID;
    public String SSID;
    public AcessPoint(String ssid, String bssid){
        SSID=ssid;
        BSSID=bssid;
    }

    @Override
    public String toString(){
        return SSID;
    }
    @Override
    public boolean equals(Object arg){
        return ((AcessPoint) arg).BSSID.equals(BSSID);
    }


    @Override
    public int hashCode(){
        return  BSSID.hashCode();
    }

}
