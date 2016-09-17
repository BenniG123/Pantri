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
public class AddFoodView extends LinearLayout {
    private Context mContext;

    public AddFoodView(Context context, int imageID, String title) {
        super(context, null);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.add_food_adapter, this, true);

        setOrientation(VERTICAL);

        // Set up picView part of ViewGroup
        ImageView picView = (ImageView) getChildAt(0);
        Picasso.with(context)
                .load(imageID)
                .placeholder(R.drawable.ic_menu_camera)
                .resize(500, 500)
                .error(R.drawable.ic_menu_manage)
                .into(picView);

        // Set up name part of ViewGroup
        TextView nameView = (TextView) getChildAt(1);
        nameView.setTextSize(18);
        nameView.setTextColor(Color.BLACK);
        nameView.setTypeface(null, Typeface.BOLD);
        nameView.setText(title);
        nameView.setTextSize(18);
        nameView.setTextColor(Color.BLACK);
    }
}
