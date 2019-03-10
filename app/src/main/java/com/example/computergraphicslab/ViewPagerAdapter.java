package com.example.computergraphicslab;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.viewpager.widget.PagerAdapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private static ArrayList<Bitmap> mBitmapImages = new ArrayList<Bitmap>();



    ViewPagerAdapter(Context context, ArrayList<Bitmap> historyList){
        mContext = context;
        mBitmapImages = historyList;
    }

    @Override
    public int getCount() {
        if(mBitmapImages != null && mBitmapImages.size() != 0){
            return mBitmapImages.size();
        }else{
            return 0;
        }

    }

    public static Bitmap getmCurrentBitmap(int i ) {
        return mBitmapImages.get(i);
    }


    @Override
    public boolean isViewFromObject( View view, Object o) {
        return view == o;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        PhotoView photoView  = new PhotoView (mContext);
        photoView.setImageBitmap(mBitmapImages.get(position));
        container.addView(photoView,0);
        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position,  Object object) {
        container.removeView((ImageView)object);
    }


}
