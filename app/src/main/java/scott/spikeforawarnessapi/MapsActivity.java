package scott.spikeforawarnessapi;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.location.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v4.app.*;
import android.view.*;

import com.google.android.gms.awareness.*;
import com.google.android.gms.awareness.snapshot.*;
import com.google.android.gms.awareness.snapshot.LocationResult;
import com.google.android.gms.common.*;
import com.google.android.gms.common.api.*;
import com.google.android.gms.location.*;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import java.util.*;

import butterknife.*;

import static android.Manifest.permission.*;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap map;
    public static GoogleApiClient apiClient;
    public ScottsFenceReceiver receiver;
    public PendingIntent myPendingIntent;
    private ResultCallback<? super LocationResult> myLocation = new ResultCallback<LocationResult>() {
        @Override
        public void onResult(@NonNull LocationResult locationResult) {
            Location location = locationResult.getLocation();
            LatLng hereIAm = new LatLng(location.getLatitude(), location.getLongitude());
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(hereIAm, 17));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        initGoogleApiClient();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 123);
            return;
        }
        map.setMyLocationEnabled(true);
        Awareness.SnapshotApi.getLocation(apiClient).setResultCallback(myLocation);
    }

    protected void initGoogleApiClient() {
        apiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Awareness.API)
                .build();
        apiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            startService(new Intent(this, AwarenessIntentService.class));
        } catch (Exception e) {
        }

//        new PushNotifications().sendNotification(this, "Yay", "Connected");

    }
    @Override
    public void onConnectionSuspended(int i) {
//        new PushNotifications().sendNotification(this, "Not", "Not Connected");
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//        new PushNotifications().sendNotification(this, "Failed", "failed Connection");
    }

    @OnClick(R.id.btnClearLogs_m)
public void clearLogs()
    {
        LogIt.clear();
    }

    @OnClick(R.id.btnSendLogs_m)
    public void sendLogs(View view) {
        EmailIntent.builder().setContext(this).setBody(LogIt.getLogs()).setSubject("Awareness Api logs").setEmail("scott.lahay.wave@gmail.com").build().send();
    }


    @OnClick(R.id.btnCurrentFenceState_m)
    public void pushCurrentStateToLogs() {
        Awareness.SnapshotApi.getDetectedActivity(apiClient)
                .setResultCallback(fenceCallback);
    }

    ResultCallback<DetectedActivityResult> fenceCallback = new ResultCallback<DetectedActivityResult>() {
        @Override
        public void onResult(@NonNull DetectedActivityResult detectedActivityResult) {
            if (!detectedActivityResult.getStatus().isSuccess()) {
                LogIt.log("Could not get the current activity.");
                return;
            }
            ActivityRecognitionResult ar = detectedActivityResult.getActivityRecognitionResult();
            List<DetectedActivity> probableActivities = ar.getProbableActivities();
            for (DetectedActivity probableActivity : probableActivities) {
                LogIt.log(probableActivity.toString(), probableActivity.getConfidence());
            }
        }
    };
}
