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

import prodigy.pantri.util.Recipe;

public class RecipeActivity extends AppCompatActivity {

    Recipe r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        r = (Recipe) getIntent().getSerializableExtra("recipe");

        final ColorStateList darker_gray = fab.getBackgroundTintList();
        final ColorStateList yellow = ColorStateList.valueOf(Color.rgb(255, 180, 0));

        // Set favorite icon
        if (r.isFavorite) {
            fab.setBackgroundTintList(yellow);
        }

        // Create text view
        String recipeInstructions = "<body bgcolor=\"#fafafa\"><p style=\"font-weight:bold;font-size:20\">Ingredients</p>";
        for (String s : r.ingredients) {
            recipeInstructions += s + "<br />";
        }
        recipeInstructions += "<br /><p style=\"font-weight:bold;font-size:20\">Instructions</p>";
        for (String s : r.steps) {
            recipeInstructions += s + "<br />";
        }
        recipeInstructions += "</body>";

        WebView wv = (WebView) findViewById(R.id.id_txt_recipe);
        wv.loadData(recipeInstructions, "text/html", null);

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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (r.isFavorite) {
                    r.isFavorite = false;
                    Snackbar.make(view, "Recipe Un-Favorited", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    view.setBackgroundTintList(darker_gray);
                }
                else {
                    r.isFavorite = true;
                    Snackbar.make(view, "Recipe Favorited!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    view.setBackgroundTintList(yellow);
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
