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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Console;

import prodigy.pantri.util.PantriApplication;
import prodigy.pantri.util.PantriService;
import retrofit2.Retrofit;

public class MainActivity extends PantriBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app.replaceLayout(this, R.layout.content_main);

        LinearLayout recipeScroller = (LinearLayout) findViewById(R.id.recipe_card_view);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < 5; i++) {
            View card = inflater.inflate(R.layout.horizontal_recipe_adapter, null);
            TextView text = (TextView) card.findViewById(R.id.info_text);
            text.setText("Old Fashion Pumpkin Pie");
            recipeScroller.addView(card);
        }
    }
}
