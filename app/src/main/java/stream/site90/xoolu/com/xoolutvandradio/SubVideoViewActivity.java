package stream.site90.xoolu.com.xoolutvandradio;

import android.app.IntentService;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.IntRange;
import android.support.v7.app.AlertDialog;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.devbrackets.android.exomedia.ExoMedia;
import com.devbrackets.android.exomedia.listener.OnBufferUpdateListener;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.google.android.exoplayer2.source.TrackGroupArray;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Map;

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
    private TvDataModel tvDataModel;
    private ImageView expandImageView;
    private int position;

    private IntentService service;
    String streamLink;


    //flag that show the current stat of the recording
    private boolean isRecoding=false;


    //image view for recording
    private ImageView recordImageView;
    private TextView timeRecordTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_video_view);

        position=0;

       overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );


        //refrencing the record text and image view
        recordImageView=(ImageView) findViewById(R.id.recordImageView);
        timeRecordTextView=(TextView) findViewById(R.id.timeRecordView);


        recordImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isRecoding){

                    stopRecord();

                }else {
                    startRecording();

                }

            }
        });






        //getting stream data from the intent object
        tvDataModel=(TvDataModel) getIntent().getSerializableExtra(KEY);

        //referencing view
        webView=(AdvancedWebView) findViewById(R.id.webview);
        videoView = (VideoView)findViewById(R.id.video_view);
        channelName=(TextView) findViewById(R.id.channelName);
        recyclerView=(RecyclerView) findViewById(R.id.rv);
        expandImageView=(ImageView) findViewById(R.id.expandImageView);

        //expand the view to the full screen
        expandImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(SubVideoViewActivity.this, VideoActivity.class);
                intent.putExtra(SubVideoViewActivity.KEY,TvAdapter.getTvDataModelList().get(position));
                startActivity(intent);

            }
        });

        //set the layout manager of the recylve viw
        int screenSize=getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        int screenOrientation=getResources().getConfiguration().orientation;



        if(screenSize==Configuration.SCREENLAYOUT_SIZE_LARGE || screenSize==Configuration.SCREENLAYOUT_SIZE_XLARGE){

            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        }else if(screenSize==Configuration.SCREENLAYOUT_SIZE_SMALL || screenSize==Configuration.SCREENLAYOUT_SIZE_NORMAL){

            if(screenOrientation==Configuration.ORIENTATION_LANDSCAPE){

                recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

            }else{

                recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

            }

        }else{
            if(screenOrientation==Configuration.ORIENTATION_LANDSCAPE){

                recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

            }else{

                recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

            }

        }
        //create the adapter
        adapter=new SubVideoStreamAdapter();

        //pass data to the adapter
        recyclerView.setAdapter(adapter);

        //get the streaming link
        String link =tvDataModel.getLink();

        //get the tv data model
        String type=tvDataModel.getType();

        videoView.setOnBufferUpdateListener(new OnBufferUpdateListener() {
            @Override
            public void onBufferingUpdate(@IntRange(from = 0L, to = 100L) int percent) {
                RecordService.posPercent = ((float) videoView.getCurrentPosition())/(float) videoView.getDuration();

            }
        });

        //set the channel name
        channelName.setText(tvDataModel.getName());



        webView.setListener(this,this);

        //checking the implementation of a given stream weather if it is implemented on web or on video view
        checkType(type,link);



    }

    private void startRecording() {

        AlertDialog.Builder builder=new AlertDialog.Builder(this);

        builder.setTitle("Start Recording ");
        builder.setIcon(R.drawable.ic_videocam_red);
        builder.setMessage("This will record currently streaming video and save it");

        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                isRecoding=false;
            }
        });

        builder.setNegativeButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(timeRecordTextView.getVisibility()==View.INVISIBLE){

                    timeRecordTextView.setVisibility(View.VISIBLE);
                }

                continueRecording();
        }
        });

        builder.create().show();


    }

    private void continueRecording() {
         recordImageView.setImageResource(R.drawable.ic_videocam_red);
         timeRecordTextView.setText("Recording...");
         timeRecordTextView.setTextColor(Color.RED);
         startRecord();
    }


    public void startRecord(){
        isRecoding=true;
        Toast.makeText(this,"Recording started",Toast.LENGTH_LONG).show();

        Intent i = new Intent(SubVideoViewActivity.this, RecordService.class);
        i.putExtra("%",((float) videoView.getCurrentPosition())/(float) videoView.getDuration());
        i.setData(Uri.parse(streamLink));
        startService(i);

    }

    public void stopRecord(){
        isRecoding=false;
        Toast.makeText(this,"Recording stopped",Toast.LENGTH_LONG).show();
        recordImageView.setImageResource(R.drawable.ic_videocam_black_24dp);

        if(timeRecordTextView.getVisibility()==View.VISIBLE)
           timeRecordTextView.setVisibility(View.INVISIBLE);

        RecordService.stopped = true;

    }


    public void saveRecord(){
        Toast.makeText(this,"Recording saved",Toast.LENGTH_LONG).show();
    }

    private void checkType(String type,String link){


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



                //listener for tv items to start playing stream
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        channelName.setText(TvAdapter.getTvDataModelList().get(getAdapterPosition()).getName());

                        checkType(TvAdapter.getTvDataModelList().get(getAdapterPosition()).getType(),
                                TvAdapter.getTvDataModelList().get(getAdapterPosition()).getLink());

                        position =getAdapterPosition();


                    }
                });
            }
        }


    }



}
