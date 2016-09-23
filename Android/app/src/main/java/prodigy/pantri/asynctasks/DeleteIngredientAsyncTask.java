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

public class DeleteIngredientAsyncTask extends AsyncTask<Object, Void, Boolean> {

    private int mIngredientID;
    private int mQuantity;
    private PantriCallback<Boolean> mCallback;
    private PantriApplication mApp;

    public DeleteIngredientAsyncTask(PantriApplication application, PantriCallback<Boolean> callback, int ingredientID, int quantity) {
        mApp = application;
        mCallback = callback;
        mQuantity = quantity;
        mIngredientID = ingredientID;
    }

    public DeleteIngredientAsyncTask(PantriApplication application, PantriCallback<Boolean> callback, int ingredientID) {
        mApp = application;
        mCallback = callback;
        mIngredientID = ingredientID;
        mQuantity = -1;
    }

    @Override
    protected Boolean doInBackground(Object[] params) {
        boolean result = false;

        if (mQuantity > 0) {
            result = deleteIngredient(mQuantity);
        }
        else {
            result = deleteIngredient();
        }

        mCallback.run(result);
        return result;
    }

    private boolean deleteIngredient(int quantity) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mApp.getString(R.string.rest_url))
                .build();

        PantriService service = retrofit.create(PantriService.class);

        Call<ResponseBody> authResponse = service.decIngredient("Token " + mApp.getAuthToken(), mIngredientID, quantity);

        try {
            authResponse.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return authResponse.isExecuted();
    }

    private boolean deleteIngredient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mApp.getString(R.string.rest_url))
                .build();

        PantriService service = retrofit.create(PantriService.class);

        Call<ResponseBody> authResponse = service.deleteIngredient("Token " + mApp.getAuthToken(), mIngredientID);

        try {
            authResponse.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return authResponse.isExecuted();
    }
}
