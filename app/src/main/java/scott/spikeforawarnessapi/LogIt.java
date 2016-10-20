package scott.spikeforawarnessapi;

import android.util.*;

import java.io.*;
import java.text.*;
import java.util.*;

public class LogIt {

    public static SimpleDateFormat FORMAT_TIMESTAMP = new SimpleDateFormat("EEE, MMM d, hh:mm:ss", Locale.getDefault());

    static List<String> logs = new ArrayList<>();
    public static void log(Object... logs) {
        String log = FORMAT_TIMESTAMP.format(new Date()) + "," + createLogMessage(logs);
        log(log);
        Log.w("scottlog", log);
    }

    public static String getLogs() {
//        String temp = "";
//        for (String log : logs) {
//            temp += log + System.lineSeparator();
//        }
        return readLogFile();
    }

    public static String createLogMessage(Object[] objs) {
        String moreText = "";
        for (Object object : objs) {
            moreText += object == null ? "NULL" : object.toString();
            moreText += " ";
        }
        return moreText;
    }
    public static void clear() {
        logs.clear();
        clearLogs();
    }

    private static void log(String message) {
        String log = FORMAT_TIMESTAMP.format(new Date()) + "," + message;
        File file = getFile();
        try {
            FileOutputStream f = new FileOutputStream(file, true);
            PrintWriter pw = new PrintWriter(f);
            pw.println(log);
            pw.flush();
            pw.close();
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readLogFile() {
        File file = getFile();
        String logs = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String s = reader.readLine();
            while (s != null) {
                logs += s + System.lineSeparator();
                s = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return logs;
    }

    private static File getFile() {

        //        Scott.log("EXISTS1,", fileDirectory.exists(), fileDirectory);
        File dir = new File(MapsApplication.localLogFile + "/LogData");
//        Scott.log("EXISTS2,", dir.exists(), dir);

        if (!dir.exists()) { dir.mkdir();}
        return new File(dir, "awarness.logs");
    }

    public static void clearLogs() {
        try {
            getFile().delete();
            getFile().createNewFile();
        } catch (IOException e) {
        }
    }

}
