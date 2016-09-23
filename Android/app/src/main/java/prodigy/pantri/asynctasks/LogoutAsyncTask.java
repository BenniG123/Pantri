package prodigy.pantri.asynctasks;

import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.ResponseBody;
import prodigy.pantri.R;
import prodigy.pantri.util.PantriApplication;
import prodigy.pantri.util.PantriCallback;
import prodigy.pantri.util.PantriService;
import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by Ben on 9/23/2016.
 */

public class LogoutAsyncTask extends AsyncTask {

    private PantriApplication mApp;
    private PantriCallback<Boolean> mCallback;

    public LogoutAsyncTask(PantriApplication application, PantriCallback<Boolean> callback) {
        mApp = application;
        mCallback = callback;
    }

    @Override
    protected Boolean doInBackground(Object... voids) {
        boolean success = logout(mApp);
        mCallback.run(success);
        return success;
    }

    private boolean logout(PantriApplication app) {

        boolean success = false;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(app.getString(R.string.rest_url))
                .build();

        PantriService service = retrofit.create(PantriService.class);

        Call<ResponseBody> response = service.deleteSession("Token " + app.getAuthToken());

        try {
            response.execute();
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        app.setAuthToken(null);
        return success;
    }
}
