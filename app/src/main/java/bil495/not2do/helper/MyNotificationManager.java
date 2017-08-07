package bil495.not2do.helper;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import bil495.not2do.R;

/**
 * Created by burak on 8/7/2017.
 */

public class MyNotificationManager {
    public static void notify(Object systemService, Context context, String body){
        String title = "Not2Do";

        NotificationManager notif=(NotificationManager)systemService;
        Notification notify=new Notification.Builder
                (context).setContentTitle(title).setContentText(body)
                .setSmallIcon(R.drawable.ic_launcher).build();

        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.notify(0, notify);
    }
}
