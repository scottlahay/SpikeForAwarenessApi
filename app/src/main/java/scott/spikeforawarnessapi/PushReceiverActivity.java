package scott.spikeforawarnessapi;

import android.app.*;
import android.os.*;
import android.support.v7.app.*;

public class PushReceiverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getNotificationService().cancelAll();
        finish();
    }
    NotificationManager getNotificationService() {return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);}
}
