package prodigy.pantri;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import prodigy.pantri.util.Ingredient;
import prodigy.pantri.util.PantryItemAdapter;

public class ShoppingListActivity extends PantriBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app.replaceLayout(this, R.layout.content_shopping_list);
        setTitle("Shopping List");

        // Get list of ingredients
        List<Ingredient> ingredientList = new ArrayList<Ingredient>();

        // TODO - Get Pantry List
        Ingredient i1 = new Ingredient();
        i1.name = "Potatoes";
        Ingredient i2 = new Ingredient();
        i2.name = "Potatoes2";
        Ingredient i3 = new Ingredient();
        i3.name = "Potatoes3";

        ingredientList.add(i1);
        ingredientList.add(i2);
        ingredientList.add(i3);
        ingredientList.add(i1);
        ingredientList.add(i2);
        ingredientList.add(i3);
        ingredientList.add(i1);
        ingredientList.add(i2);
        ingredientList.add(i3);
        ingredientList.add(i1);
        ingredientList.add(i2);
        ingredientList.add(i3);
        ingredientList.add(i1);
        ingredientList.add(i2);
        ingredientList.add(i3);
        ingredientList.add(i1);
        ingredientList.add(i2);
        ingredientList.add(i3);

        PantryItemAdapter pantryItemAdapter = new PantryItemAdapter(this, ingredientList, null);
        ListView listView = (ListView) findViewById(R.id.shopping_list);
        listView.setAdapter(pantryItemAdapter);
    }
}
