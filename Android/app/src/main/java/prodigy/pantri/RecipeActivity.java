package prodigy.pantri;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import prodigy.pantri.util.Ingredient;
import prodigy.pantri.util.Recipe;

public class RecipeActivity extends AppCompatActivity {

    Recipe r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        r = (Recipe) getIntent().getSerializableExtra("recipe");


        // Create text view
        StringBuilder recipeInstructions = new StringBuilder(
                "<!DOCTYPE html>" +
                        "<html>" +
                        "<head>" +
                        "<link href=\"https://fonts.googleapis.com/css?family=Lora\" rel=\"stylesheet\">" +
                        "<style>" +

                            "h1 {" +
                                "font-size: 24px;" +
                                "font-family: 'Lora', serif;" +
                            "}" +

                            "p, li {" +
                                "font-family: 'Lora', serif;" +
                            "}" +
                        "</style></head>" +
                        "<body><h1>Ingredients</h1><ul>"
        );
        for (String s : r.ingredients) {
            recipeInstructions.append("<li>").append(s).append("</li>");
        }
        recipeInstructions.append("</ul>");

        recipeInstructions.append("<h1>Instructions</h1>");
        recipeInstructions.append("<ol>");
        for (String s : r.steps) {
            recipeInstructions.append("<li><p>").append(s).append("</p></li>");
        }
        recipeInstructions.append("</ol></body></html>");

        Log.e("Hey", recipeInstructions.toString());
        WebView wv = (WebView) findViewById(R.id.id_txt_recipe);
        wv.loadData(recipeInstructions.toString(), "text/html", null);

        // Load the image URL
        ImageView recipeImage = (ImageView) findViewById(R.id.img_recipe_view);

        // TODO Figure out how to make the image fill area without changing aspect ratio
        Picasso.with(getApplicationContext())
                .load(r.image)
                .error(R.drawable.ic_info_black_24dp)
                .centerCrop()
                .fit()
                .into(recipeImage);

        setTitle(r.name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
