package com.example.easycooking;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.easycooking.model.Recipe;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private Recipe eggTartRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Display welcome message
        String userName = getIntent().getStringExtra("USER_NAME");
        if (userName != null && !userName.isEmpty()) {
            Toast toast = Toast.makeText(this, "Welcome " + userName, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 100);
            toast.show();
        }

        // Hardcoded data
        eggTartRecipe = new Recipe(
                "Egg Tart",
                R.drawable.egg_tart, 
                "1 hr 30 min",
                "210 calories / serving",
                "One of the most famous Chinese desserts is the classic egg tart, a delicious pastry consisting of a flaky outer shell with a creamy, but firm egg custard in the center. The origin of this traditional Chinese dessert is vague.",
                Arrays.asList(
                        "Custard Filling:",
                        "4 eggs (reserve 2 tablespoons for pastry dough)",
                        "180 mL hot water",
                        "6 tablespoons (~75 g) sugar",
                        "⅛ teaspoon salt",
                        "60 mL evaporated milk",
                        "A dash of vanilla extract (optional)",
                        "",
                        "Pastry Dough:",
                        "200 g cake flour (plus extra for dusting)",
                        "115 g unsalted butter (at room temperature)",
                        "40 g powdered sugar",
                        "2 tablespoons beaten egg (from the custard filling eggs)",
                        "⅛ teaspoon salt",
                        "A dash of vanilla extract (optional)"
                ),
                "For the pastry, in a large bowl, sift flour, sugar, and salt. Then add softened butter. Bring the mixture together with your hands, careful not to knead the pastry dough too much or you will make the pastry tough. \n Whisk the egg yolks and add the 2 tablespoons of beaten yolk to the flour mixture. Bring together until smooth. If the dough is too sticky, coating your hands with flour will help. Cover with plastic wrap and then refrigerate for 30 minutes, or until the dough is firm. \n To make the custard filling, melt sugar and salt with hot water. Mix until dissolved then let cool. \n Add the rest of the beaten egg yolk. Stir in sugar water and also evaporated milk (if adding vanilla, add now). Stir and combine everything well. \n Strain the filling to ensure no lumps. Chill in the refrigerator. \n Preheat the oven to 400˚F (200˚C). \n Take the dough out and divide into 16 equal portions. Spray the tart pan with a light coating of oil. Take one portion of your dough and roll it into a ball and place in your tart shell. Press the shell into the pan with your fingers. Try to make the wrapper uniform in thickness and avoid a thick bottom. Repeat to finish all. \n Pour the custard filling into the shells until it is about 80% full. Bake for 15 to 20 minutes until the surface becomes golden brown and a toothpick can stand in the egg tart. \n Cool down for several minutes and then take the egg tarts out of the pan. Serve while still warm."
        );

        displayTopRecipe(eggTartRecipe);

        findViewById(R.id.top_recipe_card).setOnClickListener(v -> openRecipeDetail(eggTartRecipe));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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
