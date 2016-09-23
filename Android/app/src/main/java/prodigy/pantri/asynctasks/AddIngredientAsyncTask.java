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

public class AddIngredientAsyncTask extends AsyncTask<Object, Void, Boolean> {

    private int mRecipeID;
    private int mQuantity;
    private PantriCallback<Boolean> mCallback;
    private PantriApplication mApp;

    public AddIngredientAsyncTask(PantriApplication application, PantriCallback<Boolean> callback, int recipeID, int quantity) {
        mApp = application;
        mCallback = callback;
        mQuantity = quantity;
        mRecipeID = recipeID;
    }

    public AddIngredientAsyncTask(PantriApplication application, PantriCallback<Boolean> callback, int recipeID) {
        mApp = application;
        mCallback = callback;
        mRecipeID = recipeID;
        mQuantity = -1;
    }

    @Override
    protected Boolean doInBackground(Object[] params) {
        boolean result = false;
        if (mQuantity > 0) {
             result = addIngredient(mQuantity);
        }
        else {
            result = addIngredient();
        }

        mCallback.run(result);
        return result;
    }

    private boolean addIngredient(int quantity) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mApp.getString(R.string.rest_url))
                .build();

        PantriService service = retrofit.create(PantriService.class);

        Call<ResponseBody> authResponse = service.incIngredient("Token " + mApp.getAuthToken(), mRecipeID, quantity);
        try {
            authResponse.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return authResponse.isExecuted();
    }

    private boolean addIngredient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mApp.getString(R.string.rest_url))
                .build();

        PantriService service = retrofit.create(PantriService.class);

        Call<ResponseBody> authResponse = service.addIngredient("Token " + mApp.getAuthToken(), mRecipeID);
        try {
            authResponse.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return authResponse.isExecuted();
    }
}
