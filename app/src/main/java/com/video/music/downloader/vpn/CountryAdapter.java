package com.video.music.downloader.vpn;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.anchorfree.partner.api.data.Country;
import com.bumptech.glide.Glide;
import com.video.music.downloader.R;

import java.util.ArrayList;

public class CountryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    LayoutInflater inflater;
    Context context;
    ArrayList<Country_Code> list;
    selecTionCountry selectionCountry;
    int type;


    public CountryAdapter(Context context, ArrayList<Country_Code> list, int type) {
        this.context = context;
        this.list = list;
        this.type = type;
        inflater = LayoutInflater.from(context);


    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        view = inflater.inflate(R.layout.layout_country_list, parent, false);
        return new Holder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder mainHolder, @SuppressLint("RecyclerView") int position) {
        Holder holder = (Holder) mainHolder;
        if (type == 2) {
            holder.llLock.setVisibility(View.GONE);
            Log.e("Flage","if"+list.get(position).getPathFlag());
            Glide.with(context).load(list.get(position).getPathFlag()).into(holder.ivFlag);
            holder.tvName.setText(list.get(position).getName());
            holder.clContainer.setOnClickListener(v -> {
                selectionCountry.onSelectionCountry(list.get(position).getCountry());
            });
        } else {
            Log.e("Flage","else"+list.get(position).getPathFlag());
            Glide.with(context).load(list.get(position).getPathFlag()).into(holder.ivFlag);
            holder.tvName.setText(list.get(position).getName());


            holder.clContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectionCountry.onSelectionCountry(list.get(position).getCountry());
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(ArrayList<Country_Code> list, int type) {
        this.list = new ArrayList<>();
        this.list = list;
        this.type = type;
        notifyDataSetChanged();

    }

    public class Holder extends RecyclerView.ViewHolder {

        ImageView ivFlag;
        TextView tvName;
        ConstraintLayout clContainer;
        LinearLayout llLock;

        public Holder(@NonNull View itemView) {
            super(itemView);
            ivFlag = itemView.findViewById(R.id.rv_server_flag);
            tvName = itemView.findViewById(R.id.rv_server_name);
            clContainer = itemView.findViewById(R.id.container);

        }
    }

    public interface selecTionCountry {
        void onSelectionCountry(Country countryName);
    }

    public void setCallBack(selecTionCountry selecTionCountry) {
        this.selectionCountry = selecTionCountry;
    }


}