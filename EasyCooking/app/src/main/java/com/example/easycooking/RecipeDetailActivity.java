package com.example.easycooking;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

        // Retrieve data from intent
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

        String[] steps = recipeInstructions.split("\n");
        StringBuilder numberedInstructions = new StringBuilder();
        for (int i = 0; i < steps.length; i++) {
            numberedInstructions.append(i + 1).append(". ").append(steps[i].trim()).append("\n\n");
        }

        recipeInstructionsView.setText(numberedInstructions.toString().trim());

        recipeImage.setImageResource(imageResource);
        recipeTitle.setText(title);
        recipeTime.setText(time);
        recipeCalories.setText(calories);
        recipeAbout.setText(about);
        recipeIngredients.setText(formatIngredients(ingredients));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        LinearLayout moreLinksLayout = findViewById(R.id.more_links);
        addLinkButton(moreLinksLayout, "Tasty", "https://tasty.co/tag/desserts");
        addLinkButton(moreLinksLayout, "All Recipes", "https://www.allrecipes.com/recipes/79/desserts/");
        addLinkButton(moreLinksLayout, "Taste.com.au", "https://www.taste.com.au/baking/galleries/top-50-desserts/rysx9rys?page=3");
        addLinkButton(moreLinksLayout, "BBC Good Food", "https://www.bbcgoodfood.com/recipes/collection/dessert-recipes");
        addLinkButton(moreLinksLayout, "NY Times Cooking", "https://cooking.nytimes.com/topics/desserts");
    }

    private void addLinkButton(LinearLayout layout, String name, String url) {
        TextView linkButton = new TextView(this);
        linkButton.setText(name);
        linkButton.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
        linkButton.setTextSize(16);
        linkButton.setPadding(0, 16, 0, 16);
        linkButton.setGravity(View.TEXT_ALIGNMENT_CENTER);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        linkButton.setLayoutParams(params);

        linkButton.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        });
        layout.addView(linkButton);
    }


    private String formatIngredients(ArrayList<String> ingredients) {
        StringBuilder formattedIngredients = new StringBuilder();
        for (String ingredient : ingredients) {
            formattedIngredients.append("• ").append(ingredient).append("\n");
        }
        return formattedIngredients.toString().trim();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
