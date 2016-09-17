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

        View C = findViewById(R.id.activity_content);
        ViewGroup parent = (ViewGroup) C.getParent();
        int index = parent.indexOfChild(C);
        parent.removeView(C);
        C = getLayoutInflater().inflate(R.layout.content_main, parent, false);
        parent.addView(C, index);
    }
}
