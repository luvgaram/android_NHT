package org.nhnnext.nearhoneytip;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.nhnnext.nearhoneytip.adapter.ImageListAdapter;
import org.nhnnext.nearhoneytip.item.TipItem;
import org.nhnnext.nearhoneytip.listener.OnPhotoSelectedListener;
import org.nhnnext.nearhoneytip.remote.RemoteService;
import org.nhnnext.nearhoneytip.remote.ServiceGenerator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class WritingTipActivity extends AppCompatActivity implements OnPhotoSelectedListener {

    private EditText storeName;
    private EditText tipDetail;
    private ImageView preview;
    private Menu menu;
    private ProgressDialog progressDialog;
    private String uid;
    private String nickname;
    private String profilephoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_tip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences pref = getSharedPreferences("login", MODE_PRIVATE);
        uid = pref.getString("uuid", "");
        nickname = pref.getString("nickname", "");
        profilephoto = pref.getString("profilephoto", "");

        storeName = (EditText) findViewById(R.id.storename);
        tipDetail = (EditText) findViewById(R.id.tipdetail);
        preview = (ImageView) findViewById(R.id.preview);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // hide keyboard
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.imagelist);
        StaggeredGridLayoutManager staggeredLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Files.FileColumns.DATE_ADDED + " DESC");

        Log.d("writing cursor: ", cursor.getCount() + "");
        ImageListAdapter imageAdapter = new ImageListAdapter(this, cursor, this); // this class implements callback

        recyclerView.setLayoutManager(staggeredLayoutManager);
        recyclerView.setAdapter(imageAdapter);
    }

    @Override
    public void onPhotoSelected(Uri uri) {
        Picasso.with(this)
                .load(uri)
                .fit()
                .centerCrop()
                .into(preview);

        preview.setTag(uri.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_writing_tip, menu);
        this.menu = menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_cancel) {
            finish();
        } else if (id == R.id.action_next ) {
            checkPhoto();
        } else if (id == R.id.action_previous) {
            setPickPhoto();
        } else if (id == R.id.action_save) {
            checkInputTip();
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkPhoto() {
        if (preview.getTag() != null) {
            setWritingEditor();
        } else Toast.makeText(getApplicationContext(), "사진을 선택해주세요!", Toast.LENGTH_SHORT).show();
    }

    private void setWritingEditor() {
        RecyclerView imageList = (RecyclerView) findViewById(R.id.imagelist);
        imageList.setVisibility(RecyclerView.GONE);

        RelativeLayout storeInfo = (RelativeLayout) findViewById(R.id.storeinfo);
        storeInfo.setVisibility(View.VISIBLE);

        menu.findItem(R.id.action_previous).setVisible(true);
        menu.findItem(R.id.action_next).setVisible(false);
        menu.findItem(R.id.action_save).setVisible(true);
    }

    private void setPickPhoto() {
        RecyclerView imageList = (RecyclerView) findViewById(R.id.imagelist);
        imageList.setVisibility(RecyclerView.VISIBLE);

        RelativeLayout storeInfo = (RelativeLayout) findViewById(R.id.storeinfo);
        storeInfo.setVisibility(View.GONE);

        menu.findItem(R.id.action_previous).setVisible(false);
        menu.findItem(R.id.action_next).setVisible(true);
        menu.findItem(R.id.action_save).setVisible(false);
    }

    private void checkInputTip() {

        // TODO string library 만들기 null이 아니고 공백, 공백 여러개(트림처리)도 아니면 처리하도록

        if (storeName.getText() != null) {

            progressDialog = ProgressDialog.show(this, "", "업로드중입니다...");

            String actualPath = getRealPathFromUriString(preview.getTag().toString());

            File newFile = setPhotoFile(actualPath);

//            TypedFile typedFile = new TypedFile("multipart/form-data", new File(actualPath));
            TypedFile typedFile = new TypedFile("multipart/form-data", newFile);

            postTip(typedFile);

        } else {
            Toast.makeText(getApplicationContext(), "가게 이름과 사진이 필요합니다", Toast.LENGTH_SHORT).show();
        }
    }

    private void postTip(TypedFile typedFile) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class, RemoteService.BASE_URL);
        remoteService.postTip(
                storeName.getText().toString(),
                tipDetail.getText().toString(),
                uid,
                nickname,
                profilephoto,
                typedFile,
                new Callback<TipItem>() {
            @Override
            public void success(TipItem tipItem, Response response) {
                Log.d("retrofit", "upload success " + response.toString());
                progressDialog.cancel();
                finish();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("retrofit", "test failure " + error.toString());
                Toast.makeText(getApplicationContext(), "업로드가 실패했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getRealPathFromUriString(String contentUri) {
        Cursor cursor = null;
        try {
            cursor = getApplicationContext().getContentResolver().query(
                    Uri.parse(contentUri), new String[] {MediaStore.Images.Media.DATA}, null, null, null);
            int column_index;
            if (cursor != null) {
                column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            }
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @NonNull
    private File setPhotoFile(String path) {

        ImageView selectedPhoto = (ImageView) findViewById(R.id.preview);
        Bitmap bitmap = ((BitmapDrawable)selectedPhoto.getDrawable()).getBitmap();

        File newFile = new File(path);

        try {
            newFile.createNewFile();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, bos);

            FileOutputStream fos = new FileOutputStream(newFile);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();


        } catch (IOException e) {
            e.printStackTrace();
        } return newFile;
    }
}
