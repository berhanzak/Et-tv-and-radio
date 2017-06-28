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

import stream.site90.xoolu.com.xoolutvandradio.Model.RadioDataModel;
import stream.site90.xoolu.com.xoolutvandradio.R;

public class RadioAdapter extends RecyclerView.Adapter<RadioAdapter.RadioViewHolder>{


    private Context context;

    //list of tv object
    private List<RadioDataModel> radioDataModelList=new ArrayList<>();

    //create tv adapter
    public RadioAdapter(Context context){

        this.context=context;
    }

    //set the tv list
    public void setRadioDataModelList(List<RadioDataModel> radioDataModelList) {
        this.radioDataModelList = radioDataModelList;
    }

    @Override
    public RadioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.radio_item,parent,false);
        return new RadioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RadioViewHolder holder, int position) {

        Glide.with(context).load(radioDataModelList.get(position).getImage()).into(holder.srcImageView);
        holder.srcTextView.setText(radioDataModelList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return radioDataModelList.size();
    }


    //tv view holder for tv object
    public class RadioViewHolder extends RecyclerView.ViewHolder {

        ImageView srcImageView;
        TextView srcTextView;

        public RadioViewHolder(View itemView) {
            super(itemView);

            srcImageView=(ImageView) itemView.findViewById(R.id.srcImageView);
            srcTextView=(TextView) itemView.findViewById(R.id.srcTextView);
        }
    }
}
