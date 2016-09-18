package prodigy.pantri.util;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import prodigy.pantri.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Quinn on 9/17/2016.
 */
public class ServerComms {
    public static boolean login(PantriApplication app, SharedPreferences prefs, String email, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(app.getString(R.string.rest_url))
                .build();

        PantriService service = retrofit.create(PantriService.class);
        String token = null;
        boolean loginSuccess = false;

        try {
            Response<ResponseBody> authResponse = service.getSession(email).execute();
            JSONObject jsonObject = new JSONObject(authResponse.body().string());
            token = jsonObject.getString("token");
            loginSuccess = true;
        } catch (IOException e) {
            loginSuccess = false;
            token = null;
            e.printStackTrace();
        } catch (JSONException e) {
            loginSuccess = false;
            token = null;
            e.printStackTrace();
        }

        // Update auth token
        SharedPreferences privateData = app.getSharedPreferences("private_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = privateData.edit();
        edit.putString("auth", token);
        edit.apply();

        if (loginSuccess) {
            // Update email and password
            edit = prefs.edit();
            edit.putString("pref_name", "John Smith");
            edit.putString("pref_email", email);
            edit.putString("pref_password", password);
            edit.apply();

            return true;
        }

        return loginSuccess;
    }

    public static void logout(PantriApplication app) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(app.getString(R.string.rest_url))
                .build();

        PantriService service = retrofit.create(PantriService.class);

        Call<ResponseBody> authResponse = service.deleteSession("Token " + app.getAuthToken());
        authResponse.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    System.out.println(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        app.setAuthToken(null);
    }
}
