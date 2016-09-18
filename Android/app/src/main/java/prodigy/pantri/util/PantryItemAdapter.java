package prodigy.pantri.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import prodigy.pantri.ViewPantryActivity;

/**
 * Created by Ben on 9/17/2016.
 */
public class PantryItemAdapter extends BaseAdapter {

    private Context context;
    private List<Ingredient> ingredients;
    private ViewPantryActivity mViewPantyActivity;

    public PantryItemAdapter(Context c, List<Ingredient> i, ViewPantryActivity viewPantryActivity) {
        context = c;
        ingredients = i;
        mViewPantyActivity = viewPantryActivity;
    }

    @Override
    public int getCount() {
        return ingredients.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return new PantryItemView(context, ingredients.get(position), mViewPantyActivity);
    }
}
