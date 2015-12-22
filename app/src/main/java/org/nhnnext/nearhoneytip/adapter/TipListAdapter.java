package org.nhnnext.nearhoneytip.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
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
import org.nhnnext.nearhoneytip.item.ResponseResult;
import org.nhnnext.nearhoneytip.item.TipItem;
import org.nhnnext.nearhoneytip.item.User;
import org.nhnnext.nearhoneytip.library.ImageLib;
import org.nhnnext.nearhoneytip.remote.RemoteService;
import org.nhnnext.nearhoneytip.remote.ServiceGenerator;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedString;

/**
 * Created by eunjooim on 2015. 11. 10.
 */
public class TipListAdapter extends RecyclerView.Adapter<TipListAdapter.ViewHolder> {

    private Context context;
    private List<TipItem> tipItems;
    private String uid;
    private RemoteService remoteService;

    public TipListAdapter(Context context, List<TipItem> tipItemList, String uid) {
        this.context = context;
        this.tipItems = tipItemList;
        this.uid = uid;
        remoteService = ServiceGenerator.createService(RemoteService.class, RemoteService.BASE_URL);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tip_row_places, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final TipItem tipItem = tipItems.get(position);

        holder.likeBtn.setTag(R.string.liked, false);
        holder.likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_like_off, 0, 0, 0);

        if (tipItem.getStorename() != null) holder.placeStoreName.setText(tipItem.getStorename());
        if (tipItem.getTipdetail() != null) holder.placeTipDetail.setText(tipItem.getTipdetail());
        if (tipItem.getNickname() != null) holder.placeNickName.setText(tipItem.getNickname());
        if (tipItem.getDate() != null) holder.placeDate.setText(tipItem.getDate());

        setBtnNums(holder, tipItem);

        // set images
        ImageLib imageLib = new ImageLib(context);

        imageLib.setPhotoThumbnail(holder.placeImageView, tipItem);
        imageLib.setIconImage(holder.placeProfilePhoto, tipItem.getProfilephoto());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("tip adapter", Integer.toString(v.getId()));
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
                toggleLikeBtn(holder.likeBtn, tipItem.get_id());
            }
        });
        holder.replyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("tip adapter", "댓글");
            }
        });
    }

    private void setBtnNums(ViewHolder holder, TipItem tipItem) {
        String btnText;
        String[] likeList;
        if ((likeList = tipItem.getLike()) != null) {
            setLikeBtn(holder.likeBtn, likeList);
            holder.likeBtn.setTag(R.string.like_num, likeList.length);

            btnText = context.getString(R.string.button_like) + " " + likeList.length;
            holder.likeBtn.setText(btnText);

        }

        if (tipItem.getReply() != null) {
            btnText = context.getString(R.string.button_reply) + " " + tipItem.getLike().length;
            holder.replyBtn.setText(btnText);
        }
    }

    private void setLikeBtn(Button likeBtn, String[] likeList) {
        for (String id : likeList) {
            if (id.equals(uid)) {
                setLikeBtnWhenLoaded(likeBtn);
                break;
            }
        }
    }

    private void toggleLikeBtn(Button likeBtn, String tid) {
        if (likeBtn.getTag(R.string.liked).equals(false)) {
            setLikeBtnOn(likeBtn);

            TypedString newLike = new TypedString("{" + "\"like\":" + "\"" + uid + "\"" + "}");

            remoteService.putLike(tid, newLike, new Callback<ResponseResult>() {

                @Override
                public void success(ResponseResult responseResult, Response response) {
                    Log.d("retrofit", "test success");
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d("retrofit", "test failure");
                }
            });

        } else {
            setLikeBtnOff(likeBtn);

            TypedString newLike = new TypedString("{" + "\"like\":" + "\"" + uid + "\"" + "}");
            remoteService.deleteLike(tid, newLike, new Callback<ResponseResult>() {

                @Override
                public void success(ResponseResult responseResult, Response response) {
                    Log.d("retrofit", "test success");
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d("retrofit", "test failure");
                }
            });
        }
    }

    private void setLikeBtnWhenLoaded(Button likeBtn) {
        likeBtn.setTag(R.string.liked, true);
        likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_like_on, 0, 0, 0);
    }

    private void setLikeBtnOn(Button likeBtn) {
        likeBtn.setTag(R.string.liked, true);
        likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_like_on, 0, 0, 0);
        int likedNum = (Integer) likeBtn.getTag(R.string.like_num) + 1;
        likeBtn.setTag(R.string.like_num, likedNum);

        String btnText = context.getString(R.string.button_like) + " " + likedNum;
        likeBtn.setText(btnText);
    }

    private void setLikeBtnOff(Button likeBtn) {
        likeBtn.setTag(R.string.liked, false);
        likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_like_off, 0, 0, 0);
        int likedNum = (Integer) likeBtn.getTag(R.string.like_num) - 1;
        likeBtn.setTag(R.string.like_num, likedNum);

        String btnText = context.getString(R.string.button_like) + " " + likedNum;
        likeBtn.setText(btnText);
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
