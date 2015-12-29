package org.nhnnext.nearhoneytip.library;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.nhnnext.nearhoneytip.R;
import org.nhnnext.nearhoneytip.item.NearTipItem;
import org.nhnnext.nearhoneytip.item.TipItem;
import org.nhnnext.nearhoneytip.remote.RemoteService;

/**
 * Created by eunjooim on 15. 12. 14.
 */
public class ImageLib {

    private Context context;

    public ImageLib(Context context) {
        this.context = context;
    }

    public void setPhotoThumbnail(ImageView imageView, NearTipItem tipItem) {

        String imagePath = tipItem.getFile()[0].getPath();

        if (imagePath != null) {
            imagePath = imagePath.replaceAll("data/", "");

            Picasso.with(context)
                    .load(RemoteService.BASE_URL + "/image/photo=" + imagePath)
                    .fit()
                    .centerCrop()
                    .into(imageView);
        } else {
            imageView.setBackgroundResource(R.drawable.nht_logo);
        }
    }

    public void setPhotoThumbnail(ImageView imageView, TipItem tipItem) {

        String imagePath = tipItem.getFile()[0].getPath();

        if (imagePath != null) {
            imagePath = imagePath.replaceAll("data/", "");

            Picasso.with(context)
                    .load(RemoteService.BASE_URL + "/image/photo=" + imagePath)
                    .fit()
                    .centerCrop()
                    .into(imageView);
        } else {
            imageView.setBackgroundResource(R.drawable.nht_logo);
        }
    }

    public void setPhotoImage(ImageView imageView, String imagePath) {

//        String imagePath = tipItem.getFile()[0].getPath();

        if (imagePath != null) {
            imagePath = imagePath.replaceAll("data/", "");

            Picasso.with(context)
                    .load(RemoteService.BASE_URL + "/image/photo=" + imagePath)
                    .fit()
                    .centerInside()
                    .placeholder(R.drawable.nht_logo)
                    .into(imageView);
        } else {
            imageView.setBackgroundResource(R.drawable.nht_logo);
        }
    }

    public void setIconImage(ImageView imageView, String imagePath) {
//        String imagePath = tipItem.getProfilephoto();

        if (imagePath != null) {
            imagePath = imagePath.replaceAll("icon/", "");

            Picasso.with(context)
                    .load(RemoteService.BASE_URL + "/image/icon=" + imagePath)
                    .fit()
                    .centerCrop()
                    .transform(new RoundedTransform())
                    .placeholder(R.drawable.nht_logo)
                    .into(imageView);
        } else {
            imageView.setBackgroundResource(R.drawable.nht_logo);
        }
    }
}
