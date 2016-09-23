package prodigy.pantri.views;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import prodigy.pantri.R;
import prodigy.pantri.activities.ViewPantryActivity;
import prodigy.pantri.adapters.PantryItemAdapter;
import prodigy.pantri.asynctasks.AddIngredientAsyncTask;
import prodigy.pantri.asynctasks.DeleteIngredientAsyncTask;
import prodigy.pantri.models.Ingredient;
import prodigy.pantri.util.PantriApplication;
import prodigy.pantri.util.PantriCallback;

/**
 * Created by Ben on 9/17/2016.
 */
public class PantryItemView extends LinearLayout {

    private Ingredient mIngredient;
    private int mPosition;
    private List<Ingredient> mIngredientList;
    private Context mContext;
    private AddIngredientAsyncTask mAddTask;
    private DeleteIngredientAsyncTask mDeleteTask;
    private ViewPantryActivity mViewPantryActivity;
    private PantryItemAdapter mAdapter;

    public PantryItemView(Context context, List<Ingredient> ingredientList, int position, PantryItemAdapter adapter, ViewPantryActivity viewPantryActivity) {
        super(context, null);
        mIngredient = ingredientList.get(position);
        mPosition = position;
        mIngredientList = ingredientList;
        mContext = context;
        mViewPantryActivity = viewPantryActivity;
        mAdapter = adapter;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.adapter_pantry_item, this, true);

        // Set up name part of ViewGroup
        TextView nameView = (TextView) findViewById(R.id.ingredient_name);
        nameView.setTextSize(18);
        nameView.setText(mIngredient.name);
        nameView.setTextColor(Color.BLACK);

        final TextView quantityView = (TextView) findViewById(R.id.txt_quantity);
        quantityView.setTextSize(18);
        quantityView.setTextColor(Color.BLACK);
        quantityView.setText(Integer.toString(mIngredient.quantity));

        Button decrementButton = (Button) findViewById(R.id.btn_minus);
        decrementButton.setOnClickListener(new OnClickListener() {
               @Override
               public void onClick(View v) {
                   mDeleteTask = new DeleteIngredientAsyncTask((PantriApplication) mContext, new PantriCallback<Boolean>() {
                       @Override
                       public void run(Boolean isAdded) {
                           if (isAdded) {
                               mIngredient.quantity--;
                               // Increment Client Quantity
                               mViewPantryActivity.runOnUiThread(new Runnable() {
                                   @Override
                                   public void run() {
                                       if (mIngredient.quantity > 0) {
                                           TextView quantityView = (TextView) findViewById(R.id.txt_quantity);
                                           quantityView.setText(Integer.toString(mIngredient.quantity));
                                       }
                                       else {
                                           // Delete this view
                                           mIngredientList.remove(mPosition);
                                           mAdapter.notifyDataSetChanged();
                                       }
                                   }
                               });
                           }
                       }
                   }, mIngredient.id, 1);
                   mDeleteTask.execute();
               }
           }
        );

        Button incrementButton = (Button) findViewById(R.id.btn_plus);
        incrementButton.setOnClickListener(new OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
               mAddTask = new AddIngredientAsyncTask((PantriApplication) mContext, new PantriCallback<Boolean>() {
                   @Override
                   public void run(Boolean isAdded) {
                       if (isAdded) {
                           mIngredient.quantity++;
                           // Increment Client Quantity
                           mViewPantryActivity.runOnUiThread(new Runnable() {
                               @Override
                               public void run() {
                                   TextView quantityView = (TextView) findViewById(R.id.txt_quantity);
                                   quantityView.setText(Integer.toString(mIngredient.quantity));
                               }
                           });
                       }
                   }
               }, mIngredient.id);
               mAddTask.execute();
           }
       }
        );

    }
}
