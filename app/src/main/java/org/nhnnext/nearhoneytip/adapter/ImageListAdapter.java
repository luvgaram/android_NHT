package org.nhnnext.nearhoneytip.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.nhnnext.nearhoneytip.OnPhotoSelectedListener;
import org.nhnnext.nearhoneytip.R;

/**
 * Created by eunjooim on 2015. 12. 3.
 */
public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ViewHolder> {

    Context context;
    private Cursor cursor;
    OnPhotoSelectedListener onPhotoSelectedListener;

    public ImageListAdapter(Context context, Cursor cursor, OnPhotoSelectedListener onPhotoSelectedListener) {
        this.context = context;
        this.cursor = cursor;
        this.onPhotoSelectedListener = onPhotoSelectedListener;
        Log.d("imageListAdapter", cursor.toString());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_row_places, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        
        cursor.moveToPosition(position);
        Uri imageUri = Uri.withAppendedPath(
                MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                cursor.getString(cursor.getColumnIndex(MediaStore.Images.Thumbnails._ID)));

        // TODO rotate thumbnails
        Picasso.with(context)
                .load(imageUri)
                .into(holder.placeImageView);

        final int imageId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID)));


        holder.placeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPhotoSelectedListener.onPhotoSelected(imageId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView placeImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            placeImageView = (ImageView)itemView.findViewById(R.id.placeImage);
        }
    }
}
