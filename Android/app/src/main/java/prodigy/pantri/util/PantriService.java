package prodigy.pantri.util;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Ben on 9/17/2016.
 */
public interface PantriService {
    @GET("pantry")
    Call<List<Ingredient>> listPantry();

    @GET("recipe")
    Call<List<Recipe>> listRecipes();

    @DELETE("pantry/{id}")
    Call<List<Recipe>> deleteIngredient(@Path("id") int ingredientID);
}
