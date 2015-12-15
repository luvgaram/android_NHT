package org.nhnnext.nearhoneytip.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.nhnnext.nearhoneytip.R;
import org.nhnnext.nearhoneytip.ViewTipDetailActivity;
import org.nhnnext.nearhoneytip.item.TipItem;
import org.nhnnext.nearhoneytip.library.ImageLib;

import java.util.List;

/**
 * Created by eunjooim on 2015. 11. 10.
 */
public class TipListAdapter extends RecyclerView.Adapter<TipListAdapter.ViewHolder> {

    Context context;
    private List<TipItem> tipItems;

    public TipListAdapter(Context context, List<TipItem> tipItemList) {
        this.context = context;
        this.tipItems = tipItemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tip_row_places, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final TipItem tipItem = tipItems.get(position);

//        holder._id = tipItem.get_id();

        if (tipItem.getStorename() != null) holder.placeStoreName.setText(tipItem.getStorename());
        if (tipItem.getTipdetail() != null) holder.placeTipDetail.setText(tipItem.getTipdetail());
        if (tipItem.getNickname() != null) holder.placeNickName.setText(tipItem.getNickname());
        if (tipItem.getDate() != null) holder.placeDate.setText(tipItem.getDate());

        // set images
        ImageLib imageLib = new ImageLib(context);

        imageLib.setPhotoThumbnail(holder.placeImageView, tipItem);
        imageLib.setIconImage(holder.placeProfilePhoto, tipItem);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("tip adapter", Integer.toString(v.getId()));
                startViewDetailActivity();
            }

            private void startViewDetailActivity() {
                Intent intent = new Intent(context, ViewTipDetailActivity.class);
                intent.putExtra("tipItem", tipItem);

                context.startActivity(intent);
            }
        });

        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("tip adapter", "좋아요");
            }
        });

        holder.replyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("tip adapter", "댓글");
            }
        });
    }



    @Override
    public int getItemCount() {
        return tipItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView placeStoreName;
        public ImageView placeImageView;
        public TextView placeTipDetail;
        public ImageView placeProfilePhoto;
        public TextView placeNickName;
        public TextView placeDate;
        public Button likeBtn;
        public Button replyBtn;

        public ViewHolder(View itemView) {
            super(itemView);

            placeStoreName = (TextView)itemView.findViewById(R.id.placeStoreName);
            placeImageView = (ImageView)itemView.findViewById(R.id.placeImage);
            placeTipDetail = (TextView)itemView.findViewById(R.id.placeTipDetail);
            placeProfilePhoto = (ImageView)itemView.findViewById(R.id.placeProfilePhoto);
            placeNickName = (TextView)itemView.findViewById(R.id.placeNickName);
            placeDate = (TextView)itemView.findViewById(R.id.placeDate);
            likeBtn = (Button)itemView.findViewById(R.id.likeBtn);
            replyBtn = (Button)itemView.findViewById(R.id.replyBtn);
        }
    }
}
