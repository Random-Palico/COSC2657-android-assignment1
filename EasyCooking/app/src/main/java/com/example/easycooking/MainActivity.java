package com.example.easycooking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.easycooking.model.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Recipe eggTartRecipe; // declare recipe the recipe as unique
    private EditText searchBox;
    private List<Recipe> allRecipes = new ArrayList<>();
    private List<Recipe> filteredRecipes = new ArrayList<>();
    private TextView topRecipeTitleLabel;
    private View topRecipeCard;
    private TextView forYouTitle;
    private LinearLayout recipeListContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();

        displayWelcomeMessage();

        // Load recipes from JSON file
        allRecipes = loadRecipesFromJson();

        if (!allRecipes.isEmpty()) {
            setupTopRecipe();
            displayRecipes(filteredRecipes);
        } else {
            Toast.makeText(this, "Failed to load recipe data", Toast.LENGTH_SHORT).show();
        }

        setupSearchFunctionality();
    }

    private void initUI() {
        searchBox = findViewById(R.id.search_box);
        topRecipeTitleLabel = findViewById(R.id.top_recipe_title_label);
        topRecipeCard = findViewById(R.id.top_recipe_card);
        forYouTitle = findViewById(R.id.for_you_title);
        recipeListContainer = findViewById(R.id.recipe_card_horizontal);
    }

    private void displayWelcomeMessage() {
        String userName = getIntent().getStringExtra("USER_NAME");
        if (userName != null && !userName.isEmpty()) {
            Toast toast = Toast.makeText(this, "Welcome " + userName, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 100);
            toast.show();
        }
    }

    private void setupTopRecipe() {
        eggTartRecipe = allRecipes.get(0); // Set first recipe as top recipe
        filteredRecipes.addAll(allRecipes);
        filteredRecipes.remove(0); // Remove top recipe from main list (not displayed in For you list)

        displayTopRecipe(eggTartRecipe);
        topRecipeCard.setOnClickListener(v -> openRecipeDetail(eggTartRecipe));
    }

    private void setupSearchFunctionality() {
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterRecipes(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void filterRecipes(String query) {
        filteredRecipes.clear();

        if (query.isEmpty()) {
            filteredRecipes.addAll(allRecipes);
            filteredRecipes.remove(eggTartRecipe);

            topRecipeTitleLabel.setVisibility(View.VISIBLE);
            topRecipeCard.setVisibility(View.VISIBLE);
            forYouTitle.setVisibility(View.VISIBLE);
        } else {
            for (Recipe recipe : allRecipes) {
                if (recipe.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredRecipes.add(recipe);
                }
            }

            topRecipeTitleLabel.setVisibility(View.GONE);
            topRecipeCard.setVisibility(View.GONE);
            forYouTitle.setVisibility(View.GONE);
        }

        displayRecipes(filteredRecipes);
    }

    private void displayRecipes(List<Recipe> recipes) {
        recipeListContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        for (Recipe recipe : recipes) {
            View recipeCard = inflater.inflate(R.layout.recipe_card_horizontal, recipeListContainer, false);

            ImageView recipeImage = recipeCard.findViewById(R.id.recipe_image);
            TextView recipeTitle = recipeCard.findViewById(R.id.recipe_title);
            TextView recipeTime = recipeCard.findViewById(R.id.recipe_time);
            TextView recipeCalories = recipeCard.findViewById(R.id.recipe_calories);
            TextView recipeRating = recipeCard.findViewById(R.id.recipe_rating);

            recipeImage.setImageResource(recipe.getImageResource());
            recipeTitle.setText(recipe.getTitle());
            recipeTime.setText(recipe.getTime());
            recipeCalories.setText(recipe.getCalories());
            recipeRating.setText(recipe.getRating());

            recipeCard.setOnClickListener(v -> openRecipeDetail(recipe));

            recipeListContainer.addView(recipeCard);
        }
    }

    private ArrayList<Recipe> loadRecipesFromJson() {
        ArrayList<Recipe> recipes = new ArrayList<>();
        String jsonString = loadJSONFromAsset("recipes.json");

        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject recipeObject = jsonArray.getJSONObject(i);

                @SuppressLint("DiscouragedApi") Recipe recipe = new Recipe(
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
                hideKeyboardOnTouchOutside(view, event);
            }
        }
        return super.dispatchTouchEvent(event);
    }

    private void hideKeyboardOnTouchOutside(View view, MotionEvent event) {
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

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
