package prodigy.pantri;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import prodigy.pantri.util.AddFoodAdapter;

public class AddFoodActivity extends PantriBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app.replaceLayout(this, R.layout.content_add_food);
        setTitle("Add Food");

        final GridView gridView = (GridView) findViewById(R.id.grid_food_group);
        ArrayList<Integer> images = new ArrayList<>();
        final ArrayList<String> names = new ArrayList<>();

        images.add(R.drawable.grains);
        images.add(R.drawable.fruit);
        images.add(R.drawable.vegetables);
        images.add(R.drawable.meat);
        images.add(R.drawable.dairy);
        images.add(R.drawable.confectionery);

        names.add("Grains");
        names.add("Fruit");
        names.add("Vegetables");
        names.add("Meat");
        names.add("Dairy");
        names.add("Other");

        AddFoodAdapter addFoodAdapter = new AddFoodAdapter(getApplicationContext(), images, names);
        gridView.setAdapter(addFoodAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Navigator to SubFood Activity, Specifying Food Group
                Intent intent = new Intent(getApplicationContext(), AddSubFoodActivity.class);
                intent.putExtra("Food Group", names.get(position));
                startActivity(intent);
            }
        });

    }
}
