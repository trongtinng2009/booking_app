package com.example.bookingapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingapp.Model.Review;
import com.example.bookingapp.R;
import com.example.bookingapp.Utils.Utils;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    ArrayList<Review> reviews;
    Context context;
    int layoutid;

    public ReviewAdapter(ArrayList<Review> reviews, Context context, int layoutid) {
        this.reviews = reviews;
        this.context = context;
        this.layoutid = layoutid;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReviewHolder(LayoutInflater.from(context).inflate(layoutid,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Review review = reviews.get(position);
        ReviewHolder reviewHolder = (ReviewHolder) holder;
        reviewHolder.txtname.setText(review.getUser().getFullname());
        reviewHolder.txtcontent.setText(review.getContent());
        reviewHolder.txtpost.setText(Utils.dateToString(review.getPostdate().toDate()));
        reviewHolder.ratingBar.setRating(review.getRating_point());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder
    {
        TextView txtname,txtcontent,txtpost;
        RatingBar ratingBar;
        public ReviewHolder(@NonNull View itemView) {
            super(itemView);
            txtcontent = itemView.findViewById(R.id.review_text);
            txtname = itemView.findViewById(R.id.review_username);
            txtpost = itemView.findViewById(R.id.review_postin);
            ratingBar = itemView.findViewById(R.id.review_rating);
        }
    }
}
