package org.nhnnext.nearhoneytip;


import android.app.FragmentTransaction;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;

import android.widget.Toast;

import org.nhnnext.nearhoneytip.remote.RemoteService;
import org.nhnnext.nearhoneytip.remote.ServiceGenerator;

import java.io.File;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class WritingActivity extends AppCompatActivity implements ImagePickFragment.OnHidePhotoListListener{

    private FragmentTransaction fragmentTransaction;
    private EditText storeName;
    private EditText tipDetail;
    private Boolean isPhotoSelected = false;

    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        storeName = (EditText) findViewById(R.id.storename);
        tipDetail = (EditText) findViewById(R.id.tipdetail);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setImagePickFragment();

    }

    private void setImagePickFragment() {
        ImagePickFragment fragment = new ImagePickFragment(this);

        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down);
        fragmentTransaction.add(R.id.imagegrid, fragment);
        fragmentTransaction.commit();

        // hide keyboard
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }

    @Override
    public void onHidePhotoList(int imageID) {
        Log.d("writingAct:", imageID + "");
        ImageViewFragment imageViewFragment = ImageViewFragment.newInstance(imageID);

        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.imagegrid, imageViewFragment);
        fragmentTransaction.commit();

        isPhotoSelected = true;

        imageUri = Uri.withAppendedPath(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                Integer.toString(imageID));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_writing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save) {
            setNewTip();
        }

        return id == R.id.action_save || super.onOptionsItemSelected(item);
    }

    private void setNewTip() {

        if (isPhotoSelected && storeName.getText() != null) {

            String actualPath = getRealPathFromUri(imageUri);

            Log.d("actualPath: ", actualPath);

            // TODO resize photo
            TypedFile typedFile = new TypedFile("multipart/form-data", new File(actualPath));

            RemoteService remoteService = ServiceGenerator.createService(RemoteService.class, RemoteService.BASE_URL);
            remoteService.postTip(typedFile, storeName.getText().toString(), tipDetail.getText().toString(), new Callback<String>() {
                @Override
                public void success(String tipItem, Response response) {
                    Log.d("retrofit", "upload success " + response.toString());
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d("retrofit", "test failure " + error.toString());
                }
            });

        } else {
            Toast.makeText(getApplicationContext(), "가게 이름과 사진이 필요합니다", Toast.LENGTH_SHORT).show();
        }
    }

    public String getRealPathFromUri(Uri contentUri) {
        Cursor cursor = null;
        try {
            cursor = getApplicationContext().getContentResolver().query(
                    contentUri, new String[] {MediaStore.Images.Media.DATA}, null, null, null);
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

}
