package stream.site90.xoolu.com.xoolutvandradio.Adapters;


import android.content.ContentValues;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import stream.site90.xoolu.com.xoolutvandradio.Model.TvDataModel;
import stream.site90.xoolu.com.xoolutvandradio.R;

public class TvAdapter extends RecyclerView.Adapter<TvAdapter.TvViewHolder>{


    private Context context;

    //list of tv object
    private List<TvDataModel> tvDataModelList=new ArrayList<>();

    //create tv adapter
    public TvAdapter(Context context){

        this.context=context;
    }

    //set the tv list
    public void setTvDataModelList(List<TvDataModel> tvDataModelList) {
        this.tvDataModelList = tvDataModelList;
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
        }
    }
}