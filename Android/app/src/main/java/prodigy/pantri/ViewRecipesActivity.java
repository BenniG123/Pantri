package prodigy.pantri;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import prodigy.pantri.util.Ingredient;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app.replaceLayout(this, R.layout.content_view_recipes);
        setTitle("View Recipes");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTask = new ServerCommsTask(TaskType.LIST_RECIPES, (PantriApplication) getApplication());
        mTask.execute();
    }

    @Override
    public void run(List<Recipe> recipes) {
        mRecipeList = recipes;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mRecipeList.size() > 0) {
                    // Set up list view
                    listAdapter = new RecipeListAdapter(getApplicationContext(), mRecipeList);
                    listView = (ListView) findViewById(R.id.list_view);
                    listView.setAdapter(listAdapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getApplicationContext(), RecipeActivity.class);
                            intent.putExtra("recipe", mRecipeList.get(position));
                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }
}
