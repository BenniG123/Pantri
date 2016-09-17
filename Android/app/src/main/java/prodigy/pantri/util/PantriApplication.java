package prodigy.pantri.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import prodigy.pantri.LoginActivity;
import prodigy.pantri.MainActivity;
import prodigy.pantri.R;
import prodigy.pantri.SettingsActivity;

/**
 * Created by Quinn on 9/16/2016.
 */
public class PantriApplication extends Application {
    public String getAuthToken() {
        SharedPreferences prefs = getSharedPreferences("private_data", MODE_PRIVATE);
        return prefs.getString("auth", null);
    }

    public void setAuthToken(String authToken) {
        SharedPreferences prefs = getSharedPreferences("private_data", MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("auth", authToken);
        edit.apply();
    }

    public static Intent handleNavDrawer(Activity activity, MenuItem item) {

        Intent ret = null;
        int id = item.getItemId();
        Context context = activity.getApplicationContext();

        if (id == R.id.nav_home && !(activity instanceof MainActivity)) {
            ret = new Intent(context, MainActivity.class);
        } else if (id == R.id.nav_add_food) {
            //ret = new Intent(context, AddFoodActivity.class);
        } else if (id == R.id.nav_view_pantry) {
            //ret = new Intent(context, ViewPantryActivity.class);
        } else if (id == R.id.nav_cook) {
            //ret = new Intent(context, CookActivity.class);
        } else if (id == R.id.nav_shopping_list) {
            //ret = new Intent(context, ShoppingListActivity.class);
        } else if (id == R.id.nav_settings && !(activity instanceof SettingsActivity)) {
            ret = new Intent(context, SettingsActivity.class);
        } else if (id == R.id.nav_logout) {
            ((PantriApplication) activity.getApplication()).setAuthToken(null);
            ret = new Intent(context, LoginActivity.class);
        }

        return ret;
    }
}
