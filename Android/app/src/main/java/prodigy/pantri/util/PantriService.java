package prodigy.pantri.util;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Ben on 9/17/2016.
 */
public interface PantriService {

    @GET("pantry")
    Call<List<Ingredient>> listPantry(@Header("Authorization") String authorization);

    @GET("recipe")
    Call<List<Recipe>> listRecipes(@Header("Authorization") String authorization);

    @DELETE("pantry/{id}")
    Call<String> deleteIngredient(@Header("Authorization") String authorization, @Path("id") int ingredientID);

    @PUT("pantry/{id}")
    Call<String> addIngredient(@Header("Authorization") String authorization, @Path("id") int ingredientID);

    @GET("ingredient/upc")
    Call<Ingredient> getIngredientUPC(@Header("Authorization") String authorization, @Query("upc") String upc);

    @GET("ingredient/name")
    Call<Ingredient> getIngredientName(@Header("Authorization") String authorization, @Query("name") String name);

}
