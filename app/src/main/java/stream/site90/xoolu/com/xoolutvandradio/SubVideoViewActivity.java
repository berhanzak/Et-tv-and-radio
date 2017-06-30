package stream.site90.xoolu.com.xoolutvandradio;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.VideoView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import im.delight.android.webview.AdvancedWebView;
import stream.site90.xoolu.com.xoolutvandradio.Adapters.TvAdapter;
import stream.site90.xoolu.com.xoolutvandradio.Model.TvDataModel;

public class SubVideoViewActivity extends AppCompatActivity implements OnPreparedListener ,AdvancedWebView.Listener{

    public static final String KEY="key";

    private VideoView videoView;
    private AdvancedWebView webView;
    private TextView channelName;
    private RecyclerView recyclerView;
    private SubVideoStreamAdapter adapter;
    TvDataModel tvDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_video_view);

       overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );

        //getting stream data from the intent object
        tvDataModel=(TvDataModel) getIntent().getSerializableExtra(KEY);

        //referencing view
        webView=(AdvancedWebView) findViewById(R.id.webview);
        videoView = (VideoView)findViewById(R.id.video_view);
        channelName=(TextView) findViewById(R.id.channelName);
        recyclerView=(RecyclerView) findViewById(R.id.rv);

        //set the layout manager of the recylve viw
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));

        //create the adapter
        adapter=new SubVideoStreamAdapter();

        //pass data to the adapter
        recyclerView.setAdapter(adapter);

        //get the streaming link
        String link =tvDataModel.getLink();

        //set the channel name
        channelName.setText(tvDataModel.getName());

        //get the tv data model
        String type=tvDataModel.getType();

        webView.setListener(this,this);

        //checking the implementation of a given stream weather if it is implemented on web or on video view
        checkType(type,link);

    }

    private void checkType(String type,String link){


        if(type.equals("web")){
            setUpWebView(link);
        }else if(type.equals("direct")){
            videoView.animate();
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

        //video view must be visible
        if(videoView.getVisibility()==View.VISIBLE){
            videoView.setVideoURI(Uri.parse(uri));
        }else{
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoURI(Uri.parse(uri));
        }
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



    private class SubVideoStreamAdapter extends RecyclerView.Adapter<SubVideoStreamAdapter.MyVideoStreamAdapter>{


        public SubVideoStreamAdapter() {
        }

        @Override
        public SubVideoStreamAdapter.MyVideoStreamAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.tv_item,parent,false);
            return new MyVideoStreamAdapter(view);
        }

        @Override
        public void onBindViewHolder(SubVideoStreamAdapter.MyVideoStreamAdapter holder, int position) {

            Glide.with(SubVideoViewActivity.this).load(TvAdapter.getTvDataModelList().get(position).getImage()).into(holder.srcImageView);
            holder.srcTextView.setText(TvAdapter.getTvDataModelList().get(position).getName());

        }

        @Override
        public int getItemCount() {
            return TvAdapter.getTvDataModelList().size();
        }

        public class MyVideoStreamAdapter extends RecyclerView.ViewHolder {

            ImageView srcImageView;
            TextView srcTextView;

            public MyVideoStreamAdapter(View itemView) {
                super(itemView);

                //reference view
                srcImageView=(ImageView) itemView.findViewById(R.id.srcImageView);
                srcTextView=(TextView) itemView.findViewById(R.id.srcTextView);


                //set the channel name
                channelName.setText(TvAdapter.getTvDataModelList().get(getAdapterPosition()).getName());

                //listener for tv items to start playing stream
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkType(TvAdapter.getTvDataModelList().get(getAdapterPosition()).getType(),
                                TvAdapter.getTvDataModelList().get(getAdapterPosition()).getLink());


                    }
                });
            }
        }


    }



}
