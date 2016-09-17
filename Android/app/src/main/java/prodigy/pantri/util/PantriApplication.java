package prodigy.pantri.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import prodigy.pantri.AddFoodActivity;
import prodigy.pantri.CookActivity;
import prodigy.pantri.LoginActivity;
import prodigy.pantri.MainActivity;
import prodigy.pantri.R;
import prodigy.pantri.RecipeActivity;
import prodigy.pantri.SettingsActivity;
import prodigy.pantri.ShoppingListActivity;
import prodigy.pantri.ViewPantryActivity;

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
        } else if (id == R.id.nav_add_food && !(activity instanceof AddFoodActivity)) {
            ret = new Intent(context, AddFoodActivity.class);
        } else if (id == R.id.nav_view_pantry && !(activity instanceof ViewPantryActivity)) {
            ret = new Intent(context, ViewPantryActivity.class);
        } else if (id == R.id.nav_cook && !(activity instanceof CookActivity)) {
            ret = new Intent(context, CookActivity.class);
        } else if (id == R.id.nav_shopping_list && !(activity instanceof ShoppingListActivity)) {
            ret = new Intent(context, ShoppingListActivity.class);
        } else if (id == R.id.nav_settings && !(activity instanceof SettingsActivity)) {
            ret = new Intent(context, SettingsActivity.class);
        } else if (id == R.id.nav_logout) {
            ((PantriApplication) activity.getApplication()).setAuthToken(null);
            ret = new Intent(context, LoginActivity.class);
        }
        if (ret != null) {
            ret.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        }

        return ret;
    }

    public static void replaceLayout(Activity a, int resource) {
        View C = a.findViewById(R.id.activity_content);
        ViewGroup parent = (ViewGroup) C.getParent();
        int index = parent.indexOfChild(C);
        parent.removeView(C);
        C = a.getLayoutInflater().inflate(resource, parent, false);
        parent.addView(C, index);
    }
}
