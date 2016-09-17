package prodigy.pantri.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import prodigy.pantri.R;

/**
 * Created by Quinn on 9/17/2016.
 */
public class RecipeListAdapter extends BaseAdapter {
    private Context mContext;
    private Recipe[] mRecipes;

    public RecipeListAdapter(Context c, Recipe[] recipes) {
        mContext = c;
        mRecipes = recipes;
    }

    public int getCount() {
        return mRecipes.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        return new RecipeView(mContext, mRecipes[position]);
    }
}
