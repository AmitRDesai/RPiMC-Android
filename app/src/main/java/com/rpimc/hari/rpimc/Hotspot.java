package com.rpimc.hari.rpimc;

/**
 * Created by Hari on 10/16/2015.
 */

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Hotspot {
    static public void start(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifi.setWifiEnabled(false);
        WifiConfiguration wificonfig = new WifiConfiguration();
        wificonfig.SSID = "RPiMC";
        wificonfig.allowedAuthAlgorithms
                .set(WifiConfiguration.AuthAlgorithm.OPEN);
        Method method1;
        try {
            method1 = wifi.getClass().getMethod("setWifiApEnabled",
                    WifiConfiguration.class, boolean.class);
            try {
                method1.invoke(wifi, wificonfig, true);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    static public void stop(Context context) {
        WifiConfiguration wificonfig = new WifiConfiguration();
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wificonfig.SSID = "RPiMC";
        wificonfig.allowedAuthAlgorithms
                .set(WifiConfiguration.AuthAlgorithm.OPEN);
        Method method1;
        try {
            method1 = wifi.getClass().getMethod("setWifiApEnabled",
                    WifiConfiguration.class, boolean.class);
            try {
                method1.invoke(wifi, wificonfig, false);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}