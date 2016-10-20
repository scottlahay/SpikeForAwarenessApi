package scott.spikeforawarnessapi;

import android.app.*;
import android.content.*;
import android.support.v4.app.*;

import com.google.android.gms.gcm.*;

public class PushNotifications extends GcmListenerService {

    public void sendNotification(Context ctx,  String subject, String body) {
        final Intent emptyIntent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0, emptyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx)
                .setSmallIcon(R.drawable.push_notification)
                .setContentTitle(subject)
                .setContentText(body)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(""));

        Notification notification = builder.build();
        getNotificationService(ctx).notify(body.hashCode(), notification);
    }

    NotificationManager getNotificationService(Context ctx) {return (NotificationManager) ctx.getSystemService(NOTIFICATION_SERVICE);}
}
