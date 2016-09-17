package prodigy.pantri;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.Console;

import prodigy.pantri.util.PantriApplication;
import prodigy.pantri.util.PantriService;
import retrofit2.Retrofit;

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
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
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

    @Override
    protected void onResume() {
        super.onResume();

        // TODO - Enter real URL
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://ITouchMyselfAtNight.com")
                .build();

        PantriService service = retrofit.create(PantriService.class);
        // System.out.println(service.listRecipes("Token " + app.getAuthToken()));

        /* SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.getString("pref_name", "");
        prefs.getString("pref_email", "");
        */
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        Intent intent = PantriApplication.handleNavDrawer(this, item);

        if (intent != null) {
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
