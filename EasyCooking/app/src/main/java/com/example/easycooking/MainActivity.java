package com.example.easycooking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easycooking.model.Recipe;
import com.example.easycooking.model_class.RecipeAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private Recipe eggTartRecipe;
    private EditText searchBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        searchBox = findViewById(R.id.search_box);

        String userName = getIntent().getStringExtra("USER_NAME");
        if (userName != null && !userName.isEmpty()) {
            Toast toast = Toast.makeText(this, "Welcome " + userName, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 100);
            toast.show();
        }

        // Load recipes from JSON
        ArrayList<Recipe> recipes = loadRecipesFromJson();

        if (!recipes.isEmpty()) {
            eggTartRecipe = recipes.get(0);
            displayTopRecipe(eggTartRecipe);
            findViewById(R.id.top_recipe_card).setOnClickListener(v -> openRecipeDetail(eggTartRecipe));

            recipes.remove(0);
        } else {
            Toast.makeText(this, "Failed to load recipe data", Toast.LENGTH_SHORT).show();
        }

        // Set up the recipe list cards under the For You title
        RecyclerView recipeRecyclerView = findViewById(R.id.recipe_recycler_view);
        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecipeAdapter recipeAdapter = new RecipeAdapter(this, recipes);
        recipeRecyclerView.setAdapter(recipeAdapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @SuppressLint("DiscouragedApi")
    private ArrayList<Recipe> loadRecipesFromJson() {
        ArrayList<Recipe> recipes = new ArrayList<>();
        String jsonString = loadJSONFromAsset("recipes.json");
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject recipeObject = jsonArray.getJSONObject(i);

                Recipe recipe = new Recipe(
                        recipeObject.getString("title"),
                        getResources().getIdentifier(recipeObject.getString("imageResource"), "drawable", getPackageName()),
                        recipeObject.getString("time"),
                        recipeObject.getString("calories"),
                        recipeObject.getString("about"),
                        toList(recipeObject.getJSONArray("ingredients")),
                        recipeObject.getString("recipeInstructions"),
                        recipeObject.getString("rating")
                );
                recipes.add(recipe);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recipes;
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
        TextView recipeTime = findViewById(R.id.top_recipe_time);
        TextView recipeCalories = findViewById(R.id.top_recipe_calories);
        TextView recipeRating = findViewById(R.id.top_recipe_rating);

        recipeImage.setImageResource(recipe.getImageResource());
        recipeTitle.setText(recipe.getTitle());
        recipeTime.setText(recipe.getTime());
        recipeCalories.setText(recipe.getCalories());
        recipeRating.setText(recipe.getRating());
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
        intent.putExtra("rating", recipe.getRating());
        startActivity(intent);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (view instanceof EditText) {
                int[] location = new int[2];
                view.getLocationOnScreen(location);
                float x = event.getRawX();
                float y = event.getRawY();

                if (x < location[0] || x > location[0] + view.getWidth() ||
                        y < location[1] || y > location[1] + view.getHeight()) {
                    hideKeyboard(view);
                    view.clearFocus();
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
