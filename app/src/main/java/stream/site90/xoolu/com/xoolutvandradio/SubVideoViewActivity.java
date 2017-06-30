package stream.site90.xoolu.com.xoolutvandradio;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import stream.site90.xoolu.com.xoolutvandradio.Model.TvDataModel;

public class SubVideoViewActivity extends AppCompatActivity implements OnPreparedListener ,AdvancedWebView.Listener{

    public static final String KEY="key";

    private VideoView videoView;
    private AdvancedWebView webView;

    TvDataModel tvDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_video_view);

        //getting stream data from the intent object
        tvDataModel=(TvDataModel) getIntent().getSerializableExtra(KEY);

        //referencing view
        webView=(AdvancedWebView) findViewById(R.id.webview);
        videoView = (VideoView)findViewById(R.id.video_view);

        //get the streaming link
        String link =tvDataModel.getLink();

        //get the tv data model
        String type=tvDataModel.getType();

        webView.setListener(this,this);

        //checking the implementation of a given stream weather if it is implemented on web or on video view
        if(type.equals("web")){
            setUpWebView(link);
        }else if(type.equals("direct")){
            sendVideoLink(link);
        }else if(type.equals("ebc")){
            download(link);
        }

    }

    private void setUpWebView(String string) {
        webView.setBackgroundColor(Color.BLACK);
        videoView.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);
        webView.loadUrl(string);

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


    @Override
    public void onPageStarted(String url, Bitmap favicon) {

        Log.i("TAG","PAGE start");


    }

    @Override
    public void onPageFinished(String url) {

        Log.i("TAG","PAGE finish");


    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {

        Log.i("TAG","PAGE error");

    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {


        Log.i("TAG","PAGE dowload");
    }

    @Override
    public void onExternalPageRequest(String url) {

        Log.i("TAG","external page download");
    }
}
