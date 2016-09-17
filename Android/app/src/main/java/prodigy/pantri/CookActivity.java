package prodigy.pantri;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import prodigy.pantri.util.Recipe;
import prodigy.pantri.util.RecipeListAdapter;

public class CookActivity extends PantriBaseActivity implements AdapterView.OnItemClickListener {
    private Recipe[] recipes;
    private ListView listView;
    private RecipeListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app.replaceLayout(this, R.layout.content_cook);

        // Set up list view
        listAdapter = new RecipeListAdapter(this, recipes);
        listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra("recipe", recipes[i]);
        startActivity(intent);
    }
}
