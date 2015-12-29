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
import org.nhnnext.nearhoneytip.item.NearTipItem;
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
    private List<NearTipItem> tipItems;
    private String uid;
    private RemoteService remoteService;

    public TipListAdapter(Context context, List<NearTipItem> tipItemList, String uid) {
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
        final NearTipItem tipItem = tipItems.get(position);

//        holder.likeBtn.setTag(R.string.liked, false);
//        holder.likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_like_off, 0, 0, 0);

        if (tipItem.getStorename() != null) holder.placeStoreName.setText(tipItem.getStorename());
        String distance = context.getString(R.string.distance) + " " + tipItem.getDis() + " " + context.getString(R.string.meter);
        holder.placeDistance.setText(distance);

        if (tipItem.getTipdetail() != null) holder.placeTipDetail.setText(tipItem.getTipdetail());
        if (tipItem.getNickname() != null) holder.placeNickName.setText(tipItem.getNickname());
        if (tipItem.getDate() != null) holder.placeDate.setText(tipItem.getDate());

        if (tipItem.isliked()) holder.likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_like_on, 0, 0, 0);
        else holder.likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_like_off, 0, 0, 0);

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
//                NearTipItem modifiedTip = toggleLikeBtn(holder.likeBtn, tipItem.get_id(), tipItem);
//                tipItem.setIsliked(modifiedTip.isliked());
//                tipItem.setLike(modifiedTip.getLike());

                int likeNum = tipItem.getLike();
                boolean isLiked = tipItem.isliked();
                TypedString newLike = new TypedString("{" + "\"like\":" + "\"" + uid + "\"" + "}");
                String tid = tipItem.get_id();

                if (isLiked) {
                    isLiked = false;
                    likeNum--;

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

                } else {
                    isLiked = true;
                    likeNum++;

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
                }

                tipItem.setLike(likeNum);
                tipItem.setIsliked(isLiked);

                notifyDataSetChanged();
            }
        });
        holder.replyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("tip adapter", "댓글");
            }
        });
    }

    private void setBtnNums(ViewHolder holder, NearTipItem tipItem) {
        String btnText;
        String likeNum = tipItem.getLike() + "";

//        holder.likeBtn.setTag(R.string.like_num, likeNum);

        btnText = context.getString(R.string.button_like) + " " + likeNum;
        holder.likeBtn.setText(btnText);
    }

//        if (tipItem.getReply() != null) {
//            btnText = context.getString(R.string.button_reply) + " " + tipItem.getLike().length;
//            holder.replyBtn.setText(btnText);
//        }
//    }
//
//    private void setLikeBtn(Button likeBtn, String[] likeList) {
//        for (String id : likeList) {
//            if (id.equals(uid)) {
//                setLikeBtnWhenLoaded(likeBtn);
//                break;
//            }
//        }
//    }

    private NearTipItem toggleLikeBtn(Button likeBtn, String tid, NearTipItem tipItem) {
        NearTipItem modifiedTip;

//        if (likeBtn.getTag(R.string.liked).equals(false)) {
        if (!tipItem.isliked()) {
            modifiedTip = setLikeBtnOn(likeBtn, tipItem);

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
            modifiedTip = setLikeBtnOff(likeBtn, tipItem);

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

        return modifiedTip;
    }

//    private void setLikeBtnWhenLoaded(Button likeBtn) {
//        likeBtn.setTag(R.string.liked, true);
//        likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_like_on, 0, 0, 0);
//    }

    private NearTipItem setLikeBtnOn(Button likeBtn, NearTipItem tipItem) {
//        likeBtn.setTag(R.string.liked, true);
//        int likedNum = (Integer) likeBtn.getTag(R.string.like_num) + 1;
//        likeBtn.setTag(R.string.like_num, likedNum);

        likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_like_on, 0, 0, 0);
        int likedNum = tipItem.getLike() + 1;

        String btnText = context.getString(R.string.button_like) + " " + likedNum;
        likeBtn.setText(btnText);

        tipItem.setIsliked(true);
        tipItem.setLike(likedNum);

        return tipItem;
    }

    private NearTipItem setLikeBtnOff(Button likeBtn, NearTipItem tipItem) {
//        likeBtn.setTag(R.string.liked, false);
//        int likedNum = (Integer) likeBtn.getTag(R.string.like_num) - 1;
//        likeBtn.setTag(R.string.like_num, likedNum);

        likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_like_off, 0, 0, 0);
        int likedNum = tipItem.getLike() - 1;

        String btnText = context.getString(R.string.button_like) + " " + likedNum;
        likeBtn.setText(btnText);

        tipItem.setIsliked(false);
        tipItem.setLike(likedNum);

        return tipItem;
    }

    @Override
    public int getItemCount() {
        return tipItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView placeStoreName;
        public ImageView placeImageView;
        public TextView placeDistance;
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
            placeDistance = (TextView)itemView.findViewById(R.id.placeDistance);
            placeTipDetail = (TextView)itemView.findViewById(R.id.placeTipDetail);
            placeProfilePhoto = (ImageView)itemView.findViewById(R.id.placeProfilePhoto);
            placeNickName = (TextView)itemView.findViewById(R.id.placeNickName);
            placeDate = (TextView)itemView.findViewById(R.id.placeDate);
            likeBtn = (Button)itemView.findViewById(R.id.likeBtn);
            replyBtn = (Button)itemView.findViewById(R.id.replyBtn);
        }
    }
}
