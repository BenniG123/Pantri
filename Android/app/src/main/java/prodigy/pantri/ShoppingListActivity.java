package prodigy.pantri;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ShoppingListActivity extends PantriBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app.replaceLayout(this, R.layout.content_shopping_list);
        setTitle("Shopping List");
    }
}
