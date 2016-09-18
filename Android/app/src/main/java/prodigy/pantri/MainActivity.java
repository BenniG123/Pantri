package prodigy.pantri;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.LayoutInflaterCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.Console;
import java.util.List;

import prodigy.pantri.util.PantriApplication;
import prodigy.pantri.util.PantriService;
import prodigy.pantri.util.Recipe;
import prodigy.pantri.util.ServerCommsTask;
import prodigy.pantri.util.TaskType;
import retrofit2.Retrofit;

public class MainActivity extends PantriBaseActivity {
    private ServerCommsTask mTask;
    private LinearLayout mRecipeScroller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app.replaceLayout(this, R.layout.content_main);

        mTask = new ServerCommsTask(TaskType.LIST_RECIPES, (PantriApplication) getApplication(), null, new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateRecipes();
                    }
                });
            }
        });
        mTask.execute();

        mRecipeScroller = (LinearLayout) findViewById(R.id.recipe_card_view);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View card = inflater.inflate(R.layout.horizontal_recipe_adapter, null);
        mRecipeScroller.addView(card);
    }

    private void updateRecipes() {
        if (mTask.recipes.isEmpty()) {
            TextView text = (TextView) mRecipeScroller.getChildAt(0).findViewById(R.id.info_text);
            text.setText("No recipes found");
            return;
        }
        mRecipeScroller.removeAllViews();
        for (Recipe r : mTask.recipes) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View card = inflater.inflate(R.layout.horizontal_recipe_adapter, null);
            TextView text = (TextView) card.findViewById(R.id.info_text);
            ImageView image = (ImageView) card.findViewById(R.id.recipe_image);
            text.setText(r.name);
            Picasso.with(getApplicationContext())
                    .load(r.thumbnail)
                    .error(R.drawable.ic_info_black_24dp)
                    .centerCrop()
                    .fit()
                    .into(image);
            mRecipeScroller.addView(card);
        }
    }
}
