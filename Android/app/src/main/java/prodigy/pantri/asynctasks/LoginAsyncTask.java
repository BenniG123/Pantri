package prodigy.pantri.asynctasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import prodigy.pantri.R;
import prodigy.pantri.util.PantriApplication;
import prodigy.pantri.util.PantriCallback;
import prodigy.pantri.util.PantriService;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Ben on 9/23/2016.
 */

public class LoginAsyncTask extends AsyncTask<Object, Void, Object> {

    private PantriApplication mApp;
    private String mUserName;
    private PantriCallback<Boolean> mCallback;

    public LoginAsyncTask(PantriApplication application, String userName, PantriCallback<Boolean> callback) {
        mApp = application;
        mUserName = userName;
        mCallback = callback;
    }

    @Override
    protected Boolean doInBackground(Object... voids) {
        boolean success = login(mApp, mUserName);
        mCallback.run(success);
        return success;
    }

    private boolean login(PantriApplication app, String email) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(app.getString(R.string.rest_url))
                .build();

        PantriService service = retrofit.create(PantriService.class);
        String token = null;
        boolean loginSuccess = false;

        try {
            Response<ResponseBody> response = service.getSession(email).execute();
            if (response.isSuccessful()) {
                JSONObject jsonObject = new JSONObject(response.body().string());
                token = jsonObject.getString("token");
                loginSuccess = true;
            }
        } catch (IOException | JSONException e) {
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
            edit = PreferenceManager.getDefaultSharedPreferences(app).edit();
            edit.putString("pref_name", "John Smith");
            edit.putString("pref_email", email);
            edit.putString("pref_password", "");
            edit.apply();
            return true;
        }
        else {
            return false;
        }
    }
}
