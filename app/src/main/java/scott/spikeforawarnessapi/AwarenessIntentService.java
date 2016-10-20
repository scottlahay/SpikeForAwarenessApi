package scott.spikeforawarnessapi;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.annotation.*;

import com.google.android.gms.awareness.*;
import com.google.android.gms.awareness.fence.*;
import com.google.android.gms.common.api.*;

import static scott.spikeforawarnessapi.MapsActivity.*;
import static scott.spikeforawarnessapi.ScottsFenceReceiver.*;

public class AwarenessIntentService extends Service {

    private static final String ACTION_FOO = "scott.spikeforawarnessapi.action.FOO";
    public PendingIntent myPendingIntent;
    public ScottsFenceReceiver receiver;

    @Override
    public void onCreate() {
        LogIt.log("onStartCommand");
        receiver = new ScottsFenceReceiver();
        Intent intent2 = new Intent(FENCE_RECEIVER_ACTION);
        myPendingIntent = PendingIntent.getBroadcast(this, 0, intent2, 0);
        registerReceiver(receiver, new IntentFilter(FENCE_RECEIVER_ACTION));
        removeSomeFences("Walk");
        removeSomeFences("Still");
        addSomeFences(DetectedActivityFence.WALKING, "Walk");
        addSomeFences(DetectedActivityFence.STILL, "Still");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    public void addSomeFences(int detectedActivity, String key) {

        addFence("b_" + key, DetectedActivityFence.starting(detectedActivity), myPendingIntent, resultCallback);
        addFence("a_" + key, DetectedActivityFence.stopping(detectedActivity), myPendingIntent, resultCallback);
        addFence("d_" + key, DetectedActivityFence.during(detectedActivity), myPendingIntent, resultCallback);
    }

    public void removeSomeFences(String key) {
        removeFence("b_" + key, resultCallback);
        removeFence("a_" + key, resultCallback);
        removeFence("d_" + key, resultCallback);
    }

    private void addFence(String key, AwarenessFence fence, PendingIntent myPendingIntent, ResultCallback<? super Status> resultCallback) {
        if (apiClient.isConnected()) {
            FenceUpdateRequest addFence = new FenceUpdateRequest.Builder().addFence(key, fence, myPendingIntent).build();
            Awareness.FenceApi.updateFences(apiClient, addFence).setResultCallback(resultCallback);
        }
    }

    private void removeFence(String key, ResultCallback<? super Status> resultCallback) {
        if (apiClient.isConnected()) {
            FenceUpdateRequest removeFence = new FenceUpdateRequest.Builder().removeFence(key).build();
            Awareness.FenceApi.updateFences(apiClient, removeFence).setResultCallback(resultCallback);
        }
    }

    ResultCallback<Status> resultCallback = new ResultCallback<Status>() {
        @Override
        public void onResult(@NonNull Status status) {
            if (status.isSuccess()) {
//                LogIt.log("Success Registering");
            } else {
                LogIt.log("Failure Registering");
            }
        }
    };

    @Override
    public void onDestroy() {
        LogIt.log("Stopping Service");
        removeFence("Walk", resultCallback);
        removeFence("Still", resultCallback);
        unregisterReceiver(receiver);
        LogIt.log("Finished Stopping Service");
        super.onDestroy();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
