package prodigy.pantri.views;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import prodigy.pantri.R;
import prodigy.pantri.activities.ViewPantryActivity;
import prodigy.pantri.asynctasks.AddIngredientAsyncTask;
import prodigy.pantri.models.Ingredient;
import prodigy.pantri.models.TaskType;
import prodigy.pantri.util.PantriApplication;
import prodigy.pantri.asynctasks.ServerCommsTask;
import prodigy.pantri.util.PantriCallback;

/**
 * Created by Ben on 9/17/2016.
 */
public class PantryItemView extends LinearLayout implements Runnable {

    private Ingredient mIngredient;
    private Context mContext;
    private Handler mHandler;
    private PantryItemView mRunnable;
    private AddIngredientAsyncTask mAddTask;
    private ServerCommsTask mTask;
    private ViewPantryActivity mViewPantryActivity;

    public PantryItemView(Context context, Ingredient i, ViewPantryActivity viewPantryActivity) {
        super(context, null);
        mIngredient = i;
        mContext = context;
        mRunnable = this;
        mViewPantryActivity = viewPantryActivity;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.adapter_pantry_item, this, true);

        // Set up name part of ViewGroup
        TextView nameView = (TextView) findViewById(R.id.ingredient_name);
        nameView.setTextSize(18);
        nameView.setText(mIngredient.name);
        nameView.setTextColor(Color.BLACK);

        TextView quantityView = (TextView) findViewById(R.id.txt_quantity);
        quantityView.setTextSize(18);
        quantityView.setTextColor(Color.BLACK);
        quantityView.setText(Integer.toString(mIngredient.quantity));

        Button decrementButton = (Button) findViewById(R.id.btn_minus);
        decrementButton.setOnClickListener(new OnClickListener() {
               @Override
               public void onClick(View v) {
                   mTask = new ServerCommsTask<>(TaskType.DEC_INGREDIENT, null, (PantriApplication) mContext, mIngredient.id, 1);
                   mTask.execute();

                   mHandler = new Handler();
                   mHandler.post(mRunnable);
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

    @Override
    public void run() {
        // Refresh the page
        mViewPantryActivity.refresh();
    }
}
