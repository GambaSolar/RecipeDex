package com.example.recipespringandroid.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recipespringandroid.R;
import com.example.recipespringandroid.api.repository.ReviewRepository;
import com.example.recipespringandroid.models.Review;
import com.example.recipespringandroid.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewsActivity extends AppCompatActivity {

    private EditText etComment, etRating;
    private Button btnSend;

    private ReviewRepository reviewRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        etComment = findViewById(R.id.etComment);
        etRating = findViewById(R.id.etRating);
        btnSend = findViewById(R.id.btnSend);

        reviewRepository = new ReviewRepository();

        int recipeId = getIntent().getIntExtra("recipeId", -1);

        if (recipeId == -1) {
            Toast.makeText(this, "Receta inválida", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnSend.setOnClickListener(v -> sendReview(recipeId));
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

        reviewRepository.addReview(userId, recipeId, review)
                .enqueue(new Callback<Review>() {
                    @Override
                    public void onResponse(Call<Review> call, Response<Review> response) {

                        if (response.isSuccessful()) {
                            Toast.makeText(ReviewsActivity.this,
                                    "Review enviada correctamente",
                                    Toast.LENGTH_SHORT).show();
                            finish();
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