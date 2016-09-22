package prodigy.pantri.util;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Ben on 9/17/2016.
 */
public interface PantriService {

    @GET("pantry")
    Call<ResponseBody> listPantry(@Header("Authorization") String authorization);

    @FormUrlEncoded
    @POST("session")
    Call<ResponseBody> getSession(@Field("email") String email);

    @DELETE("session")
    Call<ResponseBody> deleteSession(@Header("Authorization") String authorization);

    @GET("recipe/")
    Call<ResponseBody> listRecipes(@Header("Authorization") String authorization);

    @DELETE("pantry/{id}")
    Call<ResponseBody> deleteIngredient(@Header("Authorization") String authorization, @Path("id") int ingredientID);

    @DELETE("pantry/{id}")
    Call<ResponseBody> decIngredient(@Header("Authorization") String authorization, @Path("id") int ingredientID, @Query("quantity") int quantity);

    @PUT("pantry/{id}")
    Call<ResponseBody> addIngredient(@Header("Authorization") String authorization, @Path("id") int ingredientID);

    @PUT("pantry/{id}")
    Call<ResponseBody> incIngredient(@Header("Authorization") String authorization, @Path("id") int ingredientID, @Query("quantity") int quantity);

    @GET("ingredient/upc/{upc}")
    Call<ResponseBody> getIngredientUPC(@Header("Authorization") String authorization, @Path("upc") String upc);

    @GET("ingredient/name/{name}")
    Call<ResponseBody> getIngredientName(@Header("Authorization") String authorization, @Path("name") String name);

}
