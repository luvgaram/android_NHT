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

import org.nhnnext.nearhoneytip.listener.OnPhotoSelectedListener;
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
        final Uri imageUri = Uri.withAppendedPath(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID)));

        Picasso.with(context)
                .load(imageUri)
                .fit()
                .centerCrop()
                .into(holder.placeImageView);

        holder.placeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPhotoSelectedListener.onPhotoSelected(imageUri);
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
