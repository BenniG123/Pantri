package prodigy.pantri;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Handler;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import prodigy.pantri.util.PantriApplication;
import prodigy.pantri.util.PantriCallback;
import prodigy.pantri.util.Recipe;
import prodigy.pantri.util.RecipeListAdapter;
import prodigy.pantri.util.ServerCommsTask;
import prodigy.pantri.util.TaskType;

public class ViewRecipesActivity extends PantriBaseActivity implements PantriCallback<List<Recipe>> {
    private ListView listView;
    private List<Recipe> mRecipeList;
    private RecipeListAdapter listAdapter;
    private ServerCommsTask mTask;
    private GridView mGrid;
    private TextView mPlaceholder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app.replaceLayout(this, R.layout.content_view_recipes);
        setTitle("View Recipes");
        refresh();
    }

    public void refresh() {
        mTask = new ServerCommsTask<>(TaskType.LIST_RECIPES, this, (PantriApplication) getApplication());
        mGrid = (GridView) findViewById(R.id.recipe_grid);
        mPlaceholder = (TextView) findViewById(R.id.placeholder);
        mTask.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mRecipeList == null || mRecipeList.size() == 0) {
            refresh();
        }
    }

    @Override
    public void run(final List<Recipe> recipes) {
        mRecipeList = recipes;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mRecipeList.size() == 0) {
                    mGrid.setVisibility(View.GONE);
                    mPlaceholder.setVisibility(View.VISIBLE);
                } else {
                    mGrid.setVisibility(View.VISIBLE);
                    mPlaceholder.setVisibility(View.GONE);
                }

                mGrid.setAdapter(new BaseAdapter() {
                    @Override
                    public int getCount() {
                        return mRecipeList.size();
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
                            convertView = getLayoutInflater().inflate(R.layout.recipe_adapter, null);
                            RecipeHolder holder = new RecipeHolder();
                            convertView.setTag(holder);
                            holder.image = (ImageView) convertView.findViewById(R.id.recipe_image);
                            holder.text = (TextView) convertView.findViewById(R.id.info_text);
                        }

                        RecipeHolder holder = (RecipeHolder) convertView.getTag();
                        holder.text.setText(mRecipeList.get(position).name);
                        holder.image.setImageDrawable(null);
                        Picasso.with(getApplicationContext())
                            .load(mRecipeList.get(position).thumbnail)
                            .error(R.drawable.ic_info_black_24dp)
                            .centerCrop()
                            .fit()
                            .into(holder.image);

                        convertView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), RecipeActivity.class);
                                intent.putExtra("recipe", mRecipeList.get(position));
                                startActivity(intent);
                            }
                        });
                        return convertView;
                    }
                });
            }
        });
    }

    private class RecipeHolder {
        public ImageView image;
        public TextView text;
    }
}
