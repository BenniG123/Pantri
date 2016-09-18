package prodigy.pantri.util;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;

import prodigy.pantri.R;

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
