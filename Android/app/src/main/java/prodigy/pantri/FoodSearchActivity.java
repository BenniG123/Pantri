package prodigy.pantri;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

import prodigy.pantri.util.Ingredient;

public class FoodSearchActivity extends PantriBaseActivity implements SearchView.OnQueryTextListener, ListView.OnItemClickListener {
    private SearchView searchView;
    private ListView listView;
    private ArrayList<String> ingredientNames;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app.replaceLayout(this, R.layout.content_food_search);
        setTitle("Add Food");

        searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setIconifiedByDefault(false);
        searchView.requestFocus();
        searchView.setOnQueryTextListener(this);

        listView = (ListView) findViewById(R.id.list_view);
        ingredientNames = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ingredientNames);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        // Update the ingredients suggested


        adapter.notifyDataSetChanged();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, NewFoodActivity.class);
        intent.putExtra("name", ingredientNames.get(0));
        startActivity(intent);
    }

    private ArrayList<Ingredient> getMasterIngredientMatches() {
        return null;
    }
}
