package com.example.easycooking.model_class;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.easycooking.R;
import com.example.easycooking.RecipeDetailActivity;
import com.example.easycooking.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private Context context;
    private List<Recipe> recipes;

    public RecipeAdapter(Context context, List<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recipe_card_horizontal, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.recipeTitle.setText(recipe.getTitle());
        holder.recipeTime.setText(recipe.getTime());
        holder.recipeCalories.setText(recipe.getCalories());
        holder.recipeRating.setText(recipe.getRating());
        holder.recipeImage.setImageResource(recipe.getImageResource());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, RecipeDetailActivity.class);
            intent.putExtra("title", recipe.getTitle());
            intent.putExtra("imageResource", recipe.getImageResource());
            intent.putExtra("time", recipe.getTime());
            intent.putExtra("calories", recipe.getCalories());
            intent.putExtra("about", recipe.getAbout());
            intent.putStringArrayListExtra("ingredients", new ArrayList<>(recipe.getIngredients()));
            intent.putExtra("recipeInstructions", recipe.getRecipeInstructions());
            intent.putExtra("rating", recipe.getRating());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        ImageView recipeImage;
        TextView recipeTitle, recipeTime, recipeCalories, recipeRating;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeImage = itemView.findViewById(R.id.recipe_image);
            recipeTitle = itemView.findViewById(R.id.recipe_title);
            recipeTime = itemView.findViewById(R.id.recipe_time);
            recipeCalories = itemView.findViewById(R.id.recipe_calories);
            recipeRating = itemView.findViewById(R.id.recipe_rating);
        }
    }
}
