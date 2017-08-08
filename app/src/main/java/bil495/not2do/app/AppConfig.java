package bil495.not2do.app;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by burak on 7/6/2017.
 */

public class AppConfig {
    // Server url
    public static String HOST = "https://not2do-rootg.c9users.io/users";

    //Api paths
    public static String PATH_LOGIN = "/log_in";
    public static String PATH_SIGN_UP = "/sign_up";
    public static String PATH_FOLLOW = "/follow?profile_id=%d";
    public static String PATH_UNFOLLOW = "/unfollow?profile_id=%d";
    public static String PATH_GET_NOT2DO = "not2do/%d";
    public static String PATH_FRIENDS_TIMELINE = "/timeline";
    public static String PATH_GLOBAL_TIMELINE = "/discover";
    public static String PATH_MY_PROFILE = "my_profile";
    public static String PATH_USER_PROFILE = "/profile?profile_id=%d";
    public static String PATH_CREATE_NOT2DO = "/create_item";
    public static String PATH_DELETE_NOT2DO = "/delete_item?item_id=%d";
    public static String PATH_PARTICIPANTS = "/participants?item_id=%d";
    public static String PATH_FAILURES = "/failed_participants?item_id=%d";
    public static String PATH_PARTICIPATE = "/participate?item_id=%d";
    public static String PATH_FAILURE = "/failed?item_id=%d";
    public static String PATH_FOLLOWERS = "/followers2?profile_id=%d";
    public static String PATH_FOLLOWINGS = "/followings2?profile_id=%d";
    public static String PATH_WHO_TO_FOLLOW = "/all";
    public static String PATH_EDIT_PROFILE = "/edit_profile";
    public static String PATH_FCM_REGISTER = "/set_fcm_token";

    public static DateFormat format = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss +0000", Locale.ENGLISH);

    public static String getURLLogin(){
        return HOST + PATH_LOGIN;
    }
    public static String getURLRegister(){
        return HOST + PATH_SIGN_UP;
    }
    public static String getURLFollow(int userId){
        return HOST + String.format(PATH_FOLLOW, userId);
    }
    public static String getURLUnfollow(int userId){
        return HOST + String.format(PATH_UNFOLLOW, userId);
    }
    public static String getURLGetNot2Do(Integer not2DoId){
        return HOST + String.format(PATH_GET_NOT2DO, not2DoId);
    }
    public static String getURLFriendsTimeline(Integer lessThan){
        return HOST + PATH_FRIENDS_TIMELINE;
    }
    public static String getURLGlobalTimeline(Integer lessThan){
        return HOST + PATH_GLOBAL_TIMELINE;
    }
    public static String getURLFCMRegister(){
        return HOST + PATH_FCM_REGISTER;
    }
    public static String getURLAllUsers(){
        return HOST + PATH_WHO_TO_FOLLOW;
    }
    public static String getURLEditProfile(){
        return HOST + PATH_EDIT_PROFILE;
    }
    public static String getURLMyProfile(){
        return HOST + PATH_MY_PROFILE;
    }
    public static String getURLUserProfile(int userId){
        return HOST + String.format(PATH_USER_PROFILE, userId);
    }
    public static String getURLCreateNot2Do(){
        return HOST + PATH_CREATE_NOT2DO;
    }

    public static String getURLDeleteNot2Do(long not2DoId){
        return HOST + String.format(PATH_DELETE_NOT2DO, not2DoId);
    }

    public static String getURLParticipantsOfNot2Do(long not2DoId){
        return HOST + String.format(PATH_PARTICIPANTS, not2DoId);
    }
    public static String getURLFailuresOfNot2Do(long not2DoId){
        return HOST + String.format(PATH_FAILURES, not2DoId);
    }
    public static String getURLParticipate(long not2DoId){
        return HOST + String.format(PATH_PARTICIPATE, not2DoId);
    }
    public static String getURLFailure(long not2DoId){
        return HOST + String.format(PATH_FAILURE, not2DoId);
    }
    public static String getURLFollowersOfUser(int userId){
        return HOST + String.format(PATH_FOLLOWERS, userId);
    }
    public static String getURLFollowingOfUser(int userId){
        return HOST + String.format(PATH_FOLLOWINGS, userId);
    }
}
