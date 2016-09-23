package prodigy.pantri.asynctasks;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import prodigy.pantri.R;
import prodigy.pantri.models.Ingredient;
import prodigy.pantri.util.PantriApplication;
import prodigy.pantri.util.PantriCallback;
import prodigy.pantri.util.PantriService;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Ben on 9/23/2016.
 */

public class AddIngredientAsyncTask extends AsyncTask<Object, Void, Boolean> {

    private int mIngredientID;
    private int mQuantity;
    private PantriCallback<Boolean> mCallback;
    private PantriApplication mApp;
    private String mIngredientName;

    public AddIngredientAsyncTask(PantriApplication application, PantriCallback<Boolean> callback, int ingredientID, int quantity) {
        mApp = application;
        mCallback = callback;
        mQuantity = quantity;
        mIngredientID = ingredientID;
    }

    public AddIngredientAsyncTask(PantriApplication application, PantriCallback<Boolean> callback, int ingredientID) {
        mApp = application;
        mCallback = callback;
        mIngredientID = ingredientID;
        mQuantity = -1;
    }

    public AddIngredientAsyncTask(PantriApplication application, PantriCallback<Boolean> callback, String ingredientName, int quantity) {
        mApp = application;
        mCallback = callback;
        mQuantity = quantity;
        mIngredientName = ingredientName;
    }

    @Override
    protected Boolean doInBackground(Object[] params) {
        boolean result = false;

        if (mIngredientName != null) {
            mIngredientID = getIngredientIdByName();
        }

        if (mIngredientID == -1) {
            return result;
        }

        if (mQuantity > 0) {
             result = addIngredient(mQuantity);
        }
        else {
            result = addIngredient();
        }

        mCallback.run(result);
        return result;
    }

    private int getIngredientIdByName() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mApp.getString(R.string.rest_url))
                .build();

        PantriService service = retrofit.create(PantriService.class);

        Call<ResponseBody> authResponse = service.getIngredientName("Token " + mApp.getAuthToken(), mIngredientName);
        int id = -1;
        try {
            Response<ResponseBody> response = authResponse.execute();
            if (response.isSuccessful()) {
                String str = response.body().string();
                JSONObject obj = new JSONObject(str);
                JSONObject tmp = obj.getJSONObject("ingredient");
                id = tmp.getInt("id");
            }
            else {
                System.out.println(response.errorBody().string());
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return id;
    }

    private boolean addIngredient(int quantity) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mApp.getString(R.string.rest_url))
                .build();

        PantriService service = retrofit.create(PantriService.class);

        Call<ResponseBody> authResponse = service.incIngredient("Token " + mApp.getAuthToken(), mIngredientID, quantity);
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

        Call<ResponseBody> authResponse = service.addIngredient("Token " + mApp.getAuthToken(), mIngredientID);
        try {
            authResponse.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return authResponse.isExecuted();
    }
}
