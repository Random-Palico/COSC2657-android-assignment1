package com.example.easycooking;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.easycooking.model.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Recipe eggTartRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String userName = getIntent().getStringExtra("USER_NAME");
        if (userName != null && !userName.isEmpty()) {
            Toast toast = Toast.makeText(this, "Welcome " + userName, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 100);
            toast.show();
        }

        // Load data from JSON file
        eggTartRecipe = loadRecipeFromJson();

        if (eggTartRecipe != null) {
            displayTopRecipe(eggTartRecipe);
            findViewById(R.id.top_recipe_card).setOnClickListener(v -> openRecipeDetail(eggTartRecipe));
        } else {
            Toast.makeText(this, "Failed to load recipe data", Toast.LENGTH_SHORT).show();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @SuppressLint("DiscouragedApi")
    private Recipe loadRecipeFromJson() {
        String jsonString = loadJSONFromAsset("recipes.json");
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            JSONObject recipeObject = jsonArray.getJSONObject(0);

            return new Recipe(
                    recipeObject.getString("title"),
                    getResources().getIdentifier(recipeObject.getString("imageResource"), "drawable", getPackageName()),
                    recipeObject.getString("time"),
                    recipeObject.getString("calories"),
                    recipeObject.getString("about"),
                    toList(recipeObject.getJSONArray("ingredients")),
                    recipeObject.getString("recipeInstructions")
            );

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String loadJSONFromAsset(String filename) {
        String json = null;
        try (InputStream is = getAssets().open(filename)) {
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    private ArrayList<String> toList(JSONArray jsonArray) throws JSONException {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonArray.getString(i));
        }
        return list;
    }

    private void displayTopRecipe(Recipe recipe) {
        ImageView recipeImage = findViewById(R.id.top_recipe_image);
        TextView recipeTitle = findViewById(R.id.top_recipe_title);
        TextView recipeDetails = findViewById(R.id.top_recipe_details);

        recipeImage.setImageResource(recipe.getImageResource());
        recipeTitle.setText(recipe.getTitle());
        recipeDetails.setText(recipe.getTime() + " | " + recipe.getCalories());
    }

    private void openRecipeDetail(Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra("title", recipe.getTitle());
        intent.putExtra("imageResource", recipe.getImageResource());
        intent.putExtra("time", recipe.getTime());
        intent.putExtra("calories", recipe.getCalories());
        intent.putExtra("about", recipe.getAbout());
        intent.putStringArrayListExtra("ingredients", new ArrayList<>(recipe.getIngredients()));
        intent.putExtra("recipeInstructions", recipe.getRecipeInstructions());
        startActivity(intent);
    }
}
