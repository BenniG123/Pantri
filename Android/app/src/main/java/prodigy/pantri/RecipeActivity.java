package prodigy.pantri;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

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

        final ColorStateList darker_gray = fab.getBackgroundTintList();
        final ColorStateList yellow = ColorStateList.valueOf(Color.rgb(255, 180, 0));

        if (r.isFavorite) {
            fab.setBackgroundTintList(yellow);
        }

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
