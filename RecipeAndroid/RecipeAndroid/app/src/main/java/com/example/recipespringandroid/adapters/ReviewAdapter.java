package com.example.recipespringandroid.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipespringandroid.R;
import com.example.recipespringandroid.api.dto.ReviewDTO;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private final List<ReviewDTO> reviewList;

    public ReviewAdapter(List<ReviewDTO> reviewList) {
        this.reviewList = reviewList != null ? reviewList : new ArrayList<>();
    }

    public void updateList(List<ReviewDTO> newList) {
        reviewList.clear();
        if (newList != null) {
            reviewList.addAll(newList);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);

        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {

        ReviewDTO review = reviewList.get(position);

        if (review.getUser() != null && review.getUser().getUsername() != null) {
            holder.tvUsername.setText(review.getUser().getUsername());
        } else {
            holder.tvUsername.setText("Usuario");
        }

        holder.tvRating.setText("⭐ " + review.getRating());
        holder.tvComment.setText(review.getComment() != null ? review.getComment() : "");
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {

        TextView tvUsername, tvRating, tvComment;
        MaterialCardView card;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvComment = itemView.findViewById(R.id.tvComment);
            card = itemView.findViewById(R.id.cardReview);
        }
    }
}