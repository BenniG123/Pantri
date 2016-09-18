package prodigy.pantri;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import prodigy.pantri.util.PantriApplication;
import prodigy.pantri.util.PantriDrawerToggle;
import prodigy.pantri.util.ServerCommsTask;
import prodigy.pantri.util.TaskType;

public class PantriBaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected PantriApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app = (PantriApplication) getApplication();
        if (app.getAuthToken() == null) { // TODO Add check to server to see if token is still valid
            startActivity(new Intent(this, LoginActivity.class));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        PantriDrawerToggle toggle = new PantriDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Set name and email in nav drawer
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        TextView nav_name = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_name);
        TextView nav_email = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_email);
        nav_name.setText(PreferenceManager.getDefaultSharedPreferences(this).getString("pref_name", ""));
        nav_email.setText(PreferenceManager.getDefaultSharedPreferences(this).getString("pref_email", ""));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Intent ret = null;
        int id = item.getItemId();
        Context context = getApplicationContext();

       if (id == R.id.nav_add_food && !(this instanceof FoodSearchActivity)) {
            ret = new Intent(context, FoodSearchActivity.class);
        } else if (id == R.id.nav_view_pantry && !(this instanceof ViewPantryActivity)) {
            ret = new Intent(context, ViewPantryActivity.class);
        } else if (id == R.id.nav_recipes && !(this instanceof ViewRecipesActivity)) {
            ret = new Intent(context, ViewRecipesActivity.class);
        } else if (id == R.id.nav_settings) {
            ret = new Intent(context, SettingsActivity.class);
        } else if (id == R.id.nav_logout) {
            ServerCommsTask task = new ServerCommsTask<>(TaskType.LOGOUT, null, app);
            task.execute();
            ret = new Intent(context, LoginActivity.class);
        }

        if (ret != null) {
            ret.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        }

        if (ret != null) {
            startActivity(ret);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
