package stream.site90.xoolu.com.xoolutvandradio.Adapters;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import stream.site90.xoolu.com.xoolutvandradio.MainActivity;
import stream.site90.xoolu.com.xoolutvandradio.Model.TvDataModel;
import stream.site90.xoolu.com.xoolutvandradio.R;
import stream.site90.xoolu.com.xoolutvandradio.SubVideoViewActivity;

public class TvAdapter extends RecyclerView.Adapter<TvAdapter.TvViewHolder>{


    private Context context;

    //list of tv object
    private static List<TvDataModel> tvDataModelList=new ArrayList<>();

    //create tv adapter
    public TvAdapter(Context context){

        this.context=context;
    }

    //set the tv list
    public void setTvDataModelList(List<TvDataModel> tvDataMode) {
        tvDataModelList = tvDataMode;
    }

    @Override
    public TvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.tv_item,parent,false);
        return new TvViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TvViewHolder holder, int position) {

        Glide.with(context).load(tvDataModelList.get(position).getImage()).into(holder.srcImageView);
        holder.srcTextView.setText(tvDataModelList.get(position).getName());


    }

    @Override
    public int getItemCount() {
        return tvDataModelList.size();
    }


    //tv view holder for tv object
    public class TvViewHolder extends RecyclerView.ViewHolder {

        ImageView srcImageView;
        TextView srcTextView;

        public TvViewHolder(View itemView) {
            super(itemView);

            srcImageView=(ImageView) itemView.findViewById(R.id.srcImageView);
            srcTextView=(TextView) itemView.findViewById(R.id.srcTextView);

            //listener for tv items to start playing stream
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(context, SubVideoViewActivity.class);
                    Log.i("TAG",tvDataModelList.get(getAdapterPosition()).getLink());
                    intent.putExtra(SubVideoViewActivity.KEY,tvDataModelList.get(getAdapterPosition()));
                    context.startActivity(intent);

                }
            });
        }
    }


    public static List<TvDataModel> getTvDataModelList(){

        return tvDataModelList;

    }
}
