package prodigy.pantri.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import prodigy.pantri.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Quinn on 9/17/2016.
 */
public class ServerCommsTask extends AsyncTask<Void, Void, Object> {
    private PantriApplication mApp;
    private TaskType mTask;
    private String mParam;
    public List<Ingredient> pantry = null;
    public List<Recipe> recipes = null;
    public Ingredient ingredient;
    public boolean opDone;

    public ServerCommsTask(TaskType task, PantriApplication app) {
        mApp = app;
        mTask = task;
        pantry = null;
        opDone = false;
        recipes = null;
        ingredient = null;
    }

    public ServerCommsTask(TaskType task, PantriApplication app, String param) {
        mApp = app;
        mTask = task;
        mParam = param;
        pantry = null;
        opDone = false;
        recipes = null;
        ingredient = null;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        switch (mTask) {
            case LOGIN:
                login(mApp, mParam);
                break;
            case LOGOUT:
                logout(mApp);
                break;
            case GET_PANTRY:
                pantry = getPantry(mApp);
                break;
            case ADD_INGREDIENT:
                ingredient = getIngredientByName(mApp, mParam);
                addIngredient(mApp, ingredient.id);
                break;
            case DEL_INGREDIENT:
                break;
            case GET_INGREDIENT_UPC:
                ingredient = getIngredientByUPC(mApp, mParam);
                break;
            case GET_INGREDIENT_NAME:
                ingredient = getIngredientByName(mApp, mParam);
                break;
            case LIST_RECIPES:
                recipes = getRecipes(mApp);
                break;
        }
        opDone = true;

        return true;
    }

    private List<Recipe> getRecipes(PantriApplication app) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(app.getString(R.string.rest_url))
                .build();

        PantriService service = retrofit.create(PantriService.class);

        Call<ResponseBody> authResponse = service.listRecipes("Token " + app.getAuthToken());
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

                    // public List<String> ingredients;
                    // public List<String> steps;

                    tmp.id = recipe.getInt("id");
                    tmp.name = recipe.getString("name");
                    tmp.thumbnail = recipe.getString("thumbnail");
                    tmp.image = recipe.getString("image");

                    recipeList.add(tmp);
                }
            }
            else {
                int responseCode = response.raw().code();
                System.out.println("Error - Code " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return recipeList;
    }

    protected void onPostExecute(Object result) {

    }

    private void addIngredient(PantriApplication app, int id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(app.getString(R.string.rest_url))
                .build();

        PantriService service = retrofit.create(PantriService.class);

        Call<ResponseBody> authResponse = service.addIngredient("Token " + app.getAuthToken(), id);
        try {
            authResponse.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void login(PantriApplication app, String email) {
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
            edit = PreferenceManager.getDefaultSharedPreferences(app).edit();
            edit.putString("pref_name", "John Smith");
            edit.putString("pref_email", email);
            edit.putString("pref_password", "");
            edit.apply();
        }
    }

    private void logout(PantriApplication app) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(app.getString(R.string.rest_url))
                .build();

        PantriService service = retrofit.create(PantriService.class);

        Call<ResponseBody> response = service.deleteSession("Token " + app.getAuthToken());
        try {
            response.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        app.setAuthToken(null);
    }

    private List<Ingredient> getPantry(PantriApplication app) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(app.getString(R.string.rest_url))
                .build();

        PantriService service = retrofit.create(PantriService.class);

        Call<ResponseBody> authResponse = service.listPantry("Token " + app.getAuthToken());
        List<Ingredient> pantryList = null;
        try {
            Response<ResponseBody> response = authResponse.execute();

            if (response.isSuccessful()) {
                String str = response.body().string();

                JSONObject obj = new JSONObject(str);
                JSONArray arr = obj.getJSONArray("ingredients");
                pantryList = new ArrayList<Ingredient>();

                for (int i = 0; i < arr.length(); i++) {
                    Ingredient tmp = new Ingredient();
                    JSONObject ingred = (JSONObject) arr.get(i);

                    tmp.id = ingred.getInt("id");
                    tmp.name = ingred.getString("name");
                    pantryList.add(tmp);
                }
            }
            else {
                int responseCode = response.raw().code();
                System.out.println("Error - Code " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return pantryList;
    }

    private Ingredient getIngredientByUPC(PantriApplication app, String upc) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(app.getString(R.string.rest_url))
                .build();

        PantriService service = retrofit.create(PantriService.class);

        Call<ResponseBody> authResponse = service.getIngredientUPC("Token " + app.getAuthToken(), upc);
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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ret;
    }

    private Ingredient getIngredientByName(PantriApplication app, String name) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(app.getString(R.string.rest_url))
                .build();

        PantriService service = retrofit.create(PantriService.class);

        Call<ResponseBody> authResponse = service.getIngredientName("Token " + app.getAuthToken(), name);
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
            else {
                System.out.println(response.errorBody().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ret;
    }
}

