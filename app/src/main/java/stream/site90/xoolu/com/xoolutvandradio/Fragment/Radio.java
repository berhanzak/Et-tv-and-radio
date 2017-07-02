package stream.site90.xoolu.com.xoolutvandradio.Fragment;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import co.mobiwise.library.RadioListener;
import co.mobiwise.library.RadioManager;
import stream.site90.xoolu.com.xoolutvandradio.Adapters.RadioAdapter;
import stream.site90.xoolu.com.xoolutvandradio.Adapters.TvAdapter;
import stream.site90.xoolu.com.xoolutvandradio.Model.RadioDataModel;
import stream.site90.xoolu.com.xoolutvandradio.Model.TvDataModel;
import stream.site90.xoolu.com.xoolutvandradio.R;
import stream.site90.xoolu.com.xoolutvandradio.View.CustomImageView;


public class Radio extends Fragment implements RadioListener {



    private View view;
    private View progressView;
    private List<RadioDataModel> radioDataModelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RadioAdapter adapter;
    private Toolbar toolBar;
    private CustomImageView customImageView;
    private ImageView streamImage;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    final int[] i = {0};
    private List<String> imageList = new ArrayList<>();
    RadioManager mRadioManager;
    private LinearLayout radioPlayerView;


    //radio player view
    ImageView srcImageView;
    TextView srcTextView;
    ImageView playerControl;


    private int position=0;

    public Radio() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_radio, container, false);
        progressView = inflater.inflate(R.layout.progress, container, false);
        toolBar = (Toolbar) view.findViewById(R.id.toolbar);
        toolBar.inflateMenu(R.menu.menu_main);
        radioPlayerView=(LinearLayout) view.findViewById(R.id.radioPlayerView);



        customImageView = (CustomImageView) view.findViewById(R.id.customImageView);
        streamImage = (ImageView) view.findViewById(R.id.streamImageView);
        Glide.with(getContext()).load(R.drawable.radio).into(streamImage);
        Glide.with(getContext()).load(R.drawable.radio_back).into(customImageView);


        //refrence to the radio player view
        srcImageView=(ImageView) radioPlayerView.findViewById(R.id.srcImageView);
        srcTextView=(TextView) radioPlayerView.findViewById(R.id.channelName);
        playerControl=(ImageView) radioPlayerView.findViewById(R.id.controlImageView);

        //listner for the radio view to play or pasue the radio stream
        playerControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mRadioManager.isPlaying()){

                    mRadioManager.stopRadio();
                    playerControl.setImageResource(R.drawable.ic_pause_circle);

                }else {

                    mRadioManager.startRadio(radioDataModelList.get(position).getLink());
                    playerControl.setImageResource(R.drawable.ic_play_circle);

                }

            }
        });



        //radio manger instance
         mRadioManager = RadioManager.with(getContext());


        //register listener for radio stream changes
        mRadioManager.registerListener(this);

        //enable notification for the radio manager
       mRadioManager.enableNotification(true);

        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        int screenSize=getContext().getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        int screenOrientation=getContext().getResources().getConfiguration().orientation;

        if(screenSize==Configuration.SCREENLAYOUT_SIZE_LARGE || screenSize==Configuration.SCREENLAYOUT_SIZE_XLARGE){

            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        }else if(screenSize==Configuration.SCREENLAYOUT_SIZE_SMALL || screenSize==Configuration.SCREENLAYOUT_SIZE_NORMAL){

            if(screenOrientation==Configuration.ORIENTATION_LANDSCAPE){

                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

            }else{

                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

            }

        }else{
            if(screenOrientation==Configuration.ORIENTATION_LANDSCAPE){

                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

            }else{

                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

            }

        }
        getData();
        return progressView;
    }



    private void getData() {
        database = FirebaseDatabase.getInstance();

        reference = database.getReference("radio");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                radioDataModelList.clear();
                Iterable<DataSnapshot> snaps = dataSnapshot.getChildren();
                for (DataSnapshot snap : snaps) {

                    radioDataModelList.add(snap.getValue(RadioDataModel.class));

                }
                updatedLayout();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    private void updatedLayout() {
        adapter.setRadioDataModelList(radioDataModelList);
        adapter.notifyDataSetChanged();
        ViewGroup viewGroup = (ViewGroup) getView();
        if (viewGroup != null) {
            viewGroup.removeAllViews();
            viewGroup.addView(view);
        }

        if(isOnline())
        getImageLinks();
    }
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)
                getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    private void startAnimatingImage() {
        try {
            Handler handler1 = new Handler();
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {

                    try {
                        if (i[0] >= imageList.size()) {
                            i[0] = 0;
                        }
                        Glide.with(getContext()).load(imageList.get(i[0])).into(customImageView);

                        i[0]++;
                        startAnimatingImage();
                    } catch (Exception e) {

                    }
                }
            }, 8000);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

    }
    private void getImageLinks() {


        DatabaseReference image = FirebaseDatabase.getInstance().getReference("image");

        ValueEventListener listener=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> dataSnapshotList = dataSnapshot.getChildren();
                for (DataSnapshot data : dataSnapshotList) {

                    imageList.add(data.getValue().toString());

                }

                startAnimatingImage();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        image.addValueEventListener(listener);


    }

    @Override
    public void onStart() {
        super.onStart();
       mRadioManager.connect();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       mRadioManager.disconnect();
    }

    public void setPosition(int posation){
        this.position=posation;
    }

    public void setRadioPlayerViewVisible(){

        if(radioPlayerView.getVisibility()==View.GONE)
        radioPlayerView.setVisibility(View.VISIBLE);
    }

    public void updateView(){
        Glide.with(this).load(radioDataModelList.get(position).getImage()).into(srcImageView);
        srcTextView.setText(radioDataModelList.get(position).getName());
    }

    public void play(){

        try {
            mRadioManager.startRadio(radioDataModelList.get(position).getLink());

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onRadioConnected() {

    }

    @Override
    public void onRadioStarted() {

    }

    @Override
    public void onRadioStopped() {

    }

    @Override
    public void onMetaDataReceived(String s, String s1) {

    }
}