package prodigy.pantri.asynctasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import prodigy.pantri.R;
import prodigy.pantri.models.Ingredient;
import prodigy.pantri.models.Recipe;
import prodigy.pantri.models.TaskType;
import prodigy.pantri.util.PantriApplication;
import prodigy.pantri.util.PantriCallback;
import prodigy.pantri.util.PantriService;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Quinn on 9/17/2016.
 */
public class ServerCommsTask<T> extends AsyncTask<Object, Void, Object> {
    private PantriApplication mApp;
    private TaskType mTask;
    private String mParam;
    private int mID;
    private int mQuantity;
    private PantriCallback<T> mCallback;
    private T mCallbackArg;

    public ServerCommsTask(TaskType task, PantriCallback<T> callback, PantriApplication app) {
        this(task, callback, app, null);
    }

    public ServerCommsTask(TaskType task, PantriCallback<T> callback, PantriApplication app, String param) {
        mApp = app;
        mTask = task;
        mParam = param;
        mCallback = callback;
        mCallbackArg = null;
    }

    public ServerCommsTask(TaskType task, PantriCallback<T> callback, PantriApplication app, int id) {
        mApp = app;
        mTask = task;
        mCallback = callback;
        mParam = null;
        mID = id;
    }

    public ServerCommsTask(TaskType task, PantriCallback<T> callback, PantriApplication app, int id, int quantity) {
        mApp = app;
        mTask = task;
        mCallback = callback;
        mParam = null;
        mID = id;
        mQuantity = quantity;
    }

    public ServerCommsTask(TaskType task, PantriCallback<T> callback, PantriApplication app, String param, int quantity) {
        mApp = app;
        mTask = task;
        mCallback = callback;
        mParam = param;
        mQuantity = quantity;
    }
    
    @Override
    protected Boolean doInBackground(Object... voids) {
        switch (mTask) {
            case LOGIN:
                login(mApp, mParam);
                break;
            case LOGOUT:
                logout(mApp);
                break;
            case GET_PANTRY:
                mCallbackArg = (T) getPantry(mApp);
                break;
            case ADD_INGREDIENT:
                Ingredient ingredient = getIngredientByName(mApp, mParam);
                if (ingredient != null) {
                    if (mQuantity > 1) {
                        addIngredient(mApp, ingredient.id, mQuantity);
                    } else {
                        addIngredient(mApp, ingredient.id);
                    }
                }
                mCallbackArg = (T) ingredient;
                break;
            case DEL_INGREDIENT:
                deleteIngredient(mApp, mID);
                break;
            case DEC_INGREDIENT:
                deleteIngredient(mApp, mID, mQuantity);
                break;
            case INC_INGREDIENT:
                addIngredient(mApp, mID, mQuantity);
                break;
            case GET_INGREDIENT_UPC:
                mCallbackArg = (T) getIngredientByUPC(mApp, mParam);
                break;
            case GET_INGREDIENT_NAME:
                mCallbackArg = (T) getIngredientByName(mApp, mParam);
                break;
            case LIST_RECIPES:
                mCallbackArg = (T) getRecipes(mApp);
                break;
        }

        if (mCallback != null) {
            mCallback.run(mCallbackArg);
        }
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

    private void addIngredient(PantriApplication app, int id, int quantity) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(app.getString(R.string.rest_url))
                .build();

        PantriService service = retrofit.create(PantriService.class);

        Call<ResponseBody> authResponse = service.incIngredient("Token " + app.getAuthToken(), id, quantity);
        try {
            authResponse.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteIngredient(PantriApplication app, int id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(app.getString(R.string.rest_url))
                .build();

        PantriService service = retrofit.create(PantriService.class);

        Call<ResponseBody> authResponse = service.deleteIngredient("Token " + app.getAuthToken(), id);
        try {
            authResponse.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteIngredient(PantriApplication app, int id, int quantity) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(app.getString(R.string.rest_url))
                .build();

        PantriService service = retrofit.create(PantriService.class);

        Call<ResponseBody> authResponse = service.decIngredient("Token " + app.getAuthToken(), id, quantity);

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
                    tmp.quantity = ingred.getInt("quantity");

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

