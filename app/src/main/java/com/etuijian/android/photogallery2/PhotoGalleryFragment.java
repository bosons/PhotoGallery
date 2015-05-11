package com.etuijian.android.photogallery2;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xyang on 5/8/15.
 */
public class PhotoGalleryFragment extends Fragment {

    private RecyclerView mPhotoRecyclerView;
    private List<GalleryItem> mItems = new ArrayList<GalleryItem>();

    public static PhotoGalleryFragment newInstance() {
        return new PhotoGalleryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new FetchItemsTask().execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_photo_gallery, container, false);
        mPhotoRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_photo_gallery_recycler_view);
        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        return v;
    }

    private void setUpAdaptor() {
        if(isAdded()) {
            mPhotoRecyclerView.setAdapter(new PhotoAdapter(mItems));
        }
    }


    private class PhotoHolder extends RecyclerView.ViewHolder{
        private ImageView mItemImageView;
        public PhotoHolder(View itemView) {
            super(itemView);
            mItemImageView = (ImageView)itemView.findViewById(R.id.fragment_photo_gallery_image_view);
        }
        public void bindDrawable(Drawable drawable) {
            mItemImageView.setImageDrawable(drawable);
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
        private List<GalleryItem> mGalleryItemList;

        public PhotoAdapter(List<GalleryItem> items) {
            mGalleryItemList = items;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.gallery_item, viewGroup, false);
            return new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoHolder photoHolder, int i) {
            Drawable placeHolder = getResources().getDrawable(R.drawable.abc_btn_radio_material);
            photoHolder.bindDrawable(placeHolder);
        }

        @Override
        public int getItemCount() {
            return mGalleryItemList.size();
        }
    }

    private class FetchItemsTask extends AsyncTask<Void, Void, List<GalleryItem>> {
        @Override
        protected List<GalleryItem> doInBackground(Void... params) {
            return new FlickrFetchr().fetchItem();
        }

        @Override
        protected void onPostExecute(List<GalleryItem> galleryItems) {
            mItems = galleryItems;
            setUpAdaptor();
        }
    }
}
