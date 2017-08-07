package bil495.not2do.helper;

/**
 * Created by burak on 7/6/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "Not2DoLogin";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_FCM_TOKEN = "fcm_token";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public  void setFCMToken(final String fcmToken){
        editor.putString(KEY_FCM_TOKEN, fcmToken);
        editor.commit();
    }

    public void setUsernameAndToken(final int id, final String username, final String token) {

        editor.putInt(KEY_ID, id);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_TOKEN, token);

        // commit changes
        editor.commit();

        Log.d(TAG, "Username and token saved!");
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public Integer getUserID(){
        return pref.getInt(KEY_ID, 0);
    }

    public String getUsername(){
        return pref.getString(KEY_USERNAME, "");
    }

    public String getToken(){
        return pref.getString(KEY_TOKEN, "");
    }

    public String getFCMToken(){
        return pref.getString(KEY_FCM_TOKEN, "");
    }
}