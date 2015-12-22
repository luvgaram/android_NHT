package org.nhnnext.nearhoneytip.library;

import android.content.Context;
import android.util.Log;
import android.widget.Button;

import org.nhnnext.nearhoneytip.R;
import org.nhnnext.nearhoneytip.item.ResponseResult;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedString;

/**
 * Created by eunjooim on 15. 12. 22.
 */
public class LikeLib {

    private Context context;
    private Button button;
    public LikeLib(Context context, Button button) {
        this.context = context;
        this.button = button;
    }

    public void setLikeBtnWhenLoaded() {
        button.setTag(R.string.liked, true);
        button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_like_on, 0, 0, 0);
    }

    public void setLikeBtnOn() {
        button.setTag(R.string.liked, true);
        button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_like_on, 0, 0, 0);
        int likedNum = (Integer) button.getTag(R.string.like_num) + 1;
        button.setTag(R.string.like_num, likedNum);

        String btnText = context.getString(R.string.button_like) + " " + likedNum;
        button.setText(btnText);
    }

    public void setLikeBtnOff() {
        button.setTag(R.string.liked, false);
        button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_like_off, 0, 0, 0);
        int likedNum = (Integer) button.getTag(R.string.like_num) - 1;
        button.setTag(R.string.like_num, likedNum);

        String btnText = context.getString(R.string.button_like) + " " + likedNum;
        button.setText(btnText);
    }
}
