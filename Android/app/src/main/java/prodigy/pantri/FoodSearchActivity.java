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
import java.util.Collections;

import prodigy.pantri.util.Ingredient;
import prodigy.pantri.util.LevenshteinComparator;
import prodigy.pantri.util.PantriCallback;
import prodigy.pantri.util.ServerCommsTask;
import prodigy.pantri.util.TaskType;

public class FoodSearchActivity extends PantriBaseActivity implements SearchView.OnQueryTextListener, ListView.OnItemClickListener, PantriCallback<Ingredient> {
    private SearchView searchView;
    private ListView listView;
    private ArrayList<String> ingredientNames;
    private ArrayAdapter<String> adapter;
    private String[] masterIngredientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app.replaceLayout(this, R.layout.content_food_search);
        setTitle("Add Food");

        masterIngredientList = getResources().getStringArray(R.array.master_ingredients);
        searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setIconifiedByDefault(false);
        searchView.requestFocus();
        searchView.setOnQueryTextListener(this);

        listView = (ListView) findViewById(R.id.list_view);
        ingredientNames = new ArrayList<>();
        ingredientNames.addAll(getMasterIngredientMatches(""));
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ingredientNames);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        if (s.matches("^[0-9]+$")) {
            // If match found for UPC, add it and let the user know
            ServerCommsTask task = new ServerCommsTask<>(TaskType.GET_INGREDIENT_UPC, this, app, s);
            task.execute();
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        // Update the ingredients suggested
        ingredientNames.clear();
        ingredientNames.addAll(getMasterIngredientMatches(s));
        adapter.notifyDataSetChanged();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, NewFoodActivity.class);
        intent.putExtra("name", ingredientNames.get(i));
        startActivity(intent);
    }

    private ArrayList<String> getMasterIngredientMatches(String toMatch) {
        ArrayList<String> ret = new ArrayList<>();
        for (int i = 0; i < masterIngredientList.length; i++) {
            if (masterIngredientList[i].toLowerCase().contains(toMatch.toLowerCase())) {
                ret.add(masterIngredientList[i]);
            }
        }

        // Sort by Levenshtein Distance
        Collections.sort(ret, new LevenshteinComparator(toMatch));

        return ret;
    }

    @Override
    public void run(Ingredient arg) {
        if (arg != null) { // Match
            ServerCommsTask task = new ServerCommsTask<>(TaskType.ADD_INGREDIENT, null, app, arg.id);
            task.execute();
        }
        else {
            Intent i = new Intent(this, NewFoodActivity.class);
            startActivity(i);
        }
    }
}
