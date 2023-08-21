package com.video.music.downloader.videoplayer.adapter;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.video.music.downloader.AdsUtils.FirebaseADHandlers.AdUtils;
import com.video.music.downloader.AdsUtils.Utils.Constants;
import com.video.music.downloader.R;
import com.video.music.downloader.videoplayer.data.VideoData;

import java.util.List;

public class VideoFilesAdapter extends RecyclerView.Adapter<VideoFilesAdapter.ViewHolder> {

    private final List<VideoData> list;
    private final VideoFilesHandler handler;
    Context context;

    public VideoFilesAdapter(List<VideoData> list, Context context, VideoFilesHandler handler) {
        this.list = list;
        this.handler = handler;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ImageView image = holder.videoPreview;
       int res = position % 10;

      /*  if (position != 0) {
            if (position == 4) {
                Log.e("onBindViewHolder4: ", position + "   " + res + "   ");
                holder.relativeLayout.setVisibility(View.VISIBLE);
                AdUtils.showNativeAd((Activity) context, Constants.adsJsonPOJO.getParameters().getNative_id().getDefaultValue().getValue(), holder.nativeads, true);
            } else if ((position != 5 && res == 4) || res == 9) {
                Log.e("onBindViewHolder5: ", position + "   " + res + "   ");
                holder.relativeLayout.setVisibility(View.VISIBLE);
                AdUtils.showNativeAd((Activity) context, Constants.adsJsonPOJO.getParameters().getNative_id().getDefaultValue().getValue(), holder.nativeads, true);
            }
        }*/
//        Glide.with(image)
//                .load(list.get(position).videouri)
//                .into(image);
        Glide.with(context).load(list.get(position).videouri).into(image);
        holder.videoTitle.setText(list.get(position).videoName);
//        holder.videoduration.setText(list.get(position).vide);

        holder.itemView.setOnClickListener(view -> handler.onChoose(position));

        String val = String.valueOf(holder.getAdapterPosition());
        if (val.endsWith("4") || val.endsWith("9")) {
            Log.e("onBindViewHolder: ", position + "   " + val + "   "+val.endsWith("4")+ "  "+val.endsWith("9"));
            holder.relativeLayout.setVisibility(View.VISIBLE);
            AdUtils.showNativeAd((Activity) context, Constants.adsJsonPOJO.getParameters().getNative_id().getDefaultValue().getValue(), holder.nativeads, true);
        } else {
            holder.relativeLayout.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView videoPreview;
        TextView videoTitle,videoduration;
        RelativeLayout relativeLayout;
        LinearLayout nativeads;
        CardView crd;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            videoPreview = itemView.findViewById(R.id.iv_song_cover);
            videoTitle = itemView.findViewById(R.id.tv_song_title);
            relativeLayout = itemView.findViewById(R.id.rl);
            nativeads = itemView.findViewById(R.id.native_ads);
            crd = itemView.findViewById(R.id.crd);

//            videoduration = itemView.findViewById(R.id.tv_song_artist);

        }
    }

    public interface VideoFilesHandler {
        void onChoose(int position);
    }
}
