package prodigy.pantri.activities;

import android.os.Bundle;
import prodigy.pantri.R;

public class ShoppingListActivity extends PantriBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app.replaceLayout(this, R.layout.activity_shopping_list);
        setTitle("Shopping List");

        /* Get list of ingredients
        List<Ingredient> ingredientList = new ArrayList<Ingredient>();

        PantryItemAdapter pantryItemAdapter = new PantryItemAdapter(this, ingredientList, null);
        ListView listView = (ListView) findViewById(R.id.shopping_list);
        listView.setAdapter(pantryItemAdapter);
        */
    }
}
