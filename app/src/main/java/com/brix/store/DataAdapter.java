package com.brix.store;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder> {

    private ArrayList<String> mNewsHeadline;
    private ArrayList<Bitmap> mNewsImageContainer;
    private ArrayList<String> mNewsDescription;
    Activity mActivity;

    public DataAdapter(MainActivity activity, ArrayList<String> mNewsHeadline, ArrayList<Bitmap> mNewsImageContainer, ArrayList<String> mNewsDescription) {
        this.mActivity = activity;
        this.mNewsHeadline = mNewsHeadline;
        this.mNewsImageContainer = mNewsImageContainer;
        this.mNewsDescription = mNewsDescription;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView news_headline, news_description;
        private ImageView image_container;

        // get view ID's
        public MyViewHolder(View view) {
            super(view);
            news_headline = view.findViewById(R.id.row_news_headline);
            image_container = view.findViewById(R.id.row_image_container);
            news_description = view.findViewById(R.id.row_news_description);

            // Make CardView of each news article clickable
            // onClick open popup window with full article
            CardView cardView = view.findViewById(R.id.cardView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mActivity, "Works", Toast.LENGTH_SHORT).show();
                    NewsDialog.showMyDialog(mActivity);
                }
            });
        }
    }


    // view holder
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_data, parent, false);

        return new MyViewHolder(itemView);
    }

    // set image and text
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.news_headline.setText(mNewsHeadline.get(position));
        holder.image_container.setImageBitmap(mNewsImageContainer.get(position));
        holder.news_description.setText(mNewsDescription.get(position));
    }

    @Override
    public int getItemCount() {
        return mNewsHeadline.size();
    }
}
