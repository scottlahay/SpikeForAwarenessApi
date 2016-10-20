package scott.spikeforawarnessapi;

import android.app.*;

import java.io.*;

public class MapsApplication extends Application {

    public static File localLogFile;

    @Override
    public void onCreate() {
        super.onCreate();
        localLogFile = getApplicationContext().getFilesDir();
    }

}
