package prodigy.pantri;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

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

        // Get recipes (AsyncTask?)
        recipes = new Recipe[7];
        for (int i = 0; i < 7; i++) {
            recipes[i] = new Recipe();
        }
        recipes[0].thumbnail = "http://images.media-allrecipes.com/userphotos/250x250/1000798.jpg";
        recipes[0].image = "http://images.media-allrecipes.com/userphotos/720x405/1000798.jpg";
        recipes[0].name = "Crispy Roasted Chicken";
        recipes[0].ingredients = new LinkedList<String>();
        recipes[0].ingredients.add(0, "Heyyyy ;)");
        recipes[0].steps = new LinkedList<String>();
        recipes[0].steps.add(0, "lmao");
        recipes[1].thumbnail = "http://images.media-allrecipes.com/userphotos/250x250/285788.jpg";
        recipes[1].image = "http://images.media-allrecipes.com/userphotos/720x405/285788.jpg";
        recipes[1].name = "Sauceless Garden Lasagna";
        recipes[1].ingredients = new LinkedList<String>();
        recipes[1].ingredients.add(0, "Heyyyy ;)");
        recipes[1].steps = new LinkedList<String>();
        recipes[1].steps.add(0, "lmao");
        recipes[2].thumbnail = "http://images.media-allrecipes.com/userphotos/250x250/1380312.jpg";
        recipes[2].image = "http://images.media-allrecipes.com/userphotos/720x405/1380312.jpg";
        recipes[2].name = "One Pan Orecchiette Pasta";
        recipes[2].ingredients = new LinkedList<String>();
        recipes[2].ingredients.add(0, "Heyyyy ;)");
        recipes[2].steps = new LinkedList<String>();
        recipes[2].steps.add(0, "lmao");
        recipes[3].thumbnail = "http://images.media-allrecipes.com/userphotos/250x250/842371.jpg";
        recipes[3].image = "http://images.media-allrecipes.com/userphotos/720x405/842371.jpg";
        recipes[3].name = "Fluffy Pancakes";
        recipes[3].ingredients = new LinkedList<String>();
        recipes[3].ingredients.add(0, "Heyyyy ;)");
        recipes[3].steps = new LinkedList<String>();
        recipes[3].steps.add(0, "lmao");
        recipes[4].thumbnail = "http://images.media-allrecipes.com/userphotos/250x250/618489.jpg";
        recipes[4].image = "http://images.media-allrecipes.com/userphotos/720x405/618489.jpg";
        recipes[4].name = "Baked Garlic Parmesan Chicken";
        recipes[4].ingredients = new LinkedList<String>();
        recipes[4].ingredients.add(0, "Heyyyy ;)");
        recipes[4].steps = new LinkedList<String>();
        recipes[4].steps.add(0, "lmao");
        recipes[5].thumbnail = "http://images.media-allrecipes.com/userphotos/250x250/1000798.jpg";
        recipes[5].image = "http://images.media-allrecipes.com/userphotos/720x405/1000798.jpg";
        recipes[5].name = "Crispy Roasted Chicken";
        recipes[5].ingredients = new LinkedList<String>();
        recipes[5].ingredients.add(0, "Heyyyy ;)");
        recipes[5].steps = new LinkedList<String>();
        recipes[5].steps.add(0, "lmao");
        recipes[6].thumbnail = "http://images.media-allrecipes.com/userphotos/250x250/285788.jpg";
        recipes[6].image = "http://images.media-allrecipes.com/userphotos/720x405/285788.jpg";
        recipes[6].name = "Sauceless Garden Lasagna";
        recipes[6].ingredients = new LinkedList<String>();
        recipes[6].ingredients.add(0, "Heyyyy ;)");
        recipes[6].steps = new LinkedList<String>();
        recipes[6].steps.add(0, "lmao");


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
