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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import prodigy.pantri.AddFoodActivity;
import prodigy.pantri.CookActivity;
import prodigy.pantri.FoodSearchActivity;
import prodigy.pantri.LoginActivity;
import prodigy.pantri.MainActivity;
import prodigy.pantri.R;
import prodigy.pantri.RecipeActivity;
import prodigy.pantri.SettingsActivity;
import prodigy.pantri.ShoppingListActivity;
import prodigy.pantri.ViewPantryActivity;
import retrofit2.Response;
import retrofit2.Retrofit;

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

    public static void replaceLayout(Activity a, int resource) {
        View C = a.findViewById(R.id.activity_content);
        ViewGroup parent = (ViewGroup) C.getParent();
        int index = parent.indexOfChild(C);
        parent.removeView(C);
        C = a.getLayoutInflater().inflate(resource, parent, false);
        parent.addView(C, index);
    }
}
