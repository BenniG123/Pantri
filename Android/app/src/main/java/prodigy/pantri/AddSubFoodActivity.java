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
import java.util.Arrays;

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

        final GridView gridView = (GridView) findViewById(R.id.grid_sub_food_group);

        if (subGroup != null) {
            setTitle("Add " + subGroup);
            switch (subGroup) {
                case "Fruit":
                    ingredients.addAll(Arrays.asList(getResources().getStringArray(R.array.fruits_array)));
                    break;
                case "Grains":
                    ingredients.addAll(Arrays.asList(getResources().getStringArray(R.array.grains_array)));
                    break;
                case "Vegetables":
                    ingredients.addAll(Arrays.asList(getResources().getStringArray(R.array.vegetable_array)));
                    break;
                case "Dairy":
                    ingredients.addAll(Arrays.asList(getResources().getStringArray(R.array.dairy_array)));
                    break;
                case "Meat":
                    ingredients.addAll(Arrays.asList(getResources().getStringArray(R.array.meat_array)));
                    break;
                case "Other":
                    ingredients.addAll(Arrays.asList(getResources().getStringArray(R.array.other_array)));
                    break;
            }
        }
        else {
            setTitle("Add Food to your Pantry!");
        }

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
