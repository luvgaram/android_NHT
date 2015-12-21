package org.nhnnext.nearhoneytip;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import org.nhnnext.nearhoneytip.item.TipItem;
import org.nhnnext.nearhoneytip.library.ImageLib;

public class ViewTipDetailActivity extends AppCompatActivity {

//    private Bundle bundle;
    private TipItem tipItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tip_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tipItem = (TipItem) getIntent().getSerializableExtra("tipItem");
        setView();
    }

    private void setView() {
        TextView nickName = (TextView) findViewById(R.id.nickName);
        TextView date = (TextView) findViewById(R.id.date);
        TextView tipDetail = (TextView) findViewById(R.id.tipDetail);
        ImageView profilePhoto = (ImageView) findViewById(R.id.profilePhoto);
        ImageView tipImage = (ImageView) findViewById(R.id.tipImage);

        setTitle(tipItem.getStorename());

        nickName.setText(tipItem.getNickname());
        date.setText(tipItem.getDate());
        tipDetail.setText(tipItem.getTipdetail());

        // set images
        ImageLib imageLib = new ImageLib(this);

        imageLib.setPhotoImage(tipImage, tipItem.getFile()[0].getPath());
        imageLib.setIconImage(profilePhoto, tipItem.getProfilephoto());
    }
}
