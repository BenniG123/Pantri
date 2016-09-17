package prodigy.pantri;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

        r = new Recipe();
        r.name = "Mom's Spaghetti";
        r.image = "http://i1.kym-cdn.com/photos/images/original/000/359/570/486.png";
        r.steps = new ArrayList<String>();

        final ColorStateList darker_gray = fab.getBackgroundTintList();
        final ColorStateList yellow = ColorStateList.valueOf(Color.rgb(255, 180, 0));

        // Set favorite icon
        if (r.isFavorite) {
            fab.setBackgroundTintList(yellow);
        }

        // Create text view
        String recipeInstructions = "";
        for (String s : r.steps) {
            recipeInstructions += s + "\n";
        }

        TextView t = (TextView) findViewById(R.id.id_txt_recipe);
        t.setText(recipeInstructions);

        // Load the image URL
        ImageView recipeImage = (ImageView) findViewById(R.id.img_recipe_view);
        Picasso.with(getApplicationContext())
                .load(r.image.toString())
                .placeholder(R.drawable.main_grilled_chicken)
                .error(R.drawable.ic_info_black_24dp)
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
