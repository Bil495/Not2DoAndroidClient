package bil495.not2do.app;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by burak on 7/6/2017.
 */

public class AppConfig {
    //TODO Change the host to heroku server when it is ready
    // Server url
    public static String HOST = "http://192.168.1.3/api/v1/";

    //Api paths
    public static String PATH_LOGIN = "login";
    public static String PATH_SIGN_UP = "sign_up";
    public static String PATH_FOLLOW = "follow/%s"; //follow/:username
    public static String PATH_UNFOLLOW = "unfollow/%s"; //unfollow/:username
    public static String PATH_GET_NOT2DO = "not2do/%d"; //not2do/:not2do_id
    public static String PATH_FRIENDS_TIMELINE = "following/%d"; //following/:less
    public static String PATH_GLOBAL_TIMELINE = "timeline/%d"; //timeline/:less
    public static String PATH_MY_PROFILE = "my_profile";
    public static String PATH_USER_PROFILE = "user/%s"; //user/:username
    public static String PATH_CREATE_NOT2DO = "not2do";
    public static String PATH_PARTICIPANTS = "not2do/%d/participants"; //not2do/:not2do_id/participants
    public static String PATH_PARTICIPATE = "participate/%d"; //participate/:not2do_id
    public static String PATH_REPORT_PARTICIPATE = "failed/%d/%s"; //failed/:not2do_id/:username

    public static DateFormat format = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss +0000", Locale.ENGLISH);

    public static String getURLLogin(){
        return HOST + PATH_LOGIN;
    }
    public static String getURLRegister(){
        return HOST + PATH_SIGN_UP;
    }
    public static String getURLFollow(String username){
        return HOST + String.format(PATH_FOLLOW, username);
    }
    public static String getURLUnfollow(String username){
        return HOST + String.format(PATH_UNFOLLOW, username);
    }
    public static String getURLGetNot2Do(Integer not2DoId){
        return HOST + String.format(PATH_GET_NOT2DO, not2DoId);
    }
    public static String getURLFriendsTimeline(Integer lessThan){
        return HOST + String.format(PATH_FRIENDS_TIMELINE, lessThan);
    }
    public static String getURLGlobalTimeline(Integer lessThan){
        return HOST + String.format(PATH_GLOBAL_TIMELINE, lessThan);
    }
    public static String getURLMyProfile(){
        return HOST + PATH_MY_PROFILE;
    }
    public static String getURLUserProfile(String username){
        return HOST + String.format(PATH_USER_PROFILE, username);
    }
    public static String getURLCreateNot2Do(){
        return HOST + PATH_CREATE_NOT2DO;
    }
    public static String getURLParticipantsOfNot2Do(long not2DoId){
        return HOST + String.format(PATH_PARTICIPANTS, not2DoId);
    }
    public static String getURLParticipate(Integer not2DoId){
        return HOST + String.format(PATH_PARTICIPATE, not2DoId);
    }
    public static String getURLReport(Integer not2DoId, String username){
        return HOST + String.format(PATH_REPORT_PARTICIPATE, not2DoId, username);
    }
}
