package prodigy.pantri;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import prodigy.pantri.util.AddFoodAdapter;
import prodigy.pantri.util.SubFoodAdapter;

public class AddSubFoodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sub_food);
        Bundle extras = getIntent().getExtras();
        String subGroup = extras.getString("Food Group");
        ArrayList<String> ingredients = new ArrayList<>();

        if (subGroup != null) {
            ingredients.add(subGroup);
        }

        final GridView gridView = (GridView) findViewById(R.id.grid_sub_food_group);

        // TODO - Collect proper ingredient list
        ingredients.add("Grains");
        ingredients.add("Fruit");
        ingredients.add("Vegetables");
        ingredients.add("Meat");
        ingredients.add("Dairy");
        ingredients.add("Confectionery");

        SubFoodAdapter addSubFoodAdapter = new SubFoodAdapter(getApplicationContext(), ingredients);
        gridView.setAdapter(addSubFoodAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Add item to pantry
                // Return to add food activity
            }
        });

    }

}
