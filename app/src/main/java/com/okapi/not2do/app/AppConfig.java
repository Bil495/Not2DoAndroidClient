package com.okapi.not2do.app;

/**
 * Created by burak on 7/6/2017.
 */

public class AppConfig {
    // Server url
    public static String HOST = "http://192.168.1.5/api/v1/";

    public static String getURLLogin(){
        return HOST + "login";
    }

    public static String getURLRegister(){
        return HOST + "sign_up";
    }
}
