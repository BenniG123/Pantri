package prodigy.pantri.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import prodigy.pantri.R;
import prodigy.pantri.ViewPantryActivity;

/**
 * Created by Ben on 9/17/2016.
 */
public class PantryItemView extends LinearLayout implements Runnable {

    private Ingredient ingredient;
    private Context mContext;
    private Handler mHandler;
    private PantryItemView mRunnable;
    private ServerCommsTask mTask;
    private ViewPantryActivity mViewPantryActivity;

    public PantryItemView(Context context, Ingredient i, ViewPantryActivity viewPantryActivity) {
        super(context, null);
        ingredient = i;
        mContext = context;
        mRunnable = this;
        mViewPantryActivity = viewPantryActivity;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.content_pantry_item, this, true);

        // Set up name part of ViewGroup
        TextView nameView = (TextView) findViewById(R.id.ingredient_name);
        nameView.setTextSize(18);
        nameView.setText(ingredient.name);
        nameView.setTextColor(Color.BLACK);

        Button decrementButton = (Button) findViewById(R.id.btn_minus);
        decrementButton.setOnClickListener(new OnClickListener() {
               @Override
               public void onClick(View v) {
                   // Get list of ingredients
                   mTask = new ServerCommsTask(TaskType.DEL_INGREDIENT, (PantriApplication) mContext, ingredient.id);
                   mTask.execute();

                   mHandler = new Handler();
                   mHandler.post(mRunnable);
               }
           }
        );

    }

    @Override
    public void run() {
        while (!mTask.opDone);

        // Refresh the page
        mViewPantryActivity.refresh();
    }

}
