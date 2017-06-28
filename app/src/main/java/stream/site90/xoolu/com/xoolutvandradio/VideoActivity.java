package stream.site90.xoolu.com.xoolutvandradio;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


import im.delight.android.webview.AdvancedWebView;

public class VideoActivity extends AppCompatActivity implements OnPreparedListener {

    private VideoView videoView;
    private AdvancedWebView webView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        webView=(AdvancedWebView) findViewById(R.id.webview);
        videoView = (VideoView)findViewById(R.id.video_view);
        String string = getIntent().getStringExtra("URL");
        String type=getIntent().getStringExtra("TYPE");
        if(type.equals("web")){
            setUpWebView(string);
        }else if(type.equals("direct")){
            sendVideoLink(string);
        }else if(type.equals("ebc")){
            download(string);
        }
    }
    private void setUpWebView(String string) {
        videoView.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);
        webView.loadUrl(string);
        webView.setBackgroundColor(Color.BLACK);
    }
    private void sendVideoLink(String uri) {
        videoView.setVideoURI(Uri.parse(uri));
    }
    private void setupVideoView() {
        videoView.setBackgroundColor(Color.BLACK);
        videoView.setOnPreparedListener(this);
    }
    public void onPrepared() {
        videoView.start();
    }
    public void onResume() {
        super.onResume();
        if(videoView==null){
            setupVideoView();
        }
        if(webView!=null){
            webView.onResume();
        }
    }
    public void onPause() {
        super.onPause();
        videoView.release();
        videoView.suspend();
        if(webView!=null){
            webView.onPause();
        }
    }
    public void onStop() {
        super.onStop();
       if(videoView!=null){
           videoView.release();
           videoView.suspend();
       }
       if(webView!=null){
           webView.onPause();
       }
    }
    private void download(String url){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                playStream(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }
    private void playStream(String s) {
        Document doc= Jsoup.parse(s);
        String link= doc.getElementsByTag("source").attr("src");
        Log.i("WHAT",link);
        sendVideoLink(link);
    }


}
