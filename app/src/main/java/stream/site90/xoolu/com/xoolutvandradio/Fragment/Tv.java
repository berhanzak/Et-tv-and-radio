package stream.site90.xoolu.com.xoolutvandradio.Fragment;


import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import stream.site90.xoolu.com.xoolutvandradio.Adapters.TvAdapter;
import stream.site90.xoolu.com.xoolutvandradio.Model.TvDataModel;
import stream.site90.xoolu.com.xoolutvandradio.R;
import stream.site90.xoolu.com.xoolutvandradio.View.CustomImageView;


public class Tv extends Fragment {

    //main view
    private View view;

    //progress view displayed after the main view is inflated
    private View progressView;

    //list of tv objects for the recycle view
    private List<TvDataModel> streamList = new ArrayList<>();

    //recycle view to list tv objects
    private RecyclerView recyclerView;

    //adapter for the recycle view to map tv objects
    private TvAdapter adapter;

    private Toolbar toolBar;

    //custom image that takes 2/3 of the screen height and have zooming animation
    private CustomImageView customImageView;

    //tv icon for tv fragment
    private ImageView streamImage;

    //instance for the firebase databse
    private FirebaseDatabase database;

    //firebase reference fot the tv tree structure inside the firebase real time database
    private DatabaseReference reference;

    //final int array for the starting point of the image sliding animation
    final int[] i = {0};

    //list of image links for the animation
    private List<String> imageList = new ArrayList<>();

    public Tv() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //inflating view
        view = inflater.inflate(R.layout.fragment_tv, container, false);
        progressView = inflater.inflate(R.layout.progress, container, false);

        //referencing view from xml
        toolBar = (Toolbar) view.findViewById(R.id.toolbar);
        customImageView = (CustomImageView) view.findViewById(R.id.customImageView);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        streamImage = (ImageView) view.findViewById(R.id.streamImageView);

        //creating object
        adapter = new TvAdapter(getContext());

        //inflating the toolbar with menu
        toolBar.inflateMenu(R.menu.menu_main);

        //loading image in to image view using glide image loading library
         Glide.with(getContext()).load(R.drawable.tv).into(streamImage);
         Glide.with(getContext()).load(R.drawable.first_image).into(customImageView);


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

        //setting layout manger for the recycle view

        //giving an adapter a recycle view an adapter
        recyclerView.setAdapter(adapter);

        //get tv streams data from the firebase real time databse
        getData();
        return progressView;
    }


    //method for getting tv data from the firebase real time databse
    private void getData() {

        //get reference to firebase real time database
        database = FirebaseDatabase.getInstance();

       //get the data node from the the tree that represent the tv stream data
       reference = database.getReference("data");

        //add listener to retrieve data
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                streamList.clear();

                //get stream data
                Iterable<DataSnapshot> snaps = dataSnapshot.getChildren();

                //add the tv data to the stream list
                for (DataSnapshot snap : snaps) {

                        streamList.add(snap.getValue(TvDataModel.class));

                }
                updatedLayout();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    //for updating the data from the adapter and add the main view back to the view hierarchy after removing the progress view
    private void updatedLayout() {
        adapter.setTvDataModelList(streamList);
        adapter.notifyDataSetChanged();
        ViewGroup viewGroup = (ViewGroup) getView();
        if (viewGroup != null) {
            viewGroup.removeAllViews();
            viewGroup.addView(view);
        }

        if(isOnline())
        getImageLinks();

    }


    //checking internet connectivity
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)
                getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    //method for animating the sliding image
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
                    } catch (Exception ignored) {
                    }
                }
            }, 8000);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }



    //get the image links from firebase database and store them in the image list
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
}