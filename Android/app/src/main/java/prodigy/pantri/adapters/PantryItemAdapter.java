package prodigy.pantri.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import prodigy.pantri.activities.ViewPantryActivity;
import prodigy.pantri.models.Ingredient;
import prodigy.pantri.views.PantryItemView;

/**
 * Created by Ben on 9/17/2016.
 */
public class PantryItemAdapter extends BaseAdapter {

    private Context mContext;
    private List<Ingredient> mIngredients;
    private ViewPantryActivity mViewPantryActivity;

    public PantryItemAdapter(Context context, List<Ingredient> ingredients, ViewPantryActivity viewPantryActivity) {
        mContext = context;
        mIngredients = ingredients;
        mViewPantryActivity = viewPantryActivity;
    }

    @Override
    public int getCount() {
        return mIngredients.size();
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
        return new PantryItemView(mContext, mIngredients, position, this, mViewPantryActivity);
    }
}