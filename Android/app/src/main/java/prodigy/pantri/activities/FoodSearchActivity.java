package prodigy.pantri.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import prodigy.pantri.R;
import prodigy.pantri.asynctasks.AddIngredientAsyncTask;
import prodigy.pantri.asynctasks.LookupIngredientUPCAsyncTask;
import prodigy.pantri.models.Ingredient;
import prodigy.pantri.util.LevenshteinComparator;
import prodigy.pantri.util.PantriCallback;

public class FoodSearchActivity extends PantriBaseActivity implements SearchView.OnQueryTextListener, ListView.OnItemClickListener, PantriCallback<Ingredient> {
    private SearchView mSearchView;
    private ArrayList<String> mIngredientNames;
    private ArrayAdapter<String> mAdapter;
    private String[] mIngredientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app.replaceLayout(this, R.layout.activity_food_search);
        setTitle("Add Food");

        mIngredientList = getResources().getStringArray(R.array.master_ingredients);
        mSearchView = (SearchView) findViewById(R.id.search_view);
        mSearchView.setIconifiedByDefault(false);
        mSearchView.requestFocus();
        mSearchView.setOnQueryTextListener(this);

        ListView listView = (ListView) findViewById(R.id.list_view);
        mIngredientNames = new ArrayList<>();
        mIngredientNames.addAll(getMasterIngredientMatches(""));
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mIngredientNames);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (s.length() > 1 && s.charAt(s.length() - 1) == ' ' && s.substring(0, s.length() - 1).matches("^[0-9]+$")) {
            // If match found for UPC, add it and let the user know
            LookupIngredientUPCAsyncTask task = new LookupIngredientUPCAsyncTask(app, s.substring(0, s.length()-1), this);
            task.execute();
            mSearchView.setQuery("", false);
        }
        else if (!s.substring(0, s.length()).matches("^[0-9]+$")) {
            // Update the ingredients suggested
            mIngredientNames.clear();
            mIngredientNames.addAll(getMasterIngredientMatches(s));
            mAdapter.notifyDataSetChanged();
        }
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, NewFoodActivity.class);
        intent.putExtra("name", mIngredientNames.get(i));
        startActivity(intent);
        mSearchView.setQuery("", false);
    }

    private ArrayList<String> getMasterIngredientMatches(String toMatch) {
        ArrayList<String> ret = new ArrayList<>();
        for (int i = 0; i < mIngredientList.length; i++) {
            if (mIngredientList[i].toLowerCase().contains(toMatch.toLowerCase())) {
                ret.add(mIngredientList[i]);
            }
        }

        // Sort by Levenshtein Distance
        Collections.sort(ret, new LevenshteinComparator(toMatch));

        return ret;
    }

    @Override
    public void run(Ingredient arg) {
        final Ingredient tmp = arg;

        if (arg != null) { // Match
            AddIngredientAsyncTask task = new AddIngredientAsyncTask(app, new PantriCallback<Boolean>() {
                @Override
                public void run(Boolean arg) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Successfully added " + tmp.name + "!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }, arg.id);
            task.execute();
        }
        else {
            Intent i = new Intent(this, NewFoodActivity.class);
            startActivity(i);
        }
    }
}
