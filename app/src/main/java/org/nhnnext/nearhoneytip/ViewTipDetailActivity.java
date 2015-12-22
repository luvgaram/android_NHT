package org.nhnnext.nearhoneytip;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.nhnnext.nearhoneytip.item.ResponseResult;
import org.nhnnext.nearhoneytip.item.TipItem;
import org.nhnnext.nearhoneytip.library.ImageLib;
import org.nhnnext.nearhoneytip.remote.RemoteService;
import org.nhnnext.nearhoneytip.remote.ServiceGenerator;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedString;

public class ViewTipDetailActivity extends AppCompatActivity {

//    private Bundle bundle;
    private TipItem tipItem;
    private String uid;
    private RemoteService remoteService;

    private Button likeBtn;
    private Button replyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tip_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        remoteService = ServiceGenerator.createService(RemoteService.class, RemoteService.BASE_URL);

        tipItem = (TipItem) getIntent().getSerializableExtra("tipItem");

        SharedPreferences pref = getSharedPreferences("login", MODE_PRIVATE);
        uid = pref.getString("uuid", "");

        setView();
    }

    private void setView() {
        TextView nickName = (TextView) findViewById(R.id.nickName);
        TextView date = (TextView) findViewById(R.id.date);
        TextView tipDetail = (TextView) findViewById(R.id.tipDetail);
        ImageView profilePhoto = (ImageView) findViewById(R.id.profilePhoto);
        ImageView tipImage = (ImageView) findViewById(R.id.tipImage);

        likeBtn = (Button) findViewById(R.id.likeBtn);
        replyBtn = (Button) findViewById(R.id.replyBtn);
        likeBtn.setTag(R.string.liked, false);

        setTitle(tipItem.getStorename());
        nickName.setText(tipItem.getNickname());
        date.setText(tipItem.getDate());
        tipDetail.setText(tipItem.getTipdetail());

        setBtnNums();

        setLikeBtn(tipItem.getLike());



        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleLikeBtn(tipItem.get_id());
            }
        });

        // set images
        ImageLib imageLib = new ImageLib(this);

        imageLib.setPhotoImage(tipImage, tipItem.getFile()[0].getPath());
        imageLib.setIconImage(profilePhoto, tipItem.getProfilephoto());
    }

    private void setBtnNums() {
        String btnText;
        String[] likeList;
        if ((likeList = tipItem.getLike()) != null) {
            setLikeBtn(likeList);
            likeBtn.setTag(R.string.like_num, likeList.length);

            btnText = getString(R.string.button_like) + " " + likeList.length;
            likeBtn.setText(btnText);

        }

        if (tipItem.getReply() != null) {
            btnText = getString(R.string.button_reply) + " " + tipItem.getLike().length;
            replyBtn.setText(btnText);
        }
    }

    private void setLikeBtn(String[] likeList) {
        for (String id : likeList) {
            if (id.equals(uid)) {
                setLikeBtnWhenLoaded();
                break;
            }
        }
    }

    private void setLikeBtnWhenLoaded() {
        likeBtn.setTag(R.string.liked, true);
        likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_like_yellow, 0, 0, 0);
    }

    private void toggleLikeBtn(String tid) {
        if (likeBtn.getTag(R.string.liked).equals(false)) {
            setLikeBtnOn();

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
            setLikeBtnOff();

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

    public void setLikeBtnOn() {
        likeBtn.setTag(R.string.liked, true);
        likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_like_yellow, 0, 0, 0);
        int likedNum = (Integer) likeBtn.getTag(R.string.like_num) + 1;
        likeBtn.setTag(R.string.like_num, likedNum);

        String btnText = getString(R.string.button_like) + " " + likedNum;
        likeBtn.setText(btnText);
    }

    public void setLikeBtnOff() {
        likeBtn.setTag(R.string.liked, false);
        likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_like_off, 0, 0, 0);
        int likedNum = (Integer) likeBtn.getTag(R.string.like_num) - 1;
        likeBtn.setTag(R.string.like_num, likedNum);

        String btnText = getString(R.string.button_like) + " " + likedNum;
        likeBtn.setText(btnText);
    }
}
