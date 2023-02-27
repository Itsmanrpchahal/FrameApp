package com.example.frameapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.frameapp.R;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private Uri imageUri;
    private Integer count;
    private LayoutInflater layoutInflater;
    private ArrayList<Uri> images = new ArrayList<>();
//    private Integer [] images = {R.drawable.group_1,R.drawable.group_1,R.drawable.group_1};

    public ViewPagerAdapter(Context context, Uri imageUri,Integer count,ArrayList<Uri> images) {
        this.context = context;
        this.imageUri = imageUri;
        this.count = count;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_image_layout, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.viewPagerImage);
        imageView.setImageURI(images.get(position));


        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}
