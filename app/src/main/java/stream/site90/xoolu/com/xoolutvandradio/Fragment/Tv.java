package stream.site90.xoolu.com.xoolutvandradio.Fragment;


import android.content.Context;
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

    private View view;
    private View progressView;
    private List<TvDataModel> streamList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TvAdapter adapter;
    private Toolbar toolBar;
    private CustomImageView customImageView;
    private ImageView streamImage;
    FirebaseDatabase database;
    DatabaseReference reference;
    final int[] i = {0};
    private List<String> imageList = new ArrayList<>();

    public Tv() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_tv, container, false);
        progressView = inflater.inflate(R.layout.progress, container, false);
        toolBar = (Toolbar) view.findViewById(R.id.toolbar);
        toolBar.inflateMenu(R.menu.menu_main);
        customImageView = (CustomImageView) view.findViewById(R.id.customImageView);
        streamImage = (ImageView) view.findViewById(R.id.streamImageView);

       Glide.with(getContext()).load(R.drawable.tv).into(streamImage);
       Glide.with(getContext()).load(R.drawable.first_image).into(customImageView);

        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapter = new TvAdapter(getContext());
        recyclerView.setAdapter(adapter);
        getData();
        return progressView;
    }

    private void getData() {
        database = FirebaseDatabase.getInstance();

       reference = database.getReference("data");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                streamList.clear();
                Iterable<DataSnapshot> snaps = dataSnapshot.getChildren();
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
}