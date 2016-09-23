package prodigy.pantri.asynctasks;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
public class ViewPantryAsyncTask extends AsyncTask<Object, Void, Boolean> {

    private PantriApplication mApp;
    private PantriCallback<List<Ingredient>> mCallback;

    public ViewPantryAsyncTask(PantriApplication application, PantriCallback<List<Ingredient>> callback) {
        mApp = application;
        mCallback = callback;
    }

    @Override
    protected Boolean doInBackground(Object[] params) {
        List<Ingredient> ingredientList = getPantry();
        mCallback.run(ingredientList);
        return ingredientList == null;
    }

    private List<Ingredient> getPantry() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mApp.getString(R.string.rest_url))
                .build();

        PantriService service = retrofit.create(PantriService.class);

        Call<ResponseBody> authResponse = service.listPantry("Token " + mApp.getAuthToken());
        List<Ingredient> pantryList = null;

        try {
            Response<ResponseBody> response = authResponse.execute();

            if (response.isSuccessful()) {
                String str = response.body().string();

                JSONObject obj = new JSONObject(str);
                JSONArray arr = obj.getJSONArray("ingredients");
                pantryList = new ArrayList<>();

                for (int i = 0; i < arr.length(); i++) {
                    Ingredient tmp = new Ingredient();
                    JSONObject ingredientJSON = (JSONObject) arr.get(i);

                    tmp.id = ingredientJSON.getInt("id");
                    tmp.name = ingredientJSON.getString("name");
                    tmp.quantity = ingredientJSON.getInt("quantity");

                    pantryList.add(tmp);
                }
            }
            else {
                int responseCode = response.raw().code();
                System.out.println("Error - Code " + responseCode);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return pantryList;
    }
}
