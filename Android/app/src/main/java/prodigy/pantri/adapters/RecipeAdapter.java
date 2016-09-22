package prodigy.pantri.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import prodigy.pantri.R;
import prodigy.pantri.activities.RecipeActivity;
import prodigy.pantri.activities.ViewRecipesActivity;
import prodigy.pantri.models.Recipe;

/**
 * Created by Quinn on 9/17/2016.
 */
public class RecipeAdapter extends BaseAdapter {

    private class RecipeHolder {
        public ImageView image;
        public TextView text;
    }

    private Context mContext;
    private List<Recipe> mRecipes;
    private ViewRecipesActivity mViewRecipeActivity;

    public RecipeAdapter(Context c, List<Recipe> recipes, ViewRecipesActivity viewRecipesActivity) {
        mContext = c;
        mRecipes = recipes;
        mViewRecipeActivity = viewRecipesActivity;
    }

    @Override
    public int getCount() {
        return mRecipes.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mViewRecipeActivity.getLayoutInflater().inflate(R.layout.adapter_recipe, null);
            RecipeHolder holder = new RecipeHolder();
            convertView.setTag(holder);
            holder.image = (ImageView) convertView.findViewById(R.id.recipe_image);
            holder.text = (TextView) convertView.findViewById(R.id.info_text);
        }

        RecipeHolder holder = (RecipeHolder) convertView.getTag();
        holder.text.setText(mRecipes.get(position).name);
        holder.image.setImageDrawable(null);

        Picasso.with(mContext)
        .load(mRecipes.get(position).thumbnail)
        .error(R.drawable.ic_info_black_24dp)
        .centerCrop()
        .fit()
        .into(holder.image);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RecipeActivity.class);
                intent.putExtra("recipe", mRecipes.get(position));
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }
}
