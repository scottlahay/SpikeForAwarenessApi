package scott.spikeforawarnessapi;

import android.content.*;

import com.google.android.gms.awareness.fence.*;

public class ScottsFenceReceiver extends BroadcastReceiver {
    private MapsActivity mapsActivity;

    public static final String FENCE_RECEIVER_ACTION = "FENCE_RECEIVER";

    public void setMapsActivity(MapsActivity mapsActivity) {
        this.mapsActivity = mapsActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        LogIt.log("receiving intent ");
        FenceState fenceState = FenceState.extract(intent);
        String fenceKey = fenceState.getFenceKey();
        switch (fenceState.getCurrentState()) {
            case FenceState.TRUE:
//                new PushNotifications().sendNotification(mapsActivity, fenceKey + " True", fenceKey + " True");
                LogIt.log(fenceKey + " True");
                break;
            case FenceState.FALSE:
//                new PushNotifications().sendNotification(mapsActivity, fenceKey + " False", fenceKey + " False");
                LogIt.log(fenceKey + " False");
                break;
            case FenceState.UNKNOWN:
//                new PushNotifications().sendNotification(mapsActivity, fenceKey + " Unknown", fenceKey + " Unknown");
                LogIt.log(fenceKey + " Unknown");
                break;
        }
    }
}

