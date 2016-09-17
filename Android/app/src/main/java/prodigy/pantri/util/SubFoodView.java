package prodigy.pantri.util;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import prodigy.pantri.R;

/**
 * Created by Ben on 9/17/2016.
 */
public class SubFoodView extends LinearLayout {
    private Context mContext;

    public SubFoodView(Context context, String ingredient) {
        super(context, null);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.sub_food_adapter, this, true);

        // Set up name part of ViewGroup
        TextView nameView = (TextView) getChildAt(0);
        nameView.setText(ingredient);
        nameView.setTextSize(18);
        nameView.setTextColor(Color.BLACK);
    }
}
