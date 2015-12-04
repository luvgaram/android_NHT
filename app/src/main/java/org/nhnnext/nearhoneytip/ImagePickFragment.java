package org.nhnnext.nearhoneytip;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.nhnnext.nearhoneytip.adapter.ImageListAdapter;

@SuppressLint("ValidFragment")
public class ImagePickFragment extends Fragment implements OnPhotoSelectedListener {

    private View view;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager staggeredLayoutManager;
    private Cursor cursor;
    private OnHidePhotoListListener onHidePhotoListListener;

    public interface OnHidePhotoListListener {
        void onHidePhotoList(int path);
    }

    public ImagePickFragment(OnHidePhotoListListener onHidePhotoListListener) {
        this.onHidePhotoListListener = onHidePhotoListListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_image_pick, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.imagelist);
        staggeredLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);

        ContentResolver contentResolver = getActivity().getContentResolver();

        cursor = contentResolver.query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, null, null, null, "image_id DESC");
        ImageListAdapter imageAdapter = new ImageListAdapter(getActivity(), cursor, this); // this class implements callback

        recyclerView.setLayoutManager(staggeredLayoutManager);
        recyclerView.setAdapter(imageAdapter);
    }

    @Override
    public void onPhotoSelected(int imageID) {
        Log.i("ImageFragment: ", imageID + "");
        onHidePhotoListListener.onHidePhotoList(imageID);
    }
}

//        // First request thumbnails what you want
//        String[] projection = new String[] {MediaStore.Images.Thumbnails._ID, MediaStore.Images.Thumbnails.IMAGE_ID};
//        Cursor thumbnails = contentResolver.query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null);
//
//        Log.d("test!", thumbnails.getCount() + "");
//
//        // Then walk thru result and obtain imageId from records
//        for (thumbnails.moveToFirst(); !thumbnails.isAfterLast(); thumbnails.moveToNext()) {
//            String imageId = thumbnails.getString(thumbnails.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID));
//
//            // Request image related to this thumbnail
//            String[] filePathColumn = { MediaStore.Images.Media.DATA };
//
//            Cursor images = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, filePathColumn, MediaStore.Images.Media._ID + "=?", new String[] {imageId}, null);
//
//            if (cursor != null && cursor.moveToFirst()) {
//                // Your file-path will be here
//                String filePath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
//
//                Log.d("image", filePath);
//            }
//        }