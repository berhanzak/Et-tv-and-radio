package stream.site90.xoolu.com.xoolutvandradio.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import stream.site90.xoolu.com.xoolutvandradio.Model.RecordModel;
import stream.site90.xoolu.com.xoolutvandradio.R;

public class Record extends Fragment{

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private RecordAdapter adapter;
    private View progressView;
    private View view;

    public Record(){


    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view= inflater.inflate(R.layout.fragment_record,container,false);
        progressView=inflater.inflate(R.layout.progress,container,false);

        toolbar=(Toolbar) view.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setTitle(getString(R.string.record));

        recyclerView=(RecyclerView) view.findViewById(R.id.recoredRecyleView);
        adapter=new RecordAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

         new Handler().postDelayed(new Runnable() {
             @Override
             public void run() {

                 getRecordData();
             }
         },1000);



        return progressView;
    }

    private void getRecordData() {

        //read data from the memory card


        updateView();
    }

    private void updateView(){

        ViewGroup viewGroup=(ViewGroup) getView();

        if(viewGroup!=null){

            viewGroup.removeAllViews();
            viewGroup.addView(view);
        }
    }


    public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.MyRecordAdaper>{

        private Context context;
        private List<RecordModel> recordModelList=new ArrayList<>();


        public RecordAdapter(Context context){

              this.context=context;
        }

        public void setRecordModelList(List<RecordModel> recordModelList) {
            this.recordModelList = recordModelList;
        }

        @Override
        public RecordAdapter.MyRecordAdaper onCreateViewHolder(ViewGroup parent, int viewType) {

            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.record_list_item,parent,false);

            return new RecordAdapter.MyRecordAdaper(view);
        }

        @Override
        public void onBindViewHolder(RecordAdapter.MyRecordAdaper holder, int position) {

               if(recordModelList.get(position).equals("video")){
                   holder.typeImageView.setImageResource(R.drawable.ic_videocam_red);
                }else{
                   holder.typeImageView.setImageResource(R.drawable.ic_voice);
               }

               holder.recodNameTextView.setText(recordModelList.get(position).getName());
               holder.timeTextView.setText(recordModelList.get(position).getType());



        }

        @Override
        public int getItemCount() {
            return recordModelList.size();
        }

        public class MyRecordAdaper extends RecyclerView.ViewHolder {

            private ImageView typeImageView;
            private TextView recodNameTextView;
            private TextView timeTextView;

            public MyRecordAdaper(View itemView) {
                super(itemView);

                typeImageView=(ImageView) itemView.findViewById(R.id.typeImageView);
                recodNameTextView=(TextView) itemView.findViewById(R.id.recordNameTextView);
                timeTextView=(TextView) itemView.findViewById(R.id.timeTexTView);
            }
        }
    }





}