package org.nhnnext.nearhoneytip;


import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageViewFragment extends Fragment {

    private View view;
    private int imageID;

    public static ImageViewFragment newInstance(int path) {
        ImageViewFragment imageViewFragment = new ImageViewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("imageID", path);
        imageViewFragment.setArguments(bundle);

        return imageViewFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_image_view, container, false);
        this.imageID = getArguments().getInt("imageID");

        Log.d("ImageView: ", imageID + "");

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageView selectedPhoto = (ImageView) view.findViewById(R.id.selectedphoto);

        Uri imageUri = Uri.withAppendedPath(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                Integer.toString(imageID));

                Picasso.with(getActivity())
                        .load(imageUri)
                        .into(selectedPhoto);

    }
}
