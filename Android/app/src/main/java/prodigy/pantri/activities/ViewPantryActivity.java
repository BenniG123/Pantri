package prodigy.pantri.activities;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import prodigy.pantri.R;
import prodigy.pantri.adapters.PantryItemAdapter;
import prodigy.pantri.models.Ingredient;
import prodigy.pantri.util.PantriCallback;
import prodigy.pantri.asynctasks.ServerCommsTask;
import prodigy.pantri.models.TaskType;

public class ViewPantryActivity extends PantriBaseActivity implements PantriCallback<List<Ingredient>> {
    private ServerCommsTask mTask;
    private List<Ingredient> mIngredientList;
    private ViewPantryActivity mViewPantryActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app.replaceLayout(this, R.layout.activity_view_pantry);
        setTitle("View Pantry");
        mIngredientList = new ArrayList<>();
        mViewPantryActivity = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    public void refresh() {
        // Get list of ingredients
        mTask = new ServerCommsTask<>(TaskType.GET_PANTRY, this, app);
        mTask.execute();
    }

    @Override
    public void run(List<Ingredient> pantry) {
        mIngredientList = pantry;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                PantryItemAdapter pantryItemAdapter = new PantryItemAdapter(app, mIngredientList, mViewPantryActivity);
                Collections.sort(mIngredientList, new Comparator<Ingredient>() {
                    @Override
                    public int compare(Ingredient o1, Ingredient o2) {
                        return o1.name.compareToIgnoreCase(o2.name);
                    }
                });

                ListView listView = (ListView) findViewById(R.id.list_pantry);
                listView.setAdapter(pantryItemAdapter);
            }
        });
    }
}
