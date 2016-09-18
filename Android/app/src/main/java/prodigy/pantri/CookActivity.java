package prodigy.pantri;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import prodigy.pantri.util.PantriApplication;
import prodigy.pantri.util.PantryItemAdapter;
import prodigy.pantri.util.Recipe;
import prodigy.pantri.util.RecipeListAdapter;
import prodigy.pantri.util.ServerCommsTask;
import prodigy.pantri.util.TaskType;

public class CookActivity extends PantriBaseActivity implements Runnable {
    private ListView listView;
    private RecipeListAdapter listAdapter;
    private ServerCommsTask mTask;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app.replaceLayout(this, R.layout.content_cook);
        setTitle("Cook");

        // Get recipes (AsyncTask?)
        mTask = new ServerCommsTask(TaskType.LIST_RECIPES, (PantriApplication) getApplication());
        mTask.execute();

        mHandler = new Handler();
        mHandler.post(this);
    }

    @Override
    public void run() {
        while (mTask.recipes == null);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mTask.recipes.size() > 0) {
                    // Set up list view
                    listAdapter = new RecipeListAdapter(getApplicationContext(), mTask.recipes);
                    listView = (ListView) findViewById(R.id.list_view);
                    listView.setAdapter(listAdapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getApplicationContext(), RecipeActivity.class);
                            intent.putExtra("recipe", mTask.recipes.get(position));
                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }
}
