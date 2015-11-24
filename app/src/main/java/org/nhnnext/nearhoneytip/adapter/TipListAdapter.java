package org.nhnnext.nearhoneytip.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.nhnnext.nearhoneytip.R;
import org.nhnnext.nearhoneytip.item.TipItem;
import org.nhnnext.nearhoneytip.remote.RemoteService;

import java.util.List;

/**
 * Created by eunjooim on 2015. 11. 10.
 */
public class TipListAdapter extends RecyclerView.Adapter<TipListAdapter.ViewHolder> {

    Context context;
    private List<TipItem> tipitems;

    public TipListAdapter(Context context, List<TipItem> tipItemList) {
        this.context = context;
        this.tipitems = tipItemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tip_row_places, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TipItem tipItem = tipitems.get(position);

        holder._id = tipItem.get_id();
        holder.placeStoreName.setText(tipItem.getStorename());
        holder.placeTipDetail.setText(tipItem.getTipdetail());
        holder.placeNickName.setText(tipItem.getNickname());
        holder.placeDate.setText(tipItem.getDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("tip adapter", Integer.toString(v.getId()));
            }
        });

        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("tip adapter", Integer.toString(v.getId()));
            }
        });

        holder.replyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("tip adapter", Integer.toString(v.getId()));
            }
        });
        // set images
        setPhotoImage(holder, tipItem);
        setIconImage(holder, tipItem);


    }

    private void setPhotoImage(ViewHolder holder, TipItem tipItem) {

        String imagePath = tipItem.getFile()[0].getPath();

        if (imagePath != null) {
            imagePath = imagePath.replaceAll("data/", "");

            Picasso.with(context)
                    .load(RemoteService.BASE_URL + "/image/photo=" + imagePath)
                    .into(holder.placeImageView);
        } else {
            holder.placeImageView.setBackgroundResource(R.drawable.nht_logo);
        }
    }

    private void setIconImage(ViewHolder holder, TipItem tipItem) {
        String imagePath = tipItem.getProfilephoto();

        if (imagePath != null) {
            imagePath = imagePath.replaceAll("icon/", "");

            Picasso.with(context)
                    .load(RemoteService.BASE_URL + "/image/icon=" + imagePath)
                    .into(holder.placeProfilePhoto);
        } else {
            holder.placeImageView.setBackgroundResource(R.drawable.nht_logo);
        }
    }

    @Override
    public int getItemCount() {
        return tipitems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public String _id;
        public CardView placeCard;
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
