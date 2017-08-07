package bil495.not2do.fcm;

import android.content.Context;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import bil495.not2do.helper.MyNotificationManager;

/**
 * Created by Görkem Mülayim on 8/7/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

        String body = remoteMessage.getNotification().getBody();
        MyNotificationManager notifier = new MyNotificationManager();
        notifier.notify(getSystemService(Context.NOTIFICATION_SERVICE), getBaseContext(), body);
    }
}
