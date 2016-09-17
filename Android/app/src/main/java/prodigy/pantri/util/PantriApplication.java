package prodigy.pantri.util;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import prodigy.pantri.MainActivity;
import prodigy.pantri.R;

/**
 * Created by Quinn on 9/16/2016.
 */
public class PantriApplication extends Application {
    private String authToken;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public static Intent handleNavDrawer(Context packageContext, MenuItem item) {
        Intent ret = null;
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            ret = new Intent(packageContext, MainActivity.class);
        } else if (id == R.id.nav_add_food) {
            //ret = new Intent(packageContext, AddFoodActivity.class);
        } else if (id == R.id.nav_view_pantry) {
            //ret = new Intent(packageContext, ViewPantryActivity.class);
        } else if (id == R.id.nav_cook) {
            //ret = new Intent(packageContext, CookActivity.class);
        } else if (id == R.id.nav_shopping_list) {
            //ret = new Intent(packageContext, ShoppingListActivity.class);
        } else if (id == R.id.nav_settings) {
            //ret = new Intent(packageContext, SettingsActivity.class);
        }
        return ret;
    }
}
