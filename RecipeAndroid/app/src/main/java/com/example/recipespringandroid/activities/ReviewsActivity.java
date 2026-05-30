package com.example.recipespringandroid.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipespringandroid.R;
import com.example.recipespringandroid.api.ApiClient;
import com.example.recipespringandroid.api.ApiService;
import com.example.recipespringandroid.adapters.ReviewAdapter;
import com.example.recipespringandroid.api.dto.ReviewDTO;
import com.example.recipespringandroid.models.Review;
import com.example.recipespringandroid.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewsActivity extends AppCompatActivity {

    private EditText etComment, etRating;
    private Button btnSend;
    private RecyclerView recyclerReviews;

    private int recipeId;

    private ReviewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        etComment = findViewById(R.id.etComment);
        etRating = findViewById(R.id.etRating);
        btnSend = findViewById(R.id.btnSend);

        recyclerReviews = findViewById(R.id.recyclerReviews);
        recyclerReviews.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ReviewAdapter(new ArrayList<>());
        recyclerReviews.setAdapter(adapter);

        recipeId = getIntent().getIntExtra("recipeId", -1);

        if (recipeId == -1) {
            Toast.makeText(this, "Receta inválida", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadReviews(recipeId);

        btnSend.setOnClickListener(v -> sendReview(recipeId));
    }

    private void loadReviews(int recipeId) {

        ApiService api = ApiClient.getClient().create(ApiService.class);

        api.getReviewsByRecipe(recipeId).enqueue(new Callback<List<ReviewDTO>>() {

            @Override
            public void onResponse(Call<List<ReviewDTO>> call,
                                   Response<List<ReviewDTO>> response) {

                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(ReviewsActivity.this,
                            "Error cargando reviews",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                adapter.updateList(response.body());
            }

            @Override
            public void onFailure(Call<List<ReviewDTO>> call, Throwable t) {
                Toast.makeText(ReviewsActivity.this,
                        "Error de conexión",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendReview(int recipeId) {

        SessionManager sessionManager = new SessionManager(this);
        int userId = sessionManager.getUserId();

        if (userId == -1) {
            Toast.makeText(this, "Debes iniciar sesión", Toast.LENGTH_SHORT).show();
            return;
        }

        String comment = etComment.getText().toString().trim();
        String ratingStr = etRating.getText().toString().trim();

        if (comment.isEmpty() || ratingStr.isEmpty()) {
            Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int rating;
        try {
            rating = Integer.parseInt(ratingStr);
        } catch (Exception e) {
            Toast.makeText(this, "Rating inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        Review review = new Review();
        review.setComment(comment);
        review.setRating(rating);

        ApiClient.getClient()
                .create(ApiService.class)
                .addReview(userId, recipeId, review)
                .enqueue(new Callback<Review>() {

                    @Override
                    public void onResponse(Call<Review> call, Response<Review> response) {

                        if (response.isSuccessful()) {
                            Toast.makeText(ReviewsActivity.this,
                                    "Review enviada correctamente",
                                    Toast.LENGTH_SHORT).show();

                            etComment.setText("");
                            etRating.setText("");

                            loadReviews(recipeId);
                        } else {
                            Toast.makeText(ReviewsActivity.this,
                                    "Error al enviar review",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Review> call, Throwable t) {
                        Toast.makeText(ReviewsActivity.this,
                                "Error de conexión",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}