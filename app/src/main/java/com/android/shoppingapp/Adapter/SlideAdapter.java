package com.android.shoppingapp.Adapter;

/**
 * Created by welcome on 23-08-2017.
 */

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.android.ecommerce.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SlideAdapter extends PagerAdapter {

        private ArrayList<String> images;
        private LayoutInflater inflater;
        private Context context;

        public SlideAdapter(Context context, ArrayList<String> images) {
            this.context = context;
            this.images=images;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            View myImageLayout = inflater.inflate(R.layout.slide, view, false);
            ImageView myImage = (ImageView) myImageLayout
                    .findViewById(R.id.image);

            Glide.with(context)
                .load(images.get(position))
                    .into(myImage);

            view.addView(myImageLayout, 0);
            return myImageLayout;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }
    }
