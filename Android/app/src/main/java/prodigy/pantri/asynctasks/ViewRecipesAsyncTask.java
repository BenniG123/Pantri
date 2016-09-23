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
import prodigy.pantri.models.Recipe;
import prodigy.pantri.util.PantriApplication;
import prodigy.pantri.util.PantriCallback;
import prodigy.pantri.util.PantriService;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Ben on 9/23/2016.
 */

public class ViewRecipesAsyncTask extends AsyncTask<Object, Void, Boolean> {
    private PantriApplication mApp;
    private PantriCallback<List<Recipe>> mCallback;

    public ViewRecipesAsyncTask(PantriApplication application, PantriCallback<List<Recipe>> callback) {
        mApp = application;
        mCallback = callback;
    }

    @Override
    protected Boolean doInBackground(Object[] params) {
        List<Recipe> recipeList = getRecipes();
        mCallback.run(recipeList);
        return recipeList != null;
    }

    private List<Recipe> getRecipes() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mApp.getString(R.string.rest_url))
                .build();

        PantriService service = retrofit.create(PantriService.class);

        Call<ResponseBody> authResponse = service.listRecipes("Token " + mApp.getAuthToken());
        List<Recipe> recipeList = new ArrayList<Recipe>();;

        try {
            Response<ResponseBody> response = authResponse.execute();
            if (response.isSuccessful()) {
                String str = response.body().string();
                JSONObject obj = new JSONObject(str);
                JSONArray arr = obj.getJSONArray("recipes");

                for (int i = 0; i < arr.length(); i++) {
                    Recipe tmp = new Recipe();
                    JSONObject recipe = (JSONObject) arr.get(i);

                    tmp.id = recipe.getInt("id");
                    tmp.name = recipe.getString("name");
                    tmp.thumbnail = recipe.getString("thumbnail");
                    tmp.image = recipe.getString("image");

                    tmp.ingredients = new ArrayList<>();
                    JSONArray jsonIngredients = recipe.getJSONArray("ingredients");

                    for (int j = 0; j < jsonIngredients.length(); j++) {
                        tmp.ingredients.add(jsonIngredients.getString(j));
                    }

                    tmp.steps = new ArrayList<>();
                    JSONArray jsonSteps = recipe.getJSONArray("steps");

                    for (int j = 0; j < jsonSteps.length(); j++) {
                        tmp.steps.add(jsonSteps.getString(j));
                    }

                    recipeList.add(tmp);
                }
            }
            else {
                int responseCode = response.raw().code();
                System.out.println("Error - Code " + responseCode);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return recipeList;
    }
}
