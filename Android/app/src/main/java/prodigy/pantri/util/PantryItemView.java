package prodigy.pantri.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import prodigy.pantri.R;

/**
 * Created by Ben on 9/17/2016.
 */
public class PantryItemView extends LinearLayout {

    private Ingredient ingredient;

    public PantryItemView(Context context, Ingredient i) {
        super(context, null);
        ingredient = i;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.content_pantry_item, this, true);

        // Set up name part of ViewGroup
        TextView nameView = (TextView) findViewById(R.id.ingredient_name);
        nameView.setTextSize(18);
        nameView.setText(ingredient.name);

    }
}
