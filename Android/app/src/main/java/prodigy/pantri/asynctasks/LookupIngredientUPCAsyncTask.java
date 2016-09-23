package prodigy.pantri.asynctasks;

import android.os.AsyncTask;
import android.os.Handler;

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
public class LookupIngredientUPCAsyncTask extends AsyncTask<Object, Void, Boolean>  {

    private PantriApplication mApp;
    private String mUPC;
    private PantriCallback<Ingredient> mCallback;

    public LookupIngredientUPCAsyncTask(PantriApplication application, String upc, PantriCallback<Ingredient> callback) {
        mApp = application;
        mUPC = upc;
        mCallback = callback;
    }

    @Override
    protected Boolean doInBackground(Object[] params) {
        Ingredient ingredient = getIngredientByUPC();
        mCallback.run(ingredient);
        return ingredient != null;
    }

    private Ingredient getIngredientByUPC() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mApp.getString(R.string.rest_url))
                .build();

        PantriService service = retrofit.create(PantriService.class);

        Call<ResponseBody> authResponse = service.getIngredientUPC("Token " + mApp.getAuthToken(), mUPC);
        Ingredient ret = null;

        try {
            Response<ResponseBody> response = authResponse.execute();
            if (response.isSuccessful()) {
                ret = new Ingredient();
                String str = response.body().string();
                JSONObject obj = new JSONObject(str);
                JSONObject tmp = obj.getJSONObject("ingredient");
                ret.id = tmp.getInt("id");
                ret.name = tmp.getString("name");
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return ret;
    }
}
