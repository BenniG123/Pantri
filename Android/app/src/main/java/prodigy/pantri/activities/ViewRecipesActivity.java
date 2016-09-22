package prodigy.pantri.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import prodigy.pantri.R;
import prodigy.pantri.adapters.RecipeAdapter;
import prodigy.pantri.util.PantriApplication;
import prodigy.pantri.util.PantriCallback;
import prodigy.pantri.models.Recipe;
import prodigy.pantri.asynctasks.ServerCommsTask;
import prodigy.pantri.models.TaskType;

public class ViewRecipesActivity extends PantriBaseActivity implements PantriCallback<List<Recipe>> {
    private List<Recipe> mRecipeList;
    private ServerCommsTask mTask;
    private GridView mGrid;
    private TextView mPlaceholder;
    private ViewRecipesActivity mViewRecipesActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app.replaceLayout(this, R.layout.activity_view_recipes);
        setTitle("View Recipes");
        refresh();
        mViewRecipesActivity = this;
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
        if (mTask.getStatus() == AsyncTask.Status.PENDING) {
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

                mGrid.setAdapter(new RecipeAdapter(getApplicationContext(), mRecipeList, mViewRecipesActivity));
            }
        });
    }
}
