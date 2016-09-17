package prodigy.pantri.util;

import android.graphics.Color;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.provider.CalendarContract;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;

import prodigy.pantri.R;

/**
 * Created by Quinn on 9/17/2016.
 */
public class RecipeView extends LinearLayout {
    public RecipeView(Context context, Recipe recipe) {
        super(context, null);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.recipe_list_adapter, this, true);

        // Set up picView part of ViewGroup
        ImageView picView = (ImageView) getChildAt(0);
        Picasso.with(context)
                .load(recipe.image.toString())
                .placeholder(R.drawable.ic_menu_camera)
                .error(R.drawable.ic_menu_manage)
                .into(picView);
        picView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        picView.setPadding(20, 20, 20, 20);

        // Set up name part of ViewGroup
        TextView nameView = (TextView) getChildAt(1);
        nameView.setText(recipe.name);
        nameView.setGravity(View.TEXT_ALIGNMENT_CENTER);
        nameView.setTextSize(18);
        nameView.setTextColor(Color.BLACK);

        // Set up favorite part of ViewGroup
        ImageButton favView = (ImageButton) getChildAt(2);
        favView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Toggle dat shit
            }
        });
    }
}
