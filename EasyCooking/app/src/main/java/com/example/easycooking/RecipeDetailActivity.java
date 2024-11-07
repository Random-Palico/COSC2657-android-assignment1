package com.example.easycooking;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Retrieve data
        String title = getIntent().getStringExtra("title");
        int imageResource = getIntent().getIntExtra("imageResource", 0);
        String time = getIntent().getStringExtra("time");
        String calories = getIntent().getStringExtra("calories");
        String about = getIntent().getStringExtra("about");
        ArrayList<String> ingredients = getIntent().getStringArrayListExtra("ingredients");
        String recipeInstructions = getIntent().getStringExtra("recipeInstructions");

        ImageView recipeImage = findViewById(R.id.recipe_image);
        TextView recipeTitle = findViewById(R.id.recipe_title);
        TextView recipeTime = findViewById(R.id.recipe_time);
        TextView recipeCalories = findViewById(R.id.recipe_calories);
        TextView recipeAbout = findViewById(R.id.recipe_about);
        TextView recipeIngredients = findViewById(R.id.recipe_ingredients);
        TextView recipeInstructionsView = findViewById(R.id.recipe_instructions);

        recipeImage.setImageResource(imageResource);
        recipeTitle.setText(title);
        recipeTime.setText(time);
        recipeCalories.setText(calories);
        recipeAbout.setText(about);
        recipeIngredients.setText(String.join("\n", ingredients));
        recipeInstructionsView.setText(recipeInstructions);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
