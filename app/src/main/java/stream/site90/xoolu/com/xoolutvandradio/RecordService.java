package stream.site90.xoolu.com.xoolutvandradio;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ArrayBlockingQueue;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import stream.site90.xoolu.com.xoolutvandradio.Fragment.Record;

/**
 * Created by root on 6/30/17.
 */

public class RecordService extends IntentService {
    static boolean stopped = false;
    static float posPercent = 0.0f;
    static long duration;
    static String path;
    static String recordingUrl;
    static ArrayBlockingQueue<String> loadedChunks = new ArrayBlockingQueue(100);
    static OkHttpClient client = (new OkHttpClient()).newBuilder().followRedirects(true).build();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public RecordService(String name) {
        super(name);
    }
    public RecordService(){
        super("rec");
    }
    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        recordingUrl = workIntent.getDataString();
        posPercent = workIntent.getFloatExtra("%", 0);
        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "XooluRecords");
        if (!f.exists()) {
            f.mkdirs();
        }
        path = f.getAbsolutePath();
        while(!stopped) {
            extractChunks(recordingUrl);
            try {
                Thread.sleep(1500);
            }catch (InterruptedException s){

            }

            Log.d("Url", posPercent + ":" + 1.0f/loadedChunks.size());
        }

    }

    private  void downloadChunks(boolean inSync){
        //if(!inSync) return;
        String next = loadedChunks.poll();
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(next);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.d("ERROR", "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage());
                return;
            }

            input = connection.getInputStream();
            File f2 = new File(path,"test.ts");

            output = new FileOutputStream(f2, true);

            byte data[] = new byte[4096];
            int count;
            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }
        } catch (Exception e) {
            Log.d("ConnectError", e.toString());
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }


    }

    private  void extractChunks(String url){
        //switch but generic is better
        //extract chunks add to queue
        //download first chunk pop
        //every second this service is called as long as video is playing
        //when record is stopped service must skip download
        String absoluteUrl = url.substring(0, url.lastIndexOf("/")+1);
        String[] m3u8Links = getChunkUrls(url).split("#EXT-X-STREAM-INF.*(\\n)");

        for (int i = 1; i < m3u8Links.length; i++) {
            Log.d("Lev1", (m3u8Links[i].startsWith("http") ? "": absoluteUrl)+m3u8Links[i]);
            String rr = getChunkUrls((m3u8Links[i].startsWith("http") ? "": absoluteUrl)+m3u8Links[i]);
            String[] mediaLinks = rr.split("#EXTINF:.*(\\n)");
            for(int l=1; l<mediaLinks.length; l++){
                String mLink = (mediaLinks[i].startsWith("http")
                        ? "": absoluteUrl) +mediaLinks[l];
                Log.d("Lev2", mLink);
                try {
                    if(!loadedChunks.contains(mLink) && loadedChunks.size()<100){
                        Log.d("Added new", "YES");
                        downloadChunks(posPercent > (1.0f/loadedChunks.size()));
                        loadedChunks.put(mLink);
                    }
                }catch (InterruptedException s){Log.d("Lev2Error", s.toString());}
            }
        }


    }

    private String getChunkUrls(String url){
        String ret = "";
        try {
            Request request = (new Request.Builder()).url(url).get().build();
            Response response = client.newCall(request).execute();
            ResponseBody b = response.body();
            ret = b.string();
        }catch (IOException e){Log.e("ChunkSeedError", e.toString());}

        return ret;
    }


}